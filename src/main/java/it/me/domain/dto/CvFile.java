package it.me.domain.dto;

import lombok.Builder;

import java.time.ZonedDateTime;

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
        ZonedDateTime updatedAt
) {
    CvFileBuilder buildFromThis() {
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
