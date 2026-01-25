package it.me.web.validator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

import it.me.domain.dto.AssistantRule;
import it.me.domain.repository.assistant.rule.ReadAssistantRuleByRuleTypeRepository;
import it.me.repository.entity.AssistantRuleEntity;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChatRequestValidatorTest {

    @InjectMocks
    private ChatRequestValidator sut;

    @Mock
    ReadAssistantRuleByRuleTypeRepository readAssistantRuleByRuleTypeRepository;

    @Test
    void shouldBeInScope() {
        // given
        AssistantRule rule1 = AssistantRule.builder().keyword("inscope").build();
        given(readAssistantRuleByRuleTypeRepository.readByRuleType(eq(AssistantRuleEntity.RuleType.IN_SCOPE)))
                .willReturn(List.of(rule1));
        // when
        boolean res = sut.isMessageOutOfScope("inscope");
        // then
        assertFalse(res);
    }

    @Test
    void shouldBeOutOfScope() {
        AssistantRule rule1 = AssistantRule.builder().keyword("inscope").build();
        AssistantRule rule2 = AssistantRule.builder().keyword("outscope").build();
        given(readAssistantRuleByRuleTypeRepository.readByRuleType(eq(AssistantRuleEntity.RuleType.IN_SCOPE)))
                .willReturn(List.of(rule1));
        given(readAssistantRuleByRuleTypeRepository.readByRuleType(eq(AssistantRuleEntity.RuleType.OUT_OF_SCOPE)))
                .willReturn(List.of(rule2));
        // when
        boolean res = sut.isMessageOutOfScope("outscope");
        // then
        assertTrue(res);
        var inOrder = Mockito.inOrder(readAssistantRuleByRuleTypeRepository);
        inOrder.verify(readAssistantRuleByRuleTypeRepository, times(1))
                .readByRuleType(eq(AssistantRuleEntity.RuleType.IN_SCOPE));
        inOrder.verify(readAssistantRuleByRuleTypeRepository, times(1))
                .readByRuleType(eq(AssistantRuleEntity.RuleType.OUT_OF_SCOPE));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void defaultScope() {
        AssistantRule rule1 = AssistantRule.builder().keyword("inscope").build();
        AssistantRule rule2 = AssistantRule.builder().keyword("outscope").build();
        given(readAssistantRuleByRuleTypeRepository.readByRuleType(eq(AssistantRuleEntity.RuleType.IN_SCOPE)))
                .willReturn(List.of(rule1));
        given(readAssistantRuleByRuleTypeRepository.readByRuleType(eq(AssistantRuleEntity.RuleType.OUT_OF_SCOPE)))
                .willReturn(List.of(rule2));
        // when
        boolean res = sut.isMessageOutOfScope("noscope");
        // then
        assertTrue(res);
        var inOrder = Mockito.inOrder(readAssistantRuleByRuleTypeRepository);
        inOrder.verify(readAssistantRuleByRuleTypeRepository, times(1))
                .readByRuleType(eq(AssistantRuleEntity.RuleType.IN_SCOPE));
        inOrder.verify(readAssistantRuleByRuleTypeRepository, times(1))
                .readByRuleType(eq(AssistantRuleEntity.RuleType.OUT_OF_SCOPE));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void returnTrue_whenNullMessage() {
        // given
        // when
        boolean res = sut.isMessageOutOfScope(null);
        // then
        assertTrue(res);
    }
}
