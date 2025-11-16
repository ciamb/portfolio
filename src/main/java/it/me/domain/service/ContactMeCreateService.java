package it.me.domain.service;

import it.me.entity.ContactMe;
import it.me.repository.ContactMePersistRepository;
import it.me.web.dto.ContactMeRequest;
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

    @Transactional
    public ContactMe createContactMe(ContactMeRequest contactMeRequest) {
        logger.infof("Received ContactMeRequest=%s", contactMeRequest);
        var contactMe = new ContactMe()
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
