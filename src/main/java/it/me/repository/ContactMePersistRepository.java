package it.me.repository;

import it.me.entity.ContactMe;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class ContactMePersistRepository {
    @Inject
    EntityManager em;

    public ContactMe persist(ContactMe contactMe) {
        em.persist(contactMe);
        em.flush();
        em.refresh(contactMe);
        return contactMe;
    }
}
