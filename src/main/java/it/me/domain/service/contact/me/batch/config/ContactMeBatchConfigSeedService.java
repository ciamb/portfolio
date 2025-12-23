package it.me.domain.service.contact.me.batch.config;

import it.me.domain.dto.ContactMeBatchConfig;
import it.me.domain.repository.contact.me.batch.config.ContactMeBatchConfigPersistRepository;
import it.me.domain.repository.contact.me.batch.config.ContactMeBatchConfigReadByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ContactMeBatchConfigSeedService {
    private final Logger logger = Logger.getLogger(ContactMeBatchConfigSeedService.class);

    @Inject
    ContactMeBatchConfigPersistRepository contactMeBatchConfigPersistRepository;

    @Inject
    ContactMeBatchConfigReadByIdRepository contactMeBatchConfigReadByIdRepository;

    @ConfigProperty(name = "contact.me.batch.config.target-email", defaultValue = "")
    String targetEmail;

    @Transactional
    public void createContactMeBatchConfigIfMissing() {
        var found = contactMeBatchConfigReadByIdRepository.readByIdEquals1();
        if (found.isPresent()) {
            var contactMeBatchConfig = found.get();
            logger.infof(
                    "Found! contact_me_batch_config already exists with id %d",
                    contactMeBatchConfig.id()
            );
            if (contactMeBatchConfig.targetEmail().isBlank()) {
                logger.warnf("target_email non \u00E8 configurata correttamente a DB, controlla");
            }
            return;
        }

        if (targetEmail.isBlank()) {
            throw new IllegalStateException("Add contact.me.batch.config.target-email to env");
        }

        ContactMeBatchConfig contactMeBatchConfig = ContactMeBatchConfig.builder()
                .id(1)
                .isActive(false)
                .targetEmail(targetEmail)
                .build();
        logger.infof("Seeding DB with new contact_me_batch_config with id %d", contactMeBatchConfig.id());

        contactMeBatchConfigPersistRepository.persist(contactMeBatchConfig);
    }
}
