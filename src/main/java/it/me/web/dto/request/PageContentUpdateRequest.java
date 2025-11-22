package it.me.web.dto.request;

public record PageContentUpdateRequest(
        String title,
        String subtitle,
        String body
) {
}
