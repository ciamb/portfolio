package it.me.repository.contact.me.mapper;

import it.me.domain.dto.ContactMe;
import it.me.repository.entity.ContactMeEntity;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.time.ZonedDateTime;
import java.util.function.Function;

@ApplicationScoped
public class ContactMe2ContactMeEntityMapper implements Function<ContactMe, ContactMeEntity> {
    private final Logger logger = Logger.getLogger(ContactMe2ContactMeEntityMapper.class);
    @Override
    public ContactMeEntity apply(ContactMe contactMe) {
        if (contactMe == null) {
            logger.warn("mapping contactMe null");
            return null;
        }

        return new ContactMeEntity()
                .setId(contactMe.id())
                .setEmail(contactMe.email())
                .setName(contactMe.name())
                .setMessage(contactMe.message())
                .setContactBack(contactMe.contactBack())
                .setStatus(contactMe.status())
                .setLastAttemptAt(contactMe.lastAttemptAt())
                .setErrorReason(contactMe.errorReason())
                .setCreatedAt(contactMe.createdAt())
                .setUpdatedAt(contactMe.updatedAt());
    }
}
