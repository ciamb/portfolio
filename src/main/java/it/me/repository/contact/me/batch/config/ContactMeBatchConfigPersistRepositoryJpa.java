package it.me.repository.contact.me.batch.config;

import it.me.domain.dto.ContactMeBatchConfig;
import it.me.domain.repository.contact.me.batch.config.ContactMeBatchConfigPersistRepository;
import it.me.repository.contact.me.batch.config.mapper.ContactMeBatchConfig2ContactMeBatchConfigEntityMapper;
import it.me.repository.entity.ContactMeBatchConfigEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ContactMeBatchConfigPersistRepositoryJpa implements ContactMeBatchConfigPersistRepository {
    @Inject
    EntityManager em;

    @Inject
    ContactMeBatchConfig2ContactMeBatchConfigEntityMapper mapper;

    @Transactional
    public ContactMeBatchConfig persist(ContactMeBatchConfig contactMeBatchConfig) {
        ContactMeBatchConfigEntity entity = mapper.apply(contactMeBatchConfig);
        em.persist(entity);
        return contactMeBatchConfig;
    }
}
