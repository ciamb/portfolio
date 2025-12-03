package it.me.domain.service;

import it.me.domain.dto.ProcessedContactMe;
import it.me.domain.dto.ProcessedInfo;
import it.me.entity.ContactMe;
import it.me.entity.ContactMeBatchConfig;
import it.me.entity.ContactMeBatchLog;
import it.me.repository.ContactMeBatchConfigReadByIdRepository;
import it.me.repository.ContactMeBatchLogPersistRepository;
import it.me.repository.ContactMeReadByStatusPendingRepository;
import it.me.web.dto.response.ContactMeBatchResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ContactMeBatchManagerServiceTest {

    @InjectMocks
    private ContactMeBatchManagerService sut;

    @Mock
    ContactMeBatchConfigReadByIdRepository contactMeBatchConfigReadByIdRepository;

    @Mock
    ContactMeEmailSenderService contactMeEmailSenderService;

    @Mock
    ContactMeBatchLogPersistRepository contactMeBatchLogPersistRepository;

    @Mock
    ContactMeProcessingService contactMeProcessingService;

    @Mock
    ContactMeBatchConfig contactMeBatchConfig;

    @Mock
    ContactMeBatchLog contactMeBatchLog;

    @Mock
    ProcessedInfo processedInfo;

    @Test
    void shouldExecuteBatch() {
        //given
        given(contactMeBatchConfigReadByIdRepository.readByIdEquals1())
                .willReturn(Optional.of(contactMeBatchConfig));
        given(contactMeBatchConfig.isActive()).willReturn(true);
        var contactMe1 = ProcessedContactMe.builder()
                .attempts(0)
                .build();
        var contactMe2 = ProcessedContactMe.builder()
                .attempts(0)
                .build();
        List<ProcessedContactMe> listContactMe = List.of(contactMe1, contactMe2);
        given(contactMeProcessingService.processPendingContactMe())
                .willReturn(processedInfo);
        given(processedInfo.processedContactMe())
                .willReturn(listContactMe);
        given(processedInfo.processed())
                .willReturn(2);

        given(contactMeBatchConfig.targetEmail())
                .willReturn("toemailsend");

        given(contactMeBatchLogPersistRepository.persist(any(ContactMeBatchLog.class)))
                .willReturn(contactMeBatchLog);
        given(contactMeBatchLog.id()).willReturn(1L);

        //when
        ContactMeBatchResponse response = sut.executeBatch();

        //then
        assertTrue(response.configActive());
        assertThat(response.logId()).isEqualTo(1L);
        assertThat(response.processed()).isEqualTo(2);
        assertThat(response.withError()).isEqualTo(0);
        assertThat(response.sentTo()).isEqualTo("toemailsend");

        InOrder inOrder = Mockito.inOrder(
                contactMeBatchConfigReadByIdRepository,
                contactMeProcessingService,
                contactMeEmailSenderService,
                contactMeBatchLogPersistRepository);
        inOrder.verify(contactMeBatchConfigReadByIdRepository).readByIdEquals1();
        inOrder.verify(contactMeProcessingService).processPendingContactMe();
        inOrder.verify(contactMeEmailSenderService).sendSummaryEmailForPendingList(listContactMe, "toemailsend");
        inOrder.verify(contactMeBatchLogPersistRepository).persist(any(ContactMeBatchLog.class));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldExecuteBatch_responseNotActive() {
        //given
        given(contactMeBatchConfigReadByIdRepository.readByIdEquals1())
                .willReturn(Optional.of(contactMeBatchConfig));
        given(contactMeBatchConfig.isActive()).willReturn(false);

        //when
        ContactMeBatchResponse response = sut.executeBatch();

        //then
        assertFalse(response.configActive());
        assertThat(response.logId()).isNull();
        assertThat(response.processed()).isEqualTo(0);
        assertThat(response.withError()).isEqualTo(0);
        assertThat(response.sentTo()).isNull();

        InOrder inOrder = Mockito.inOrder(
                contactMeBatchConfigReadByIdRepository,
                contactMeProcessingService,
                contactMeEmailSenderService,
                contactMeBatchLogPersistRepository);
        inOrder.verify(contactMeBatchConfigReadByIdRepository).readByIdEquals1();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowISE_whenReadConfig() {
        //given
        given(contactMeBatchConfigReadByIdRepository.readByIdEquals1())
                .willReturn(Optional.empty());

        //when
        IllegalStateException ise = assertThrows(IllegalStateException.class,
                () -> sut.executeBatch());

        //then
        assertTrue(ise.getMessage().contains("No contact me batch config present"));

        InOrder inOrder = Mockito.inOrder(
                contactMeBatchConfigReadByIdRepository,
                contactMeProcessingService,
                contactMeEmailSenderService,
                contactMeBatchLogPersistRepository);
        inOrder.verify(contactMeBatchConfigReadByIdRepository).readByIdEquals1();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldExecuteBatch_withZeroMessage() {
        //given
        given(contactMeBatchConfigReadByIdRepository.readByIdEquals1())
                .willReturn(Optional.of(contactMeBatchConfig));
        given(contactMeBatchConfig.isActive()).willReturn(true);
        given(contactMeProcessingService.processPendingContactMe())
                .willReturn(processedInfo);

        //when
        ContactMeBatchResponse response = sut.executeBatch();

        //then
        assertTrue(response.configActive());
        assertThat(response.logId()).isNull();
        assertThat(response.processed()).isEqualTo(0);
        assertThat(response.withError()).isEqualTo(0);
        assertThat(response.sentTo()).isNull();

        InOrder inOrder = Mockito.inOrder(
                contactMeBatchConfigReadByIdRepository,
                contactMeProcessingService,
                contactMeEmailSenderService,
                contactMeBatchLogPersistRepository);
        inOrder.verify(contactMeBatchConfigReadByIdRepository, times(1))
                .readByIdEquals1();
        inOrder.verify(contactMeProcessingService, times(1))
                .processPendingContactMe();
        inOrder.verifyNoMoreInteractions();
    }
}