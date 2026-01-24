package it.me.repository.contact.me.batch.config.mapper;

import static org.junit.jupiter.api.Assertions.*;

import it.me.domain.dto.ContactMeBatchConfig;
import it.me.repository.entity.ContactMeBatchConfigEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContactMeBatchConfigEntity2ContactMeBatchConfigMapperTest {

    @InjectMocks
    private ContactMeBatchConfigEntity2ContactMeBatchConfigMapper sut;

    @Test
    void shouldApply() {
        // given
        ContactMeBatchConfigEntity entity = new ContactMeBatchConfigEntity();
        entity.setId(1);

        // when
        ContactMeBatchConfig apply = sut.apply(entity);
        // then
        assertEquals(1, apply.id());
    }

    @Test
    void shouldReturnNull() {
        // given

        // when
        ContactMeBatchConfig apply = sut.apply(null);
        // then
        assertNull(apply);
    }
}
