package it.me.domain.service.page.content;

import it.me.domain.Page;
import it.me.domain.dto.PageContent;
import it.me.domain.repository.page.content.PageContentPersistRepository;
import it.me.domain.repository.page.content.PageContentReadBySlugRepository;
import it.me.repository.page.content.PageContentReadBySlugRepositoryJpa;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.time.ZonedDateTime;

@ApplicationScoped
public class PageContentHomeService {
    private static final Logger logger = Logger.getLogger(PageContentHomeService.class.getName());

    @Inject
    PageContentReadBySlugRepository pageContentReadBySlugRepository;

    @Inject
    PageContentPersistRepository pageContentPersistRepository;

    public PageContent createHomeIfMissing() {
        return pageContentReadBySlugRepository.readBySlug(Page.HOME.getSlug())
                .orElseGet(() -> {
                    logger.infof("PageContent slug=%s not found. Using default", Page.HOME.getSlug());

                    PageContent homeToInsert = PageContent.builder()
                            .slug(Page.HOME.getSlug())
                            .title("Benvenuto sul portfolio del tuo Ciambellino preferito!")
                            .subtitle("Mi chiamo Andrea e sono uno sviluppatore Java")
                            .body("""
                                    Questo portfolio \u00E8 stato costruito con Quarkus framework, utilizzando PostgreSQL su Neon DB e Render come piattaforma per il deploy.
                                    Tutti questi testi arrivano direttamente dal DB, e li aggiorno via api /admin quando necessario!
                                    Se vuoi puoi scaricare il mio CV o lasciarmi un messaggino dall'apposito bottone.
                                    Buona permanenza!
                                    """)
                            .updatedAt(ZonedDateTime.now())
                            .build();

                    pageContentPersistRepository.persist(homeToInsert);
                    return homeToInsert;
                });
    }
}
