package it.me.repository.contact.me.batch.config.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import it.me.domain.dto.ContactMeBatchConfig;
import it.me.repository.entity.ContactMeBatchConfigEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContactMeBatchConfig2ContactMeBatchConfigEntityMapperTest {

    @InjectMocks
    private ContactMeBatchConfig2ContactMeBatchConfigEntityMapper sut;

    @Mock
    ContactMeBatchConfig contactMeBatchConfig;

    @Test
    void shouldApply() {
        // given
        given(contactMeBatchConfig.id()).willReturn(1);

        // when
        ContactMeBatchConfigEntity entity = sut.apply(contactMeBatchConfig);

        // then
        assertEquals(1, entity.id());
    }

    @Test
    void shouldReturnNull() {
        // given
        // when
        ContactMeBatchConfigEntity entity = sut.apply(null);
        // then
        assertNull(entity);
    }
}
