package it.me.web.dto.request;

import jakarta.validation.constraints.NotNull;

public record ContactMeBatchConfigUpdateRequest(
        @NotNull(message = "Please specify the configuration")
        Boolean isActive
) {
}
