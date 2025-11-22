package it.me.web.dto.request;

public record CvFileUploadRequest(
        byte[] fileData,
        String filename,
        String contentType
) {
}
