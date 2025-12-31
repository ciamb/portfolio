package it.me.web.dto.request;

import jakarta.validation.constraints.Size;

public record PageContentUpdateRequest(
        @Size(max = 120, message = "title is longer than 120 characters") String title,

        @Size(max = 240, message = "subtitle is longer than 240 characters") String subtitle,

        String body) {}
