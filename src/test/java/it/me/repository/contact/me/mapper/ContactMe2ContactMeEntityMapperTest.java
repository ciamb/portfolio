package it.me.repository.contact.me.mapper;

import static org.junit.jupiter.api.Assertions.*;

import it.me.domain.dto.ContactMe;
import it.me.repository.entity.ContactMeEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContactMe2ContactMeEntityMapperTest {

    @InjectMocks
    private ContactMe2ContactMeEntityMapper sut;

    @Test
    void shouldReturnNull() {
        // given
        // when
        ContactMeEntity res = sut.apply(null);
        // then
        assertNull(res);
    }

    @Test
    void shouldApply() {
        // given
        ContactMe cm = ContactMe.builder().id(1L).build();
        // when
        ContactMeEntity res = sut.apply(cm);
        // then
        assertNotNull(res);
        assertEquals(cm.id(), res.id());
    }
}
