package it.me.domain;

import it.me.entity.PageContent;
import it.me.repository.PageContentUpdateBySlugRepository;
import it.me.web.dto.PageContentUpdateRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;

@ApplicationScoped
public class PageContentUpdateBySlugService {

    @Inject
    PageContentUpdateBySlugRepository pageContentUpdateBySlugRepository;

    public PageContent updatePageContentBySlug(
            String slug, PageContentUpdateRequest pageContentUpdateRequest) {
        return pageContentUpdateBySlugRepository.updateBySlug(slug, pageContentUpdateRequest);
    }
}
