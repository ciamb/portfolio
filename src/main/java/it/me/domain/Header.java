package it.me.domain;

public enum Header {
    X_API_KEY("x-api-key"),
    X_REQUEST_ID("x-request-id");

    private final String value;

    Header(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
