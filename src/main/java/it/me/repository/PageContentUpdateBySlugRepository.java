package it.me.repository;

import it.me.entity.PageContent;
import it.me.web.dto.PageContentUpdateRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.time.ZonedDateTime;

import org.jboss.logging.Logger;

@ApplicationScoped
public class PageContentUpdateBySlugRepository {
    private static final Logger LOG = Logger.getLogger(PageContentUpdateBySlugRepository.class.getName());

    @Inject
    PageContentReadBySlugRepository pageContentReadBySlugRepository;

    @Transactional
    public PageContent updateBySlug(String slug, PageContentUpdateRequest pageContentUpdateRequest) {
        var pageContent = pageContentReadBySlugRepository.readBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Page not found with slug: %s".formatted(slug)));

        LOG.infof("Admin update slug=%s", slug);

        if (pageContentUpdateRequest.title() != null
                && !pageContentUpdateRequest.title().isBlank()) {
            if (pageContentUpdateRequest.title().length() > 120) {
                throw new IllegalArgumentException("title is longer than 120 characters");
            }
            pageContent.setTitle(pageContentUpdateRequest.title());
        }

        if (pageContentUpdateRequest.subtitle() != null
                && !pageContentUpdateRequest.subtitle().isBlank()) {
            if (pageContentUpdateRequest.subtitle().length() > 240) {
                throw new IllegalArgumentException("subtitle is longer than 240 characters");
            }
            pageContent.setSubtitle(pageContentUpdateRequest.subtitle());
        }

        if (pageContentUpdateRequest.body() != null
                && !pageContentUpdateRequest.body().isBlank()) {
            pageContent.setBody(pageContentUpdateRequest.body());
        }

        pageContent.setUpdatedAt(ZonedDateTime.now());
        return pageContent;
    }
}
