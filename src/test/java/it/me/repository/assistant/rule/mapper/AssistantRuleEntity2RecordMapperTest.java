package it.me.repository.assistant.rule.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import it.me.domain.dto.AssistantProfile;
import it.me.domain.dto.AssistantRule;
import it.me.repository.assistant.profile.mapper.AssistantProfileEntity2RecordMapper;
import it.me.repository.entity.AssistantProfileEntity;
import it.me.repository.entity.AssistantRuleEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AssistantRuleEntity2RecordMapperTest {

    @InjectMocks
    private AssistantRuleEntity2RecordMapper sut;

    @Mock
    AssistantProfileEntity2RecordMapper profileMapper;

    @Test
    void shouldReturnNull() {
        // given
        // when
        // then
        assertNull(sut.apply(null));
    }

    @Test
    void shouldApply() {
        // given
        AssistantRuleEntity toMap = new AssistantRuleEntity();
        toMap.setId(1L);

        AssistantProfileEntity assistantProfileEntity = new AssistantProfileEntity();
        assistantProfileEntity.setId(1L);

        toMap.setAssistantProfile(assistantProfileEntity);

        AssistantProfile mockProfile = AssistantProfile.builder().id(1L).build();

        given(profileMapper.apply(eq(toMap.getAssistantProfile()))).willReturn(mockProfile);

        // when
        AssistantRule apply = sut.apply(toMap);

        // then
        assertNotNull(apply);
        assertEquals(toMap.getId(), apply.id());
        assertEquals(toMap.getAssistantProfile().getId(), mockProfile.id());
    }
}
