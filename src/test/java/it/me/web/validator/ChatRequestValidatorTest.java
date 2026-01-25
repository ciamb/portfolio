package it.me.web.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

import it.me.domain.dto.AssistantRule;
import it.me.domain.repository.assistant.rule.ReadAssistantRuleByRuleTypeRepository;
import it.me.repository.entity.AssistantRuleEntity;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import org.assertj.core.api.ObjectAssert;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.junit.jupiter.api.BeforeEach;
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

    private ManagedExecutor executor;

    @Mock
    ReadAssistantRuleByRuleTypeRepository readAssistantRuleByRuleTypeRepository;

    @BeforeEach
    void setUp() {
        executor =
                ManagedExecutor.builder().maxAsync(1).maxQueued(10).propagated().build();
        sut.executor = executor;
    }

    @Test
    void returnFalseInScope() {
        // given
        AssistantRule rule1 = AssistantRule.builder().keyword("inscope").build();
        AssistantRule rule2 = AssistantRule.builder().keyword("outofscope").build();
        given(readAssistantRuleByRuleTypeRepository.readByRuleType(eq(AssistantRuleEntity.RuleType.OUT_OF_SCOPE)))
                .willReturn(List.of(rule2));
        given(readAssistantRuleByRuleTypeRepository.readByRuleType(eq(AssistantRuleEntity.RuleType.IN_SCOPE)))
                .willReturn(List.of(rule1));
        // when
        CompletionStage<Boolean> res = sut.isMessageOutOfScope("inscope");
        // then
        ObjectAssert<Boolean> out = assertThat(res.toCompletableFuture()).succeedsWithin(1, TimeUnit.SECONDS);
        assertFalse(out.actual());
    }

    @Test
    void returnTrueOutOfScope() {
        AssistantRule rule1 = AssistantRule.builder().keyword("outscope").build();
        given(readAssistantRuleByRuleTypeRepository.readByRuleType(eq(AssistantRuleEntity.RuleType.OUT_OF_SCOPE)))
                .willReturn(List.of(rule1));

        // when
        CompletionStage<Boolean> result = sut.isMessageOutOfScope("outscope");
        // then
        ObjectAssert<Boolean> out = assertThat(result.toCompletableFuture()).succeedsWithin(1, TimeUnit.SECONDS);
        assertTrue(out.actual());

        var inOrder = Mockito.inOrder(readAssistantRuleByRuleTypeRepository);
        inOrder.verify(readAssistantRuleByRuleTypeRepository, times(1))
                .readByRuleType(eq(AssistantRuleEntity.RuleType.OUT_OF_SCOPE));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void returnTrueByDefault() {
        AssistantRule rule1 = AssistantRule.builder().keyword("inscope").build();
        AssistantRule rule2 = AssistantRule.builder().keyword("outscope").build();
        given(readAssistantRuleByRuleTypeRepository.readByRuleType(eq(AssistantRuleEntity.RuleType.OUT_OF_SCOPE)))
                .willReturn(List.of(rule2));
        given(readAssistantRuleByRuleTypeRepository.readByRuleType(eq(AssistantRuleEntity.RuleType.IN_SCOPE)))
                .willReturn(List.of(rule1));
        // when
        CompletionStage<Boolean> res = sut.isMessageOutOfScope("noscope");
        // then
        ObjectAssert<Boolean> out = assertThat(res).succeedsWithin(1, TimeUnit.SECONDS);
        assertTrue(out.actual());

        var inOrder = Mockito.inOrder(readAssistantRuleByRuleTypeRepository);
        inOrder.verify(readAssistantRuleByRuleTypeRepository, times(1))
                .readByRuleType(eq(AssistantRuleEntity.RuleType.OUT_OF_SCOPE));
        inOrder.verify(readAssistantRuleByRuleTypeRepository, times(1))
                .readByRuleType(eq(AssistantRuleEntity.RuleType.IN_SCOPE));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void returnTrue_whenNullMessage() {
        // given
        // when
        CompletionStage<Boolean> res = sut.isMessageOutOfScope(null);
        // then
        ObjectAssert<Boolean> out = assertThat(res).succeedsWithin(1, TimeUnit.SECONDS);
        assertTrue(out.actual());
    }
}
