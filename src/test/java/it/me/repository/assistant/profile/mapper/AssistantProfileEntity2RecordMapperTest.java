package it.me.repository.assistant.profile.mapper;

import static org.junit.jupiter.api.Assertions.*;

import it.me.domain.dto.AssistantProfile;
import it.me.repository.entity.AssistantProfileEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AssistantProfileEntity2RecordMapperTest {

    @InjectMocks
    private AssistantProfileEntity2RecordMapper sut;

    @Test
    void shouldApply() {
        // given
        AssistantProfileEntity entity = new AssistantProfileEntity();
        entity.setId(1L);
        // when
        AssistantProfile apply = sut.apply(entity);
        // then
        assertNotNull(apply);
        assertEquals(entity.getId(), apply.id());
    }

    @Test
    void shouldReturnNull() {
        // given
        // when
        AssistantProfile apply = sut.apply(null);
        // then
        assertNull(apply);
    }
}
