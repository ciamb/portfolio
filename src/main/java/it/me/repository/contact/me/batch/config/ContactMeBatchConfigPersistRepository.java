package it.me.repository.contact.me.batch.config;

import it.me.repository.entity.ContactMeBatchConfigEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class ContactMeBatchConfigPersistRepository {
    @Inject
    EntityManager em;

    public ContactMeBatchConfigEntity persist(ContactMeBatchConfigEntity contactMeBatchConfigEntity) {
        em.persist(contactMeBatchConfigEntity);
        em.flush();
        em.refresh(contactMeBatchConfigEntity);
        return contactMeBatchConfigEntity;
    }
}
