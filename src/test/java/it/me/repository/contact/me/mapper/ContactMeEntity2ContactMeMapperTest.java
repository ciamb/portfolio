package it.me.repository.contact.me.mapper;

import static org.junit.jupiter.api.Assertions.*;

import it.me.domain.dto.ContactMe;
import it.me.repository.entity.ContactMeEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContactMeEntity2ContactMeMapperTest {

    @InjectMocks
    private ContactMeEntity2ContactMeMapper sut;

    @Test
    void shouldApply() {
        // given
        ContactMeEntity cme = new ContactMeEntity();
        cme.setId(1L);
        // when
        ContactMe res = sut.apply(cme);

        // then
        assertNotNull(res);
        assertEquals(cme.id(), res.id());
    }

    @Test
    void shouldReturnNull() {
        // given
        // when
        // then
        assertNull(sut.apply(null));
    }
}
