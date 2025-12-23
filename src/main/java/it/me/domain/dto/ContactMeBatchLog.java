package it.me.domain.dto;

import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record ContactMeBatchLog(
        Long id,
        ZonedDateTime runAt,
        Integer processed,
        Integer withError,
        String sentTo
) {
}
