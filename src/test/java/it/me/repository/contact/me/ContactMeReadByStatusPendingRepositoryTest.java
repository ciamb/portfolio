package it.me.repository.contact.me;

import it.me.repository.entity.ContactMeEntity;
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
    TypedQuery<ContactMeEntity> query;

    @Test
    @DisplayName("Should read all contact me")
    void readAll() {
        //given
        List<ContactMeEntity> list = Arrays.asList(new ContactMeEntity(), new ContactMeEntity());
        given(em.createNamedQuery(eq(ContactMeEntity.READ_ALL_BY_STATUS_PENDING), eq(ContactMeEntity.class)))
                .willReturn(query);
        given(query.getResultList()).willReturn(list);

        //when
        List<ContactMeEntity> result = assertDoesNotThrow(() -> sut.readAllByStatusPending());

        //then
        assertFalse(result.isEmpty());
        assertEquals(list, result);
        assertEquals(list.size(), result.size());
    }
}