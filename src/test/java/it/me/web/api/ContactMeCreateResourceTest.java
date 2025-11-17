package it.me.web.api;

import it.me.domain.mapper.ContactBackToMessageMapper;
import it.me.domain.service.ContactMeCreateService;
import it.me.entity.ContactMe;
import it.me.web.dto.ContactMeRequest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ContactMeCreateResourceTest {

    @InjectMocks
    private ContactMeCreateResource sut;

    @Mock
    ContactMeCreateService contactMeCreateService;

    @Mock
    ContactBackToMessageMapper contactBackToMessageMapper;

    @Mock
    ContactMeRequest contactMeRequest;

    @Test
    void createContactMe() {
        // given
        var contactMe = new ContactMe()
                .setContactBack(false);
        given(contactMeCreateService.createContactMe(eq(contactMeRequest)))
                .willReturn(contactMe);
        given(contactBackToMessageMapper.apply(eq(contactMe.contactBack())))
                .willReturn("Grazie del messaggio");

        //when
        Response result = assertDoesNotThrow(() -> sut.contactMe(contactMeRequest));

        //then
        assertEquals(Response.Status.CREATED.getStatusCode(), result.getStatus());
        var inOrder = Mockito.inOrder(contactMeCreateService, contactBackToMessageMapper);
        inOrder.verify(contactMeCreateService).createContactMe(eq(contactMeRequest));
        inOrder.verify(contactBackToMessageMapper).apply(eq(contactMe.contactBack()));
        inOrder.verifyNoMoreInteractions();
    }
}