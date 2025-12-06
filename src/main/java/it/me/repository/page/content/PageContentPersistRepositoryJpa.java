package it.me.repository.page.content;

import it.me.domain.dto.PageContent;
import it.me.domain.repository.page.content.PageContentPersistRepository;
import it.me.repository.entity.PageContentEntity;
import it.me.repository.page.content.mapper.PageContent2PageContentEntityMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PageContentPersistRepositoryJpa implements PageContentPersistRepository {

    @Inject
    EntityManager em;

    @Inject
    PageContent2PageContentEntityMapper pageContent2PageContentEntityMapper;

    @Transactional
    @Override
    public PageContent persist(PageContent pageContent) {
        PageContentEntity entity = pageContent2PageContentEntityMapper.apply(pageContent);
        em.persist(entity);
        return pageContent;
    }
}
