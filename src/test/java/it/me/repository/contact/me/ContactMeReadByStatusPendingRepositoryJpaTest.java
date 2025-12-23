package it.me.repository.contact.me;

import it.me.domain.dto.ContactMe;
import it.me.repository.contact.me.mapper.ContactMeEntity2ContactMeMapper;
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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ContactMeReadByStatusPendingRepositoryJpaTest {

    @InjectMocks
    private ContactMeReadByStatusPendingRepositoryJpa sut;

    @Mock
    ContactMeEntity2ContactMeMapper mapper;

    @Mock
    EntityManager em;

    @Mock
    TypedQuery<ContactMeEntity> query;

    @Test
    @DisplayName("Should read all contact me")
    void readAll() {
        //given
        ContactMeEntity contactMeEntity = new ContactMeEntity();
        List<ContactMeEntity> list = List.of(contactMeEntity);
        given(em.createNamedQuery(eq(ContactMeEntity.READ_ALL_BY_STATUS_PENDING), eq(ContactMeEntity.class)))
                .willReturn(query);
        given(query.getResultStream()).willReturn(Stream.of(contactMeEntity));
        ContactMe build = ContactMe.builder().build();
        var contactMeListRes = List.of(build);
        given(mapper.apply(contactMeEntity))
                .willReturn(build);

        //when
        List<ContactMe> result = assertDoesNotThrow(() -> sut.readAllByStatusPending());

        //then
        assertFalse(result.isEmpty());
        assertEquals(contactMeListRes, result);
        assertEquals(list.size(), result.size());
    }
}