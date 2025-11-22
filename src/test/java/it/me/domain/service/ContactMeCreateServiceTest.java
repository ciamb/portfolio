package it.me.domain.service;

import it.me.entity.ContactMe;
import it.me.repository.ContactMeCountByEmailAndStatusPendingRepository;
import it.me.repository.ContactMePersistRepository;
import it.me.web.dto.request.ContactMeRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactMeCreateServiceTest {

    @InjectMocks
    private ContactMeCreateService sut;

    @Mock
    ContactMePersistRepository contactMePersistRepository;

    @Mock
    ContactMeCountByEmailAndStatusPendingRepository contactMeCountByEmailAndStatusPendingRepository;

    @Mock
    ContactMeRequest contactMeRequest;

    @Test
    void createContactMe_shouldCreate() {
        //given
        given(contactMeRequest.email()).willReturn("email");
        given(contactMeRequest.name()).willReturn("name");
        given(contactMeRequest.message()).willReturn("message");
        given(contactMeRequest.contactBack()).willReturn(true);

        given(contactMeCountByEmailAndStatusPendingRepository.countContactMeByEmailAndStatusPending(eq("email")))
                .willReturn(0L);
        given(contactMePersistRepository.persist(any(ContactMe.class)))
                .willAnswer(invocation -> invocation.getArgument(0, ContactMe.class));

        //when
        ContactMe result = sut.createContactMe(contactMeRequest);
        ArgumentCaptor<ContactMe> contactMe = ArgumentCaptor.forClass(ContactMe.class);

        //then
        assertNotNull(result);
        assertEquals("email", result.email());
        assertEquals("name", result.name());
        assertEquals("message", result.message());
        var inOrder = Mockito.inOrder(
                contactMePersistRepository,
                contactMeCountByEmailAndStatusPendingRepository
        );
        inOrder.verify(contactMeCountByEmailAndStatusPendingRepository, times(1))
                .countContactMeByEmailAndStatusPending(eq("email"));
        inOrder.verify(contactMePersistRepository, times(1))
                .persist(contactMe.capture());
        inOrder.verifyNoMoreInteractions();

        assertSame(result, contactMe.getValue());
    }

    @Test
    void createContactMe_shouldThrowIllegalArgumentException() {
        //given
        given(contactMeRequest.email()).willReturn("email");
        given(contactMeCountByEmailAndStatusPendingRepository.countContactMeByEmailAndStatusPending(eq("email")))
                .willReturn(1L);

        //when
        IllegalStateException ise = assertThrows(IllegalStateException.class,
                () -> sut.createContactMe(contactMeRequest));

        //then
        assertThat(ise).isInstanceOf(IllegalStateException.class);
        assertThat(ise.getMessage()).contains("inserito un messaggio!");
        var inOrder = Mockito.inOrder(contactMeCountByEmailAndStatusPendingRepository);
        inOrder.verify(contactMeCountByEmailAndStatusPendingRepository, times(1))
                        .countContactMeByEmailAndStatusPending(eq("email"));
        inOrder.verifyNoMoreInteractions();
    }

}