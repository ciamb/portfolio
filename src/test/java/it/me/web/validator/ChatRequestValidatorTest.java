package it.me.web.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

import it.me.domain.dto.AssistantRule;
import it.me.domain.service.assistant.rule.cache.AssistantRuleCacheService;
import it.me.repository.entity.AssistantRuleEntity;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import org.assertj.core.api.ObjectAssert;
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
    AssistantRuleCacheService assistantRuleCacheService;

    @Test
    void returnFalse_whenOutOfScope() {
        // given
        AssistantRule rule2 = AssistantRule.builder().keyword("outofscope").build();
        given(assistantRuleCacheService.getAssistantRuleByRuleType(eq(AssistantRuleEntity.RuleType.OUT_OF_SCOPE)))
                .willReturn(CompletableFuture.completedStage(List.of(rule2)));

        // when
        CompletionStage<Boolean> res = sut.isUserMessageInScope("outofscope");
        // then
        ObjectAssert<Boolean> out = assertThat(res.toCompletableFuture()).succeedsWithin(1, TimeUnit.SECONDS);
        assertFalse(out.actual());
    }

    @Test
    void returnTrue_whenInScope() {
        AssistantRule rule1 = AssistantRule.builder().keyword("inscope").build();
        AssistantRule rule2 = AssistantRule.builder().keyword("outofscope").build();

        given(assistantRuleCacheService.getAssistantRuleByRuleType(eq(AssistantRuleEntity.RuleType.OUT_OF_SCOPE)))
                .willReturn(CompletableFuture.completedStage(List.of(rule2)));
        given(assistantRuleCacheService.getAssistantRuleByRuleType(eq(AssistantRuleEntity.RuleType.IN_SCOPE)))
                .willReturn(CompletableFuture.completedStage(List.of(rule1)));

        // when
        CompletionStage<Boolean> result = sut.isUserMessageInScope("inscope");

        // then
        ObjectAssert<Boolean> out = assertThat(result.toCompletableFuture()).succeedsWithin(1, TimeUnit.SECONDS);
        assertTrue(out.actual());

        var inOrder = Mockito.inOrder(assistantRuleCacheService);
        inOrder.verify(assistantRuleCacheService, times(1))
                .getAssistantRuleByRuleType(eq(AssistantRuleEntity.RuleType.OUT_OF_SCOPE));
        inOrder.verify(assistantRuleCacheService, times(1))
                .getAssistantRuleByRuleType(eq(AssistantRuleEntity.RuleType.IN_SCOPE));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void returnFalse_whenNoKeywordWasFound() {
        AssistantRule rule1 = AssistantRule.builder().keyword("inscope").build();
        AssistantRule rule2 = AssistantRule.builder().keyword("outofscope").build();

        given(assistantRuleCacheService.getAssistantRuleByRuleType(eq(AssistantRuleEntity.RuleType.OUT_OF_SCOPE)))
                .willReturn(CompletableFuture.completedStage(List.of(rule2)));
        given(assistantRuleCacheService.getAssistantRuleByRuleType(eq(AssistantRuleEntity.RuleType.IN_SCOPE)))
                .willReturn(CompletableFuture.completedStage(List.of(rule1)));

        // when
        CompletionStage<Boolean> result = sut.isUserMessageInScope("none");

        // then
        ObjectAssert<Boolean> out = assertThat(result.toCompletableFuture()).succeedsWithin(1, TimeUnit.SECONDS);
        assertFalse(out.actual());

        var inOrder = Mockito.inOrder(assistantRuleCacheService);
        inOrder.verify(assistantRuleCacheService, times(1))
                .getAssistantRuleByRuleType(eq(AssistantRuleEntity.RuleType.OUT_OF_SCOPE));
        inOrder.verify(assistantRuleCacheService, times(1))
                .getAssistantRuleByRuleType(eq(AssistantRuleEntity.RuleType.IN_SCOPE));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void returnFalse_whenAssistantRuleDoesNotExist() {
        // given
        given(assistantRuleCacheService.getAssistantRuleByRuleType(eq(AssistantRuleEntity.RuleType.OUT_OF_SCOPE)))
                .willReturn(CompletableFuture.completedStage(List.of()));
        given(assistantRuleCacheService.getAssistantRuleByRuleType(eq(AssistantRuleEntity.RuleType.IN_SCOPE)))
                .willReturn(CompletableFuture.completedStage(null));

        // when
        CompletionStage<Boolean> res = sut.isUserMessageInScope("outofscope");
        // then
        ObjectAssert<Boolean> out = assertThat(res.toCompletableFuture()).succeedsWithin(1, TimeUnit.SECONDS);
        assertFalse(out.actual());
    }

    @Test
    void returnTrue_whenNullMessage() {
        // given
        // when
        CompletionStage<Boolean> res = sut.isUserMessageInScope(null);
        // then
        ObjectAssert<Boolean> out = assertThat(res).succeedsWithin(1, TimeUnit.SECONDS);
        assertFalse(out.actual());
    }
}
