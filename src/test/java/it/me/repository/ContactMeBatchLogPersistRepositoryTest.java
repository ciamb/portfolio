package it.me.repository;

import it.me.entity.ContactMe;
import it.me.entity.ContactMeBatchLog;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.inOrder;

@ExtendWith(MockitoExtension.class)
class ContactMeBatchLogPersistRepositoryTest {
    @InjectMocks
    private ContactMeBatchLogPersistRepository sut;

    @Mock
    EntityManager em;

    @Test
    @DisplayName("1. Should call persist flush refresh")
    void shouldCallFlushRefresh() {
        // given
        var log  = new ContactMeBatchLog();

        //when
        ContactMeBatchLog result = sut.persist(log);

        // then
        assertSame(log, result);
        var inOrder = inOrder(em);
        inOrder.verify(em).persist(log);
        inOrder.verify(em).flush();
        inOrder.verify(em).refresh(log);
        inOrder.verifyNoMoreInteractions();
    }
}