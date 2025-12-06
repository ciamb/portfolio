package it.me.repository.entity;

import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table(
        name = "page_content",
        uniqueConstraints = @UniqueConstraint(
                name = "unique_page_content_slug",
                columnNames = "slug"
        )
)
@NamedQuery(
        name = PageContentEntity.READ_BY_SLUG,
        query = " select pce from PageContentEntity pce " +
                " where pce.slug = :slug "
)
public class PageContentEntity {
    public static final String READ_BY_SLUG = "PageContent.readBySlug";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String slug; //es "home"

    @Column(nullable = false, length = 120)
    private String title;

    @Column(length = 240)
    private String subtitle;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    public Long id() {
        return id;
    }

    public PageContentEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String slug() {
        return slug;
    }

    public PageContentEntity setSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public String title() {
        return title;
    }

    public PageContentEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String subtitle() {
        return subtitle;
    }

    public PageContentEntity setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public String body() {
        return body;
    }

    public PageContentEntity setBody(String body) {
        this.body = body;
        return this;
    }

    public ZonedDateTime updatedAt() {
        return updatedAt;
    }

    public PageContentEntity setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
