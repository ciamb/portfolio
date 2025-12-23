package it.me.repository.contact.me;

import it.me.domain.dto.ContactMe;
import it.me.domain.repository.contact.me.ContactMePersistRepository;
import it.me.repository.contact.me.mapper.ContactMe2ContactMeEntityMapper;
import it.me.repository.entity.ContactMeEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ContactMePersistRepositoryJpa implements ContactMePersistRepository {
    @Inject
    EntityManager em;

    @Inject
    ContactMe2ContactMeEntityMapper contactMe2ContactMeEntityMapper;

    @Transactional
    @Override
    public ContactMe persist(ContactMe contactMe) {
        ContactMeEntity entity = contactMe2ContactMeEntityMapper.apply(contactMe);
        em.persist(entity);
        return contactMe;
    }
}
