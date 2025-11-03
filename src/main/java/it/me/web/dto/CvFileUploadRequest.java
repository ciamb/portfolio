package it.me.web.dto;

public record CvFileUploadRequest(
        byte[] fileData,
        String filename,
        String contentType
) {
}
