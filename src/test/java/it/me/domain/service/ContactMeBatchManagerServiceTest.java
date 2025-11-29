package it.me.domain.service;

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
    ContactMeReadByStatusPendingRepository contactMeReadByStatusPendingRepository;

    @Mock
    ContactMeEmailSenderService contactMeEmailSenderService;

    @Mock
    ContactMeBatchLogPersistRepository contactMeBatchLogPersistRepository;

    @Mock
    ContactMeBatchConfig contactMeBatchConfig;

    @Mock
    ContactMeBatchLog contactMeBatchLog;

    @Test
    void shouldExecuteBatch() {
        //given
        given(contactMeBatchConfigReadByIdRepository.readByIdEquals1())
                .willReturn(Optional.of(contactMeBatchConfig));
        given(contactMeBatchConfig.isActive()).willReturn(true);
        var contactMe1 = new ContactMe().setAttempts(0);
        var contactMe2 = new ContactMe().setAttempts(0);
        List<ContactMe> listContactMe = List.of(contactMe1, contactMe2);
        given(contactMeReadByStatusPendingRepository.readAllByStatusPending())
                .willReturn(listContactMe);

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

        assertThat(contactMe1.status()).isEqualTo(ContactMe.Status.PROCESSED);
        assertThat(contactMe2.status()).isEqualTo(ContactMe.Status.PROCESSED);

        InOrder inOrder = Mockito.inOrder(
                contactMeBatchConfigReadByIdRepository,
                contactMeReadByStatusPendingRepository,
                contactMeEmailSenderService,
                contactMeBatchLogPersistRepository);
        inOrder.verify(contactMeBatchConfigReadByIdRepository).readByIdEquals1();
        inOrder.verify(contactMeReadByStatusPendingRepository).readAllByStatusPending();
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
                contactMeReadByStatusPendingRepository,
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
                contactMeReadByStatusPendingRepository,
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
        given(contactMeReadByStatusPendingRepository.readAllByStatusPending())
                .willReturn(Collections.emptyList());

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
                contactMeReadByStatusPendingRepository,
                contactMeEmailSenderService,
                contactMeBatchLogPersistRepository);
        inOrder.verify(contactMeBatchConfigReadByIdRepository, times(1))
                .readByIdEquals1();
        inOrder.verify(contactMeReadByStatusPendingRepository, times(1))
                .readAllByStatusPending();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldExecuteBatch_andFailAll() {
        //given
        given(contactMeBatchConfigReadByIdRepository.readByIdEquals1())
                .willReturn(Optional.of(contactMeBatchConfig));
        given(contactMeBatchConfig.isActive()).willReturn(true);
        var contactMe1 = new ContactMe().setAttempts(0);
        var contactMe2 = new ContactMe().setAttempts(0);
        List<ContactMe> listContactMe = List.of(contactMe1, contactMe2);
        given(contactMeReadByStatusPendingRepository.readAllByStatusPending())
                .willReturn(listContactMe);

        given(contactMeBatchConfig.targetEmail())
                .willReturn("toemailsend");

        willThrow(new RuntimeException("Z".repeat(600)))
                .given(contactMeEmailSenderService)
                .sendSummaryEmailForPendingList(listContactMe, "toemailsend");

        given(contactMeBatchLogPersistRepository.persist(any(ContactMeBatchLog.class)))
                .willReturn(contactMeBatchLog);
        given(contactMeBatchLog.id()).willReturn(1L);

        //when
        ContactMeBatchResponse response = sut.executeBatch();

        //then
        assertTrue(response.configActive());
        assertThat(response.logId()).isEqualTo(1L);
        assertThat(response.processed()).isEqualTo(2);
        assertThat(response.withError()).isEqualTo(2);
        assertThat(response.sentTo()).isEqualTo("toemailsend");

        assertThat(contactMe1.status()).isEqualTo(ContactMe.Status.ERROR);
        assertThat(contactMe2.status()).isEqualTo(ContactMe.Status.ERROR);

        InOrder inOrder = Mockito.inOrder(
                contactMeBatchConfigReadByIdRepository,
                contactMeReadByStatusPendingRepository,
                contactMeEmailSenderService,
                contactMeBatchLogPersistRepository);
        inOrder.verify(contactMeBatchConfigReadByIdRepository).readByIdEquals1();
        inOrder.verify(contactMeReadByStatusPendingRepository).readAllByStatusPending();
        inOrder.verify(contactMeEmailSenderService).sendSummaryEmailForPendingList(listContactMe, "toemailsend");
        inOrder.verify(contactMeBatchLogPersistRepository).persist(any(ContactMeBatchLog.class));
        inOrder.verifyNoMoreInteractions();
    }
}