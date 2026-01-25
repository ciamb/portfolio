package it.me.domain.repository.assistant.rule;

import it.me.domain.dto.AssistantRule;
import it.me.repository.entity.AssistantRuleEntity;
import java.util.List;

public interface ReadAssistantRuleByRuleTypeRepository {
    List<AssistantRule> readByRuleType(AssistantRuleEntity.RuleType ruleType);
}
