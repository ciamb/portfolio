package it.me.domain.dto;

import java.time.ZonedDateTime;
import lombok.Builder;

@Builder
public record PageContent(Long id, String slug, String title, String subtitle, String body, ZonedDateTime updatedAt) {
    public PageContentBuilder builderFromThis() {
        return PageContent.builder()
                .id(this.id)
                .slug(this.slug)
                .title(this.title)
                .subtitle(this.subtitle)
                .body(this.body)
                .updatedAt(this.updatedAt);
    }
}
