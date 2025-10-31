package it.me.web.error;

public record ErrorResponse(
        String error,
        String message,
        String path,
        String requestId
) {

}
