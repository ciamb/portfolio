package it.me.domain.dto;

import it.me.repository.entity.ContactMeEntity;
import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
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
    public ContactMeBuilder builderFromThis() {
        return ContactMe.builder()
                .id(this.id)
                .email(this.email)
                .name(this.name)
                .message(this.message)
                .contactBack(this.contactBack)
                .status(this.status)
                .attempts(this.attempts)
                .lastAttemptAt(this.lastAttemptAt)
                .errorReason(this.errorReason)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt);
    }
}
