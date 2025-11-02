package it.me.repository;

import it.me.entity.PageContent;
import it.me.web.dto.PageContentUpdateRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import org.jboss.logging.Logger;

@ApplicationScoped
public class PageContentUpdateRepository {
    private static final Logger LOG = Logger.getLogger(PageContentUpdateRepository.class.getName());

    @Inject
    PageContentReadBySlugRepository pageContentReadBySlugRepository;

    @Transactional
    public PageContent updateBySlug(String slug, PageContentUpdateRequest pageContentUpdateRequest) {
        var pageContent = pageContentReadBySlugRepository.readBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Page not found with slug: %s".formatted(slug)));

        LOG.infof("Admin update slug=%s", slug);

        if (pageContentUpdateRequest.title() != null
                && !pageContentUpdateRequest.title().isBlank()) {
            pageContent.setTitle(pageContentUpdateRequest.title());
        }

        if (pageContentUpdateRequest.subtitle() != null
                && !pageContentUpdateRequest.subtitle().isBlank()) {
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
