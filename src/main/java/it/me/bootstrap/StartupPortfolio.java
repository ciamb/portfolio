package it.me.bootstrap;

import io.quarkus.runtime.StartupEvent;
import it.me.domain.PageContentHomeService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class StartupPortfolio {

    private final PageContentHomeService pageContentHomeService;

    public StartupPortfolio(PageContentHomeService pageContentHomeService) {
        this.pageContentHomeService = pageContentHomeService;
    }

    void startup(@Observes StartupEvent startupEvent) {
        pageContentHomeService.createHomeIfMissing();
    }
}
