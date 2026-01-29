package it.me.domain.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record CachedAssistantRuleByRuleType(List<AssistantRule> assistantRules, long loadedAt) {}
