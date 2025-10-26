package it.me.domain;

public enum Page {
    HOME("home");

    private final String slug;

    Page(String slug) {
        this.slug = slug;
    }

    public String getSlug() {
        return slug;
    }

}
