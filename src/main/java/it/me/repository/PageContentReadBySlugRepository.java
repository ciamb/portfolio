package it.me.repository;

import it.me.entity.PageContent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.Optional;

@ApplicationScoped
public class PageContentReadBySlugRepository {

    private final EntityManager em;

    @Inject
    public PageContentReadBySlugRepository(EntityManager em) {
        this.em = em;
    }

    public Optional<PageContent> readBySlug(String slug) {
        return em.createNamedQuery(PageContent.READ_BY_SLUG, PageContent.class)
                .setParameter("slug", slug)
                .getResultStream()
                .findFirst();
    }

}
