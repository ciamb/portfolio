package it.me.domain.service;

import it.me.domain.Page;
import it.me.entity.PageContent;
import it.me.repository.PageContentReadBySlugRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.time.ZonedDateTime;

import org.jboss.logging.Logger;

@ApplicationScoped
public class PageContentHomeService {
    private static final Logger LOG = Logger.getLogger(PageContentHomeService.class.getName());

    @Inject
    PageContentReadBySlugRepository pageContentReadBySlugRepository;

    @Inject
    EntityManager em;

    @Transactional
    public PageContent createHomeIfMissing() {
        return pageContentReadBySlugRepository.readBySlug(Page.HOME.getSlug())
                .orElseGet(() -> {
                    LOG.infof("PageContent slug=%s not found. Using default", Page.HOME.getSlug());

                    PageContent home = new PageContent()
                            .setSlug(Page.HOME.getSlug())
                            .setTitle("Ciao, sono Andrea, ma per gli amici Ciamb!")
                            .setBody("""
                                    Benvenuto nel mio mini-portfolio, costruito con Quarkus - Qute - SQLite!
                                    I testi che vedi arrivano direttamente dal database, e li aggiorno via api quando devo aggiornare le mie competenze!
                                    Buona permanenza :)
                                    """)
                            .setUpdatedAt(ZonedDateTime.now());

                    em.persist(home);
                    return home;
                });
    }
}
