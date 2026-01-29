package it.me.domain.service.assistant.rule.cache;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import it.me.domain.PortfolioPublicK;
import it.me.domain.dto.AssistantRule;
import it.me.domain.dto.CachedAssistantRuleByRuleType;
import it.me.domain.repository.assistant.rule.ReadAssistantRuleByRuleTypeRepository;
import it.me.repository.entity.AssistantRuleEntity;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AssistantRuleCacheServiceImplTest {

    @InjectMocks
    private AssistantRuleCacheServiceImpl sut;

    @Mock
    ManagedExecutor executor;

    @Mock
    ReadAssistantRuleByRuleTypeRepository readAssistantRuleByRuleTypeRepository;

    @BeforeEach
    void setUp() {
        doAnswer(inv -> {
                    Runnable runnable = inv.getArgument(0);
                    runnable.run();
                    return null;
                })
                .when(executor)
                .execute(any(Runnable.class));
    }

    @Test
    void whenMissingCache_shouldReadRepository_andSaveCache() {
        // given
        var ruleType = AssistantRuleEntity.RuleType.OUT_OF_SCOPE;
        List<AssistantRule> assistantRules = List.of(Mockito.mock(AssistantRule.class));

        given(readAssistantRuleByRuleTypeRepository.readByRuleType(ruleType)).willReturn(assistantRules);

        // when
        var firstRead =
                sut.getAssistantRuleByRuleType(ruleType).toCompletableFuture().join();
        var secondRead =
                sut.getAssistantRuleByRuleType(ruleType).toCompletableFuture().join();

        // then
        assertEquals(assistantRules, firstRead);
        assertEquals(assistantRules, secondRead);
        assertEquals(firstRead, secondRead);

        verify(readAssistantRuleByRuleTypeRepository, times(1)).readByRuleType(ruleType);
    }

    @Test
    void whenExpiredCache_shouldReadRepository_andSaveCache() throws IllegalAccessException, NoSuchFieldException {
        // given
        var ruleType = AssistantRuleEntity.RuleType.OUT_OF_SCOPE;
        List<AssistantRule> oldAssistantRules = List.of(Mockito.mock(AssistantRule.class));
        List<AssistantRule> newAssistantRules =
                List.of(Mockito.mock(AssistantRule.class), Mockito.mock(AssistantRule.class));

        populateCache(
                ruleType, oldAssistantRules, System.currentTimeMillis() - PortfolioPublicK.CacheTTL.TEN_MINUTE - 1);

        given(readAssistantRuleByRuleTypeRepository.readByRuleType(ruleType)).willReturn(newAssistantRules);
        // when

        List<AssistantRule> result =
                sut.getAssistantRuleByRuleType(ruleType).toCompletableFuture().join();

        // then
        assertEquals(newAssistantRules, result);
        verify(readAssistantRuleByRuleTypeRepository, times(1)).readByRuleType(ruleType);
    }

    // Reflection
    @SuppressWarnings("unchecked")
    private void populateCache(AssistantRuleEntity.RuleType ruleType, List<AssistantRule> assistantRules, long loadedAt)
            throws IllegalAccessException, NoSuchFieldException {
        Field field = AssistantRuleCacheServiceImpl.class.getDeclaredField("assistantRuleByRuleTypeCache");
        field.setAccessible(true);
        Map<AssistantRuleEntity.RuleType, CachedAssistantRuleByRuleType> previousCache =
                (Map<AssistantRuleEntity.RuleType, CachedAssistantRuleByRuleType>) field.get(sut);

        previousCache.put(
                ruleType,
                CachedAssistantRuleByRuleType.builder()
                        .assistantRules(assistantRules)
                        .loadedAt(loadedAt)
                        .build());
    }
}
