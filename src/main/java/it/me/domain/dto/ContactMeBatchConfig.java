package it.me.domain.dto;

public record ContactMeBatchConfig(
        Integer id,
        Boolean isActive,
        String targetEmail
) {
}
