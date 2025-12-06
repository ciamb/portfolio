package it.me.bootstrap;

import io.quarkus.runtime.StartupEvent;
import it.me.domain.service.contact.me.batch.config.ContactMeBatchConfigSeedService;
import it.me.domain.service.page.content.PageContentHomeService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class StartupPortfolio {
    @Inject
    PageContentHomeService pageContentHomeService;

    @Inject
    ContactMeBatchConfigSeedService contactMeBatchConfigSeedService;

    /**
     * <p>Startup action only <strong>if needed</strong>:</p>
     * <ul>
     *     <li>create a basic home page if missing on DB</li>
     *     <li>create batch configuration for contact me email</li>
     * </ul>
     *
     * @param startupEvent startupEvent observer
     */
    void startup(@Observes StartupEvent startupEvent) {
        pageContentHomeService.createHomeIfMissing();
        contactMeBatchConfigSeedService.createContactMeBatchConfigIfMissing();
    }
}
