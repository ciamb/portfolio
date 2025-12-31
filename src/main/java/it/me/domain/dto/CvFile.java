package it.me.domain.dto;

import java.time.ZonedDateTime;
import lombok.Builder;

@Builder
public record CvFile(
        Long id,
        String filename,
        String contentType,
        byte[] fileData,
        Long filesizeBytes,
        String sha256,
        boolean isActive,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt) {
    public CvFileBuilder toBuilder() {
        return CvFile.builder()
                .id(this.id)
                .filename(this.filename)
                .contentType(this.contentType)
                .fileData(this.fileData)
                .filesizeBytes(this.filesizeBytes)
                .sha256(this.sha256)
                .isActive(this.isActive)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt);
    }
}
