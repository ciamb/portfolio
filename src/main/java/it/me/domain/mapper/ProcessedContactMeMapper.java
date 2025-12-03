package it.me.domain.mapper;

import it.me.domain.dto.ProcessedContactMe;
import it.me.entity.ContactMe;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.function.Function;

@ApplicationScoped
public class ProcessedContactMeMapper implements Function<ContactMe, ProcessedContactMe> {
    private static Logger logger = Logger.getLogger(ProcessedContactMeMapper.class);

    @Override
    public ProcessedContactMe apply(ContactMe contactMe) {
        if (contactMe == null) {
            logger.warn("contactMe is null");
            return null;
        }

        return ProcessedContactMe.builder()
                .id(contactMe.id())
                .email(contactMe.email())
                .name(contactMe.name())
                .message(contactMe.message())
                .contactBack(contactMe.contactBack())
                .status(contactMe.status())
                .attempts(contactMe.attempts())
                .lastAttemptAt(contactMe.lastAttemptAt())
                .errorReason(contactMe.errorReason())
                .createdAt(contactMe.createdAt())
                .updatedAt(contactMe.updatedAt())
                .build();
    }
}
