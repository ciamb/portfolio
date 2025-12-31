package it.me.domain.dto;

import java.time.ZonedDateTime;
import lombok.Builder;

@Builder
public record ContactMeBatchLog(Long id, ZonedDateTime runAt, Integer processed, Integer withError, String sentTo) {}
