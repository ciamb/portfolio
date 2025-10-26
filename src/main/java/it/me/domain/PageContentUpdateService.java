package it.me.domain;

import it.me.entity.PageContent;
import it.me.repository.PageContentUpdateRepository;
import it.me.web.dto.PageContentUpdateRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PageContentUpdateService {

    @Inject
    PageContentUpdateRepository pageContentUpdateRepository;

    public PageContent updatePageContentBySlug(
            String slug, PageContentUpdateRequest pageContentUpdateRequest) {
        return pageContentUpdateRepository.updateBySlug(slug, pageContentUpdateRequest);
    }
}
