package it.me.web.dto.response;

public record ContactMeBatchResponse(
        Boolean configActive, Long logId, Integer processed, Integer withError, String sentTo, Boolean mailSuccess) {}
