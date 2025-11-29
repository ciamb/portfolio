package it.me.repository;

import it.me.entity.ContactMe;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ContactMeReadByStatusPendingRepositoryTest {

    @InjectMocks
    private ContactMeReadByStatusPendingRepository sut;

    @Mock
    EntityManager em;

    @Mock
    TypedQuery<ContactMe> query;

    @Test
    @DisplayName("Should read all contact me")
    void readAll() {
        //given
        List<ContactMe> list = Arrays.asList(new ContactMe(), new ContactMe());
        given(em.createNamedQuery(eq(ContactMe.READ_ALL_BY_STATUS_PENDING), eq(ContactMe.class)))
                .willReturn(query);
        given(query.getResultList()).willReturn(list);

        //when
        List<ContactMe> result = assertDoesNotThrow(() -> sut.readAllByStatusPending());

        //then
        assertFalse(result.isEmpty());
        assertEquals(list, result);
        assertEquals(list.size(), result.size());
    }
}