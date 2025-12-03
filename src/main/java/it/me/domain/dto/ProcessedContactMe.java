package it.me.domain.dto;

import it.me.entity.ContactMe;
import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record ProcessedContactMe(
        Long id,
        String email,
        String name,
        String message,
        Boolean contactBack,
        ContactMe.Status status,
        Integer attempts,
        ZonedDateTime lastAttemptAt,
        String errorReason,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt
) {
    /**
     * A partire dal record di accesso, e possibile passare
     * al builder con i valori del record iniziale gia compilati
     * e aggiungerne di nuovi
     */
    public ProcessedContactMeBuilder improve() {
        return ProcessedContactMe.builder()
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
