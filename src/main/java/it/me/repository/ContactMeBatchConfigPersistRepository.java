package it.me.repository;

import it.me.entity.ContactMeBatchConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class ContactMeBatchConfigPersistRepository {
    @Inject
    EntityManager em;

    public ContactMeBatchConfig persist(ContactMeBatchConfig contactMeBatchConfig) {
        em.persist(contactMeBatchConfig);
        em.flush();
        em.refresh(contactMeBatchConfig);
        return contactMeBatchConfig;
    }
}
