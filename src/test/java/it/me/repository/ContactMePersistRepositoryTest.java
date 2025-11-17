package it.me.repository;

import it.me.entity.ContactMe;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;

@ExtendWith(MockitoExtension.class)
class ContactMePersistRepositoryTest {

    @InjectMocks
    private ContactMePersistRepository sut;

    @Mock
    EntityManager em;

    @Test
    @DisplayName("1. Should call persist flush refresh")
    void shouldCallFlushRefresh() {
        // given
        var contactMe = new ContactMe();

        //when
        ContactMe result = sut.persist(contactMe);

        // then
        assertSame(contactMe, result);
        var inOrder = inOrder(em);
        inOrder.verify(em).persist(contactMe);
        inOrder.verify(em).flush();
        inOrder.verify(em).refresh(contactMe);
        inOrder.verifyNoMoreInteractions();
    }

}