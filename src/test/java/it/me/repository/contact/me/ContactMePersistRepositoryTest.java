package it.me.repository.contact.me;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;

import it.me.domain.dto.ContactMe;
import it.me.repository.contact.me.mapper.ContactMe2ContactMeEntityMapper;
import it.me.repository.entity.ContactMeEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContactMePersistRepositoryTest {

    @InjectMocks
    private ContactMePersistRepositoryJpa sut;

    @Mock
    ContactMe2ContactMeEntityMapper contactMe2ContactMeEntityMapper;

    @Mock
    EntityManager em;

    @Test
    @DisplayName("1. Should call persist ")
    void shouldCallFlushRefresh() {
        // given
        var contactMe = ContactMe.builder().build();
        var contactMeEntity = new ContactMeEntity();
        given(contactMe2ContactMeEntityMapper.apply(contactMe)).willReturn(contactMeEntity);

        // when
        ContactMe result = sut.persist(contactMe);

        // then
        assertSame(contactMe, result);
        var inOrder = inOrder(contactMe2ContactMeEntityMapper, em);
        inOrder.verify(contactMe2ContactMeEntityMapper).apply(contactMe);
        inOrder.verify(em).persist(contactMeEntity);
        inOrder.verifyNoMoreInteractions();
    }
}
