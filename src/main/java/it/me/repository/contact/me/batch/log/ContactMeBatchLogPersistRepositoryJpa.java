package it.me.repository.contact.me.batch.log;

import it.me.domain.dto.ContactMeBatchLog;
import it.me.domain.repository.contact.me.batch.log.ContactMeBatchLogPersistRepository;
import it.me.repository.contact.me.batch.log.mapper.ContactMeBatchLog2ContactMeBatchLogEntityMapper;
import it.me.repository.entity.ContactMeBatchLogEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ContactMeBatchLogPersistRepositoryJpa implements ContactMeBatchLogPersistRepository {
    @Inject
    EntityManager em;

    @Inject
    ContactMeBatchLog2ContactMeBatchLogEntityMapper mapper;

    @Transactional
    @Override
    public ContactMeBatchLog persist(ContactMeBatchLog contactMeBatchLog) {
        ContactMeBatchLogEntity entity = mapper.apply(contactMeBatchLog);
        em.persist(entity);
        return contactMeBatchLog;
    }
}
