package it.me.repository;

import it.me.entity.ContactMeBatchConfig;
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
    TypedQuery<ContactMeBatchConfig> query;

    @Test
    @DisplayName("Should call entity manager with right query")
    void readByIdRightQuery() {
        //given
        ContactMeBatchConfig contactMeBatchConfig = new ContactMeBatchConfig();
        given(em.createNamedQuery(eq(ContactMeBatchConfig.READ_BY_ID), eq(ContactMeBatchConfig.class)))
                .willReturn(query);
        given(query.setParameter("id", 1))
                .willReturn(query);
        given(query.getResultStream()).willReturn(Stream.of(contactMeBatchConfig));

        //when
        Optional<ContactMeBatchConfig> result = assertDoesNotThrow(() -> sut.readByIdEquals1());

        //then
        assertTrue(result.isPresent());
        assertEquals(contactMeBatchConfig, result.get());
        verify(em, times(1))
                .createNamedQuery(eq(ContactMeBatchConfig.READ_BY_ID), eq(ContactMeBatchConfig.class));
    }
}