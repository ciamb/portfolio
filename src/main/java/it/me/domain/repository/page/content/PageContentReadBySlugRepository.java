package it.me.domain.repository.page.content;

import it.me.domain.dto.PageContent;

import java.util.Optional;

public interface PageContentReadBySlugRepository {
    Optional<PageContent> readBySlug(String slug);
}
