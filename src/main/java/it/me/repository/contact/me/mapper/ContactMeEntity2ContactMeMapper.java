package it.me.repository.contact.me.mapper;

import it.me.domain.dto.ContactMe;
import it.me.repository.entity.ContactMeEntity;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.function.Function;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ContactMeEntity2ContactMeMapper implements Function<ContactMeEntity, ContactMe> {
    private final Logger logger = Logger.getLogger(ContactMeEntity2ContactMeMapper.class);

    @Override
    public ContactMe apply(ContactMeEntity contactMeEntity) {
        if (contactMeEntity == null) {
            logger.warn("mapping null contactMeEntity");
            return null;
        }

        return ContactMe.builder()
                .id(contactMeEntity.id())
                .email(contactMeEntity.email())
                .name(contactMeEntity.name())
                .message(contactMeEntity.message())
                .contactBack(contactMeEntity.contactBack())
                .status(contactMeEntity.status())
                .attempts(contactMeEntity.attempts())
                .lastAttemptAt(contactMeEntity.lastAttemptAt())
                .errorReason(contactMeEntity.errorReason())
                .createdAt(contactMeEntity.createdAt())
                .updatedAt(contactMeEntity.updatedAt())
                .build();
    }
}
