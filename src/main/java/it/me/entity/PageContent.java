package it.me.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "page_content",
        uniqueConstraints = @UniqueConstraint(
                name = "unique_page_content_slug",
                columnNames = "slug"
        )
)
@NamedQuery(
        name = PageContent.READ_BY_SLUG,
        query = " select pc from PageContent pc " +
                " where pc.slug = :slug "
)
public class PageContent {
    public static final String READ_BY_SLUG = "PageContent.readBySlug";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Autoincrement with sqlite
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
    private LocalDateTime updatedAt;

    public Long id() {
        return id;
    }

    public PageContent setId(Long id) {
        this.id = id;
        return this;
    }

    public String slug() {
        return slug;
    }

    public PageContent setSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public String title() {
        return title;
    }

    public PageContent setTitle(String title) {
        this.title = title;
        return this;
    }

    public String subtitle() {
        return subtitle;
    }

    public PageContent setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public String body() {
        return body;
    }

    public PageContent setBody(String body) {
        this.body = body;
        return this;
    }

    public LocalDateTime updatedAt() {
        return updatedAt;
    }

    public PageContent setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
