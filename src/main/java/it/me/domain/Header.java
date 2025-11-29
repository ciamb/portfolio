package it.me.domain;

public enum Header {
    C_API_KEY("c-api-key"),
    C_REQUEST_ID("c-request-id");

    private final String value;

    Header(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
