package it.me.web.dto.response;

import it.me.repository.entity.ContactMeEntity;

public record ContactMeResponse(Long id, ContactMeEntity.Status status, String message) {}
