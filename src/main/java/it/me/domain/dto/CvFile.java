package it.me.domain.dto;

import java.time.ZonedDateTime;

public record CvFile(
        Long id,
        String filename,
        String contentType,
        byte[] fileData,
        Long filesizeBytes,
        String sha256,
        boolean isActive,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt
) {
}
