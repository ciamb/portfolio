package it.me.domain.service.contact.me;

import it.me.repository.entity.ContactMeEntity;
import it.me.repository.contact.me.ContactMeCountByEmailAndStatusPendingRepository;
import it.me.repository.contact.me.ContactMePersistRepository;
import it.me.web.dto.request.ContactMeRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.time.ZonedDateTime;

@ApplicationScoped
public class ContactMeCreateService {
    private final static Logger logger = Logger.getLogger(ContactMeCreateService.class.getName());

    @Inject
    ContactMePersistRepository contactMePersistRepository;

    @Inject
    ContactMeCountByEmailAndStatusPendingRepository contactMeCountByEmailAndStatusPendingRepository;

    @Transactional
    public ContactMeEntity createContactMe(ContactMeRequest contactMeRequest) {
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

        var contactMe = new ContactMeEntity()
                .setEmail(contactMeRequest.email())
                .setName(contactMeRequest.name())
                .setMessage(contactMeRequest.message())
                .setContactBack(contactMeRequest.contactBack())
                .setCreatedAt(ZonedDateTime.now())
                .setUpdatedAt(ZonedDateTime.now());
        //attempts and status set by default 0 and PENDING
        return contactMePersistRepository.persist(contactMe);
    }
}
