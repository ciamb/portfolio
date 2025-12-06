package it.me.domain.dto;

import it.me.repository.entity.ContactMeEntity;

import java.time.ZonedDateTime;

public record ContactMe(
        Long id,
        String email,
        String name,
        String message,
        Boolean contactBack,
        ContactMeEntity.Status status,
        Integer attempts,
        ZonedDateTime lastAttemptAt,
        String errorReason,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt
) {
}
