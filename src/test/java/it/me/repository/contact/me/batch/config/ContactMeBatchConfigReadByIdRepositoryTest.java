package it.me.repository.contact.me.batch.config;

import it.me.repository.entity.ContactMeBatchConfigEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ContactMeBatchConfigReadByIdRepositoryTest {

    @InjectMocks
    private ContactMeBatchConfigReadByIdRepository sut;

    @Mock
    EntityManager em;

    @Mock
    TypedQuery<ContactMeBatchConfigEntity> query;

    @Test
    @DisplayName("Should call entity manager with right query")
    void readByIdRightQuery() {
        //given
        ContactMeBatchConfigEntity contactMeBatchConfigEntity = new ContactMeBatchConfigEntity();
        given(em.createNamedQuery(eq(ContactMeBatchConfigEntity.READ_BY_ID), eq(ContactMeBatchConfigEntity.class)))
                .willReturn(query);
        given(query.setParameter("id", 1))
                .willReturn(query);
        given(query.getResultStream()).willReturn(Stream.of(contactMeBatchConfigEntity));

        //when
        Optional<ContactMeBatchConfigEntity> result = assertDoesNotThrow(() -> sut.readByIdEquals1());

        //then
        assertTrue(result.isPresent());
        assertEquals(contactMeBatchConfigEntity, result.get());
        verify(em, times(1))
                .createNamedQuery(eq(ContactMeBatchConfigEntity.READ_BY_ID), eq(ContactMeBatchConfigEntity.class));
    }
}