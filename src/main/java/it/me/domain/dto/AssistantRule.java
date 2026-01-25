package it.me.domain.dto;

import it.me.repository.entity.AssistantRuleEntity;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Generated;

@Builder
@Generated
public record AssistantRule(
        Long id,
        AssistantProfile assistantProfile,
        AssistantRuleEntity.RuleType ruleType,
        String keyword,
        Integer priority,
        Boolean enabled,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {}
