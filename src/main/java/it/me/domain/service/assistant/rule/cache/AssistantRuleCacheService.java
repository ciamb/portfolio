package it.me.domain.service.assistant.rule.cache;

import it.me.domain.dto.AssistantRule;
import it.me.repository.entity.AssistantRuleEntity;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 *
 * <p>Servizio responsabile del recupero di {@link AssistantRule} tramite {@link AssistantRuleEntity.RuleType} specificato
 * in modalita asincrona</p>
 *
 * @since 1.3.5
 */
public interface AssistantRuleCacheService {

    /**
     * Recupera le regole dell'assistente specificate dal {@link AssistantRuleEntity.RuleType}
     *
     * @param ruleType il ruleType specificato (non deve essere {@code null})
     * @return una {@link CompletionStage} che si completa con una lista di {@link AssistantRule};
     * potrebbe anche completarsi con eccezione in caso il db vada in errore
     */
    CompletionStage<List<AssistantRule>> getAssistantRuleByRuleType(AssistantRuleEntity.RuleType ruleType);
}
