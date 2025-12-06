package it.me.repository.page.content;

import it.me.domain.dto.PageContent;
import it.me.domain.repository.page.content.PageContentReadBySlugRepository;
import it.me.repository.entity.PageContentEntity;
import it.me.repository.page.content.mapper.PageContentEntity2PageContentMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.Optional;

@ApplicationScoped
public class PageContentReadBySlugRepositoryJpa implements PageContentReadBySlugRepository {

    @Inject
    EntityManager em;

    @Inject
    PageContentEntity2PageContentMapper pageContentEntity2PageContentMapper;

    public Optional<PageContent> readBySlug(String slug) {
        return em.createNamedQuery(PageContentEntity.READ_BY_SLUG, PageContentEntity.class)
                .setParameter("slug", slug)
                .getResultStream()
                .findFirst()
                .map(pageContentEntity2PageContentMapper);
    }

}
