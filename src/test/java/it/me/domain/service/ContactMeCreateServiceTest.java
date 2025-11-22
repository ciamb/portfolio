package it.me.domain.service;

import it.me.entity.ContactMe;
import it.me.repository.ContactMePersistRepository;
import it.me.web.dto.request.ContactMeRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
    ContactMeRequest contactMeRequest;

    @Test
    void createContactMe_shouldCreate() {
        //given
        given(contactMeRequest.email()).willReturn("email");
        given(contactMeRequest.name()).willReturn("name");
        given(contactMeRequest.message()).willReturn("message");
        given(contactMeRequest.contactBack()).willReturn(true);

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
        var inOrder = Mockito.inOrder(contactMePersistRepository);
        inOrder.verify(contactMePersistRepository, times(1)).persist(contactMe.capture());
        inOrder.verifyNoMoreInteractions();

        assertSame(result, contactMe.getValue());
    }

}