package it.me.domain.dto;

import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Generated;

@Builder
@Generated
public record AssistantProfile(
        Long id,
        String name,
        String systemPrompt,
        String fallbackMessage,
        Boolean enabled,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {}
