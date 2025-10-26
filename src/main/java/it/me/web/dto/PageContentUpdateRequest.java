package it.me.web.dto;

import jakarta.validation.constraints.Size;

public record PageContentUpdateRequest(
        @Size(max = 120)
        String title,
        @Size(max = 240)
        String subtitle,
        String body) {
}
