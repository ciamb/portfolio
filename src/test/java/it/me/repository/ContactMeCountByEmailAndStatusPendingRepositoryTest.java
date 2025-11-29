package it.me.repository;

import it.me.entity.ContactMe;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class})
class ContactMeCountByEmailAndStatusPendingRepositoryTest {

    @InjectMocks
    private ContactMeCountByEmailAndStatusPendingRepository sut;

    @Mock
    private EntityManager em;

    @Mock
    TypedQuery<Long> query;

    @Test
    void countByEmailAndStatusPending() {
        //given
        given(em.createNamedQuery(eq(ContactMe.COUNT_BY_EMAIL_AND_STATUS_PENDING), eq(Long.class)))
                .willReturn(query);
        given(query.setParameter(eq("email"), anyString())).willReturn(query);
        given(query.setParameter(eq("status"), eq(ContactMe.Status.PENDING))).willReturn(query);
        given(query.getSingleResult()).willReturn(0L);

        //when
        Long count = assertDoesNotThrow(() -> sut.countContactMeByEmailAndStatusPending("email"));

        //then
        assertNotNull(count);
        assertEquals(0L, count);
        verify(em, times(1))
                .createNamedQuery(eq(ContactMe.COUNT_BY_EMAIL_AND_STATUS_PENDING), eq(Long.class));
    }
}