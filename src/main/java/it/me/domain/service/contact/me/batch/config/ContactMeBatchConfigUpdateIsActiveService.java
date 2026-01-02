package it.me.domain.service.contact.me.batch.config;

import it.me.domain.repository.contact.me.batch.config.ContactMeBatchConfigUpdateRepository;
import it.me.web.dto.request.ContactMeBatchConfigUpdateRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ContactMeBatchConfigUpdateIsActiveService {
    private final Logger logger = Logger.getLogger(ContactMeBatchConfigUpdateIsActiveService.class);

    @Inject
    ContactMeBatchConfigUpdateRepository contactMeBatchConfigUpdateRepository;

    public Boolean updateContactMeBatchConfig(ContactMeBatchConfigUpdateRequest contactMeBatchConfigUpdateRequest) {
        int update = contactMeBatchConfigUpdateRepository.update(contactMeBatchConfigUpdateRequest.isActive());
        logger.infof("update %d config", update);
        return update == 1;
    }
}
