package it.me.domain.repository.page.content;

import it.me.domain.dto.PageContent;

public interface PageContentUpdateBySlugRepository {
    PageContent updatePageContentBySlug(PageContent pageContent);
}
