package it.me.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CvFileUploadRequest(
        @NotNull(message = "fileData is null")
        byte[] fileData,
        @NotNull(message = "filename is null")
        @NotBlank(message = "filename is blank")
        String filename,
        String contentType
) {
}
