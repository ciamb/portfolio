package it.me.domain.service.assistant.rule.cache;

import it.me.domain.dto.AssistantRule;
import it.me.repository.entity.AssistantRuleEntity;
import java.util.List;
import java.util.concurrent.CompletionStage;

public interface AssistantRuleCacheService {
    CompletionStage<List<AssistantRule>> getAssistantRuleByRuleType(AssistantRuleEntity.RuleType ruleType);
}
