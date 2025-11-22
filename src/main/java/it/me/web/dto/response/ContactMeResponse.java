package it.me.web.dto.response;

import it.me.entity.ContactMe;

public record ContactMeResponse(
        Long id,
        ContactMe.Status status,
        String message
) {
}
