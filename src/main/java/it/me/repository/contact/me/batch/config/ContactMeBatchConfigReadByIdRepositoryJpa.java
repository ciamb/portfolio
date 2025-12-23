package it.me.repository.contact.me.batch.config;

import it.me.domain.dto.ContactMeBatchConfig;
import it.me.domain.repository.contact.me.batch.config.ContactMeBatchConfigReadByIdRepository;
import it.me.repository.contact.me.batch.config.mapper.ContactMeBatchConfigEntity2ContactMeBatchConfigMapper;
import it.me.repository.entity.ContactMeBatchConfigEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.Optional;

@ApplicationScoped
public class ContactMeBatchConfigReadByIdRepositoryJpa implements ContactMeBatchConfigReadByIdRepository {
    @Inject
    EntityManager em;

    @Inject
    ContactMeBatchConfigEntity2ContactMeBatchConfigMapper mapper;

    public Optional<ContactMeBatchConfig> readByIdEquals1() {
        return em.createNamedQuery(ContactMeBatchConfigEntity.READ_BY_ID, ContactMeBatchConfigEntity.class)
                .setParameter("id", 1)
                .getResultStream()
                .findFirst()
                .map(mapper);
    }
}
