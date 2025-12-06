package it.me.domain.mapper;

import it.me.domain.dto.ProcessedContactMe;
import it.me.repository.entity.ContactMeEntity;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.function.Function;

@ApplicationScoped
public class ProcessedContactMeMapper implements Function<ContactMeEntity, ProcessedContactMe> {
    private static Logger logger = Logger.getLogger(ProcessedContactMeMapper.class);

    @Override
    public ProcessedContactMe apply(ContactMeEntity contactMeEntity) {
        if (contactMeEntity == null) {
            logger.warn("contactMe is null");
            return null;
        }

        return ProcessedContactMe.builder()
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
