package it.me.domain;

import it.me.entity.PageContent;
import it.me.repository.PageContentReadBySlugRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

@ApplicationScoped
public class PageContentHomeService {

    private final PageContentReadBySlugRepository pageContentReadBySlugRepository;
    private final EntityManager em;

    @Inject
    public PageContentHomeService(PageContentReadBySlugRepository pageContentReadBySlugRepository, EntityManager em) {
        this.pageContentReadBySlugRepository = pageContentReadBySlugRepository;
        this.em = em;
    }

    @Transactional
    public PageContent createHomeIfMissing() {
        return pageContentReadBySlugRepository.readBySlug("home")
                .orElseGet(() -> {
                    PageContent home = new PageContent()
                            .setSlug("home")
                            .setTitle("Ciao, sono Andrea, ma per gli amici Ciamb!")
                            .setBody("""
                                    Benvenuto nel mio mini-portfolio, costruito con Quarkus - Qute - SQLite!
                                    I testi che vedi arrivano direttamente dal database, e li aggiorno via api quando devo aggiornare le mie competenze!
                                    Buona permanenza :)
                                    """)
                            .setUpdatedAt(LocalDateTime.now());
                    em.persist(home);
                    return home;
                });
    }
}
