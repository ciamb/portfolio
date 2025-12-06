package it.me.repository.page.content;

import it.me.repository.entity.PageContentEntity;
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

    public Optional<PageContentEntity> readBySlug(String slug) {
        return em.createNamedQuery(PageContentEntity.READ_BY_SLUG, PageContentEntity.class)
                .setParameter("slug", slug)
                .getResultStream()
                .findFirst();
    }

}
