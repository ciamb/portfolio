package it.me.domain.dto;

import it.me.repository.entity.AssistantRuleEntity;

import java.time.OffsetDateTime;

import lombok.Builder;
import lombok.Generated;
import org.jetbrains.annotations.NotNull;

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
        OffsetDateTime updatedAt) {
    @Override
    public @NotNull String toString() {
        String assistantProfileName = (assistantProfile == null) ?
                "null" : String.valueOf(assistantProfile.name());
        return "AssistantRule{" +
                "id=" + id +
                ", assistantProfile=" + assistantProfileName +
                ", ruleType=" + ruleType +
                ", keyword='" + keyword + '\'' +
                ", priority=" + priority +
                ", enabled=" + enabled +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
