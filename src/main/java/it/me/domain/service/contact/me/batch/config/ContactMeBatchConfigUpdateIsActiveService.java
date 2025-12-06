package it.me.domain.service.contact.me.batch.config;

import it.me.repository.contact.me.batch.config.ContactMeBatchConfigReadByIdRepository;
import it.me.web.dto.request.ContactMeBatchConfigUpdateRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationScoped
public class ContactMeBatchConfigUpdateIsActiveService {
    @Autowired
    ContactMeBatchConfigReadByIdRepository contactMeBatchConfigReadByIdRepository;

    @Transactional
    public Boolean updateContactMeBatchConfig(ContactMeBatchConfigUpdateRequest contactMeBatchConfigUpdateRequest) {
        var contactMeBatchConfig = contactMeBatchConfigReadByIdRepository.readByIdEquals1()
                .orElseThrow(() -> new IllegalStateException("Contact Me Batch Config not found"));
        contactMeBatchConfig.setIsActive(contactMeBatchConfigUpdateRequest.isActive());
        return contactMeBatchConfig.isActive();
    }
}
