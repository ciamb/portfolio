package it.me.repository.contact.me.batch.log;

import it.me.domain.dto.ContactMeBatchLog;
import it.me.repository.contact.me.batch.log.mapper.ContactMeBatchLog2ContactMeBatchLogEntityMapper;
import it.me.repository.entity.ContactMeBatchLogEntity;
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
class ContactMeBatchLogPersistRepositoryJpaTest {
    @InjectMocks
    private ContactMeBatchLogPersistRepositoryJpa sut;

    @Mock
    ContactMeBatchLog2ContactMeBatchLogEntityMapper mapper;

    @Mock
    EntityManager em;

    @Test
    @DisplayName("1. Should call persist flush refresh")
    void shouldCallFlushRefresh() {
        // given
        var logEntity  = new ContactMeBatchLogEntity();
        var log = ContactMeBatchLog.builder().build();
        given(mapper.apply(log))
                .willReturn(logEntity);

        //when
        ContactMeBatchLog result = sut.persist(log);

        // then
        assertSame(log, result);
        var inOrder = inOrder(mapper,em);
        inOrder.verify(mapper).apply(log);
        inOrder.verify(em).persist(logEntity);
        inOrder.verifyNoMoreInteractions();
    }
}