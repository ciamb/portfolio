package it.me.repository.contact.me.batch.config.mapper;

import it.me.domain.dto.ContactMeBatchConfig;
import it.me.repository.entity.ContactMeBatchConfigEntity;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.function.Function;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ContactMeBatchConfig2ContactMeBatchConfigEntityMapper
        implements Function<ContactMeBatchConfig, ContactMeBatchConfigEntity> {
    private final Logger logger = Logger.getLogger(ContactMeBatchConfig2ContactMeBatchConfigEntityMapper.class);

    @Override
    public ContactMeBatchConfigEntity apply(ContactMeBatchConfig contactMeBatchConfig) {
        if (contactMeBatchConfig == null) {
            logger.warn("mapping contactMeBatchConfig is null");
            return null;
        }

        return new ContactMeBatchConfigEntity()
                .setId(contactMeBatchConfig.id())
                .setIsActive(contactMeBatchConfig.isActive())
                .setTargetEmail(contactMeBatchConfig.targetEmail());
    }
}
