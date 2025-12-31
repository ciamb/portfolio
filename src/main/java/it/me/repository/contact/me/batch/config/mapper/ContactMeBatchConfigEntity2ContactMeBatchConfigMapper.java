package it.me.repository.contact.me.batch.config.mapper;

import it.me.domain.dto.ContactMeBatchConfig;
import it.me.repository.entity.ContactMeBatchConfigEntity;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.function.Function;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ContactMeBatchConfigEntity2ContactMeBatchConfigMapper
        implements Function<ContactMeBatchConfigEntity, ContactMeBatchConfig> {
    private final Logger logger = Logger.getLogger(ContactMeBatchConfigEntity2ContactMeBatchConfigMapper.class);

    @Override
    public ContactMeBatchConfig apply(ContactMeBatchConfigEntity contactMeBatchConfigEntity) {
        if (contactMeBatchConfigEntity == null) {
            logger.warn("mapping null contactMeBatchConfigEntity");
            return null;
        }

        return ContactMeBatchConfig.builder()
                .id(contactMeBatchConfigEntity.id())
                .isActive(contactMeBatchConfigEntity.isActive())
                .targetEmail(contactMeBatchConfigEntity.targetEmail())
                .build();
    }
}
