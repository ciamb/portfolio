package it.me.domain.service.page.content;

import it.me.repository.entity.PageContentEntity;
import it.me.repository.page.content.PageContentReadBySlugRepository;
import it.me.web.dto.request.PageContentUpdateRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.jboss.logging.Logger;

import java.time.ZonedDateTime;

@ApplicationScoped
public class PageContentUpdateBySlugService {
    private static final Logger logger = Logger.getLogger(PageContentUpdateBySlugService.class.getName());

    @Inject
    PageContentReadBySlugRepository pageContentReadBySlugRepository;

    @Transactional
    public PageContentEntity updatePageContentBySlug(
            String slug, PageContentUpdateRequest pageContentUpdateRequest) {
        var pageContent = pageContentReadBySlugRepository.readBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Page not found with slug: %s".formatted(slug)));

        logger.infof("Admin update slug=%s", slug);

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
