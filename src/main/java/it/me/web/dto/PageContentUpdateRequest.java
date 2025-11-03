package it.me.web.dto;

public record PageContentUpdateRequest(
        String title,
        String subtitle,
        String body
) {
}
