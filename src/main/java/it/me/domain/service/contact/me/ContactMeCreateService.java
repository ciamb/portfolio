package it.me.domain.service.contact.me;

import it.me.domain.dto.ContactMe;
import it.me.domain.repository.contact.me.ContactMeCountByEmailAndStatusPendingRepository;
import it.me.domain.repository.contact.me.ContactMePersistRepository;
import it.me.repository.contact.me.ContactMeCountByEmailAndStatusPendingRepositoryJpa;
import it.me.repository.entity.ContactMeEntity;
import it.me.web.dto.request.ContactMeRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.time.ZonedDateTime;

@ApplicationScoped
public class ContactMeCreateService {
    private final static Logger logger = Logger.getLogger(ContactMeCreateService.class.getName());

    @Inject
    ContactMePersistRepository contactMePersistRepository;

    @Inject
    ContactMeCountByEmailAndStatusPendingRepository contactMeCountByEmailAndStatusPendingRepository;

    public ContactMe createContactMe(ContactMeRequest contactMeRequest) {
        logger.infof("Received ContactMeRequest=%s", contactMeRequest);

        var count = contactMeCountByEmailAndStatusPendingRepository
                .countContactMeByEmailAndStatusPending(contactMeRequest.email());
        if (count > 0) {
            throw new IllegalStateException("""
                    Hai gi\u00E0 inserito un messaggio!
                    Attendi che sia elaborato dal batch durante il fine settimana.
                    Ci sentiamo presto!
                    """);
        }

        var contactMe = ContactMe.builder()
                .email(contactMeRequest.email())
                .name(contactMeRequest.name())
                .message(contactMeRequest.message())
                .contactBack(contactMeRequest.contactBack())
                .status(ContactMeEntity.Status.PENDING)
                .attempts(0)
                .createdAt(ZonedDateTime.now())
                .updatedAt(ZonedDateTime.now())
                .build();
        //attempts and status set 0 and PENDING
        return contactMePersistRepository.persist(contactMe);
    }
}
