package it.me.domain.service.page.content;

import it.me.domain.dto.PageContent;
import it.me.domain.repository.page.content.PageContentReadBySlugRepository;
import it.me.domain.repository.page.content.PageContentUpdateBySlugRepository;
import it.me.web.dto.request.PageContentUpdateRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import java.time.ZonedDateTime;
import org.jboss.logging.Logger;

@ApplicationScoped
public class PageContentUpdateBySlugService {
    private static final Logger logger = Logger.getLogger(PageContentUpdateBySlugService.class.getName());

    @Inject
    PageContentReadBySlugRepository pageContentReadBySlugRepository;

    @Inject
    PageContentUpdateBySlugRepository pageContentUpdateBySlugRepository;

    public PageContent updatePageContentBySlug(String slug, PageContentUpdateRequest pageContentUpdateRequest) {
        PageContent pageContent = pageContentReadBySlugRepository
                .readBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Page not found with slug: %s".formatted(slug)));
        logger.infof("found slug=%s, updating with new infos", slug);

        if (pageContentUpdateRequest.title() != null
                && !pageContentUpdateRequest.title().isBlank()) {
            pageContent = pageContent
                    .builderFromThis()
                    .title(pageContentUpdateRequest.title())
                    .build();
        }

        if (pageContentUpdateRequest.subtitle() != null
                && !pageContentUpdateRequest.subtitle().isBlank()) {
            pageContent = pageContent
                    .builderFromThis()
                    .subtitle(pageContentUpdateRequest.subtitle())
                    .build();
        }

        if (pageContentUpdateRequest.body() != null
                && !pageContentUpdateRequest.body().isBlank()) {
            pageContent = pageContent
                    .builderFromThis()
                    .body(pageContentUpdateRequest.body())
                    .build();
        }

        pageContent =
                pageContent.builderFromThis().updatedAt(ZonedDateTime.now()).build();

        pageContentUpdateBySlugRepository.updatePageContentBySlug(pageContent);

        return pageContent;
    }
}
