package it.me.domain.service.assistant.rule.cache;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import it.me.domain.PortfolioPublicK;
import it.me.domain.dto.AssistantRule;
import it.me.domain.dto.CachedAssistantRuleByRuleType;
import it.me.domain.repository.assistant.rule.ReadAssistantRuleByRuleTypeRepository;
import it.me.repository.entity.AssistantRuleEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.jboss.logging.Logger;

/**
 * <p>implementazione di {@link AssistantRuleCacheService} che legge dal db i valori di
 * {@link AssistantRuleEntity} e li salva in una cache in-memory per 10 minuti (costante salvata in {@link PortfolioPublicK})</p>
 * <p>Quando e presente una cache valida, il metodo ritorna direttamente la lista salvata nella cache, altrimenti]
 * procede alla lettura dal db delle regole e le salva di nuovo nella cache</p>
 *
 * @author Ciamb
 * @since 1.3.5
 */
@ApplicationScoped
public class AssistantRuleCacheServiceImpl implements AssistantRuleCacheService {
    private static final Logger log = Logger.getLogger(AssistantRuleCacheServiceImpl.class.getName());

    private final Map<AssistantRuleEntity.RuleType, CachedAssistantRuleByRuleType> assistantRuleByRuleTypeCache =
            new ConcurrentHashMap<>();

    @Inject
    ManagedExecutor executor;

    @Inject
    ReadAssistantRuleByRuleTypeRepository readAssistantRuleByRuleTypeRepository;

    @Override
    public CompletionStage<List<AssistantRule>> getAssistantRuleByRuleType(AssistantRuleEntity.RuleType ruleType) {
        CachedAssistantRuleByRuleType entry = assistantRuleByRuleTypeCache.get(ruleType);
        log.infof("loaded cached entry -> %s", entry);

        long now = System.currentTimeMillis();
        boolean isCacheValid = entry != null && now - entry.loadedAt() < PortfolioPublicK.CacheTTL.TEN_MINUTE;

        if (isCacheValid) {
            return CompletableFuture.completedStage(entry.assistantRules());
        }

        return readAssistantRulesAsync(ruleType, entry);
    }

    private CompletionStage<List<AssistantRule>> readAssistantRulesAsync(
            AssistantRuleEntity.RuleType ruleType, CachedAssistantRuleByRuleType previousCache) {
        return supplyAsync(() -> readAssistantRuleByRuleTypeRepository.readByRuleType(ruleType), executor)
                .thenApply(assistantRules -> {
                    log.infof("update cached assistantRules %s", ruleType);
                    assistantRuleByRuleTypeCache.put(
                            ruleType,
                            CachedAssistantRuleByRuleType.builder()
                                    .assistantRules(List.copyOf(assistantRules))
                                    .loadedAt(System.currentTimeMillis())
                                    .build());
                    return assistantRules;
                })
                .exceptionally(ex -> {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    log.errorf(
                            "Error while loading Assistent Rule %s (%s): %s",
                            ruleType, cause.getClass().getSimpleName(), cause.getMessage());
                    return previousCache != null ? previousCache.assistantRules() : List.of();
                });
    }
}
