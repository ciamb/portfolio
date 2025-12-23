package it.me.domain.dto;

import java.time.ZonedDateTime;
import java.util.List;

public record ProcessedInfo(
        ZonedDateTime runAt,
        int processed,
        int withError,
        List<ContactMe> processedContactMe
) {
}
