package it.me.domain.service.contact.me;

import it.me.domain.dto.ProcessedInfo;
import it.me.domain.repository.contact.me.ContactMeDeleteByStatusProcessedRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.ZonedDateTime;
import java.util.Collections;

@ApplicationScoped
public class ContactMeDeleteAllProcessedService {
    @Inject
    ContactMeDeleteByStatusProcessedRepository contactMeDeleteByStatusProcessedRepository;

    public ProcessedInfo deleteAllProcessed() {
        int deleted = contactMeDeleteByStatusProcessedRepository.deleteAllProcessed();
        return new ProcessedInfo(ZonedDateTime.now(), deleted, 0, Collections.emptyList());
    }
}
