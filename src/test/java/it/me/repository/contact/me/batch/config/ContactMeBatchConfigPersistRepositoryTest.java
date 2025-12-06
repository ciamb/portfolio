package it.me.repository.contact.me.batch.config;

import it.me.repository.entity.ContactMeBatchConfigEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.inOrder;

@ExtendWith(MockitoExtension.class)
class ContactMeBatchConfigPersistRepositoryTest {
    @InjectMocks
    private ContactMeBatchConfigPersistRepository sut;

    @Mock
    EntityManager em;

    @Test
    @DisplayName("1. Should call persist flush refresh")
    void shouldCallFlushRefresh() {
        // given
        var config = new ContactMeBatchConfigEntity();

        //when
        ContactMeBatchConfigEntity result = sut.persist(config);

        // then
        assertSame(config, result);
        var inOrder = inOrder(em);
        inOrder.verify(em).persist(config);
        inOrder.verify(em).flush();
        inOrder.verify(em).refresh(config);
        inOrder.verifyNoMoreInteractions();
    }
}