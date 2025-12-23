package it.me.repository.page.content;

import it.me.domain.dto.PageContent;
import it.me.domain.repository.page.content.PageContentUpdateBySlugRepository;
import it.me.repository.entity.PageContentEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class PageContentUpdateBySlugRepositoryJpa implements PageContentUpdateBySlugRepository {
    @Inject
    EntityManager em;

    @Transactional
    @Override
    public PageContent updatePageContentBySlug(PageContent pageContent) {
        PageContentEntity pageContentEntity = em
                .createNamedQuery(
                        PageContentEntity.READ_BY_SLUG, PageContentEntity.class)
                .setParameter("slug", pageContent.slug())
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException(
                        "Page not found with slug: %s".formatted(pageContent.slug())));

        pageContentEntity.setTitle(pageContent.title());
        pageContentEntity.setSubtitle(pageContent.subtitle());
        pageContentEntity.setBody(pageContent.body());
        pageContentEntity.setUpdatedAt(pageContent.updatedAt());

        return pageContent;
    }
}
