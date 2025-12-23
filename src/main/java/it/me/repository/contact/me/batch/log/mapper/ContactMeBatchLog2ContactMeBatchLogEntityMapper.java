package it.me.repository.contact.me.batch.log.mapper;

import it.me.domain.dto.ContactMeBatchLog;
import it.me.repository.entity.ContactMeBatchLogEntity;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.function.Function;

@ApplicationScoped
public class ContactMeBatchLog2ContactMeBatchLogEntityMapper
        implements Function<ContactMeBatchLog, ContactMeBatchLogEntity> {
    private final Logger logger = Logger.getLogger(ContactMeBatchLog2ContactMeBatchLogEntityMapper.class);

    @Override
    public ContactMeBatchLogEntity apply(ContactMeBatchLog contactMeBatchLog) {
        if (contactMeBatchLog == null) {
            logger.warn("Mapping null contactMeBatchLog");
            return null;
        }

        return new ContactMeBatchLogEntity()
                .setRunAt(contactMeBatchLog.runAt())
                .setProcessed(contactMeBatchLog.processed())
                .setWithError(contactMeBatchLog.withError())
                .setSentTo(contactMeBatchLog.sentTo());
    }
}
