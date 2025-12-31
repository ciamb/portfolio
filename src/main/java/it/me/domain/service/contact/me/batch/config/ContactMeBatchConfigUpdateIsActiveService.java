package it.me.domain.service.contact.me.batch.config;

import it.me.domain.repository.contact.me.batch.config.ContactMeBatchConfigPersistRepository;
import it.me.domain.repository.contact.me.batch.config.ContactMeBatchConfigReadByIdRepository;
import it.me.web.dto.request.ContactMeBatchConfigUpdateRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ContactMeBatchConfigUpdateIsActiveService {
    private final Logger logger = Logger.getLogger(ContactMeBatchConfigUpdateIsActiveService.class);

    @Inject
    ContactMeBatchConfigReadByIdRepository contactMeBatchConfigReadByIdRepository;

    @Inject
    ContactMeBatchConfigPersistRepository contactMeBatchConfigPersistRepository;

    public Boolean updateContactMeBatchConfig(ContactMeBatchConfigUpdateRequest contactMeBatchConfigUpdateRequest) {
        var contactMeBatchConfig = contactMeBatchConfigReadByIdRepository
                .readByIdEquals1()
                .orElseThrow(() -> new IllegalStateException("Contact Me Batch Config not found"));
        contactMeBatchConfig.activate();
        logger.infof("Contact Me Batch Config is Activated, %s", contactMeBatchConfig);
        contactMeBatchConfigPersistRepository.persist(contactMeBatchConfig);
        return contactMeBatchConfig.isActive();
    }
}
