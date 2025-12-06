package it.me.domain.service.contact.me.batch;

import it.me.domain.dto.ProcessedContactMe;
import it.me.domain.dto.ProcessedInfo;
import it.me.domain.service.contact.me.ContactMeEmailSenderService;
import it.me.domain.service.contact.me.ContactMeProcessingService;
import it.me.repository.entity.ContactMeBatchConfigEntity;
import it.me.repository.entity.ContactMeBatchLogEntity;
import it.me.repository.contact.me.batch.config.ContactMeBatchConfigReadByIdRepository;
import it.me.repository.contact.me.batch.log.ContactMeBatchLogPersistRepository;
import it.me.web.dto.response.ContactMeBatchResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
    ContactMeBatchConfigEntity contactMeBatchConfigEntity;

    @Mock
    ContactMeBatchLogEntity contactMeBatchLogEntity;

    @Mock
    ProcessedInfo processedInfo;

    @Test
    void shouldExecuteBatch() {
        //given
        given(contactMeBatchConfigReadByIdRepository.readByIdEquals1())
                .willReturn(Optional.of(contactMeBatchConfigEntity));
        given(contactMeBatchConfigEntity.isActive()).willReturn(true);
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

        given(contactMeBatchConfigEntity.targetEmail())
                .willReturn("toemailsend");

        given(contactMeBatchLogPersistRepository.persist(any(ContactMeBatchLogEntity.class)))
                .willReturn(contactMeBatchLogEntity);
        given(contactMeBatchLogEntity.id()).willReturn(1L);

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
        inOrder.verify(contactMeBatchLogPersistRepository).persist(any(ContactMeBatchLogEntity.class));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldExecuteBatch_responseNotActive() {
        //given
        given(contactMeBatchConfigReadByIdRepository.readByIdEquals1())
                .willReturn(Optional.of(contactMeBatchConfigEntity));
        given(contactMeBatchConfigEntity.isActive()).willReturn(false);

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
                .willReturn(Optional.of(contactMeBatchConfigEntity));
        given(contactMeBatchConfigEntity.isActive()).willReturn(true);
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