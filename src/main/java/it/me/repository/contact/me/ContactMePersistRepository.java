package it.me.repository.contact.me;

import it.me.repository.entity.ContactMeEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class ContactMePersistRepository {
    @Inject
    EntityManager em;

    public ContactMeEntity persist(ContactMeEntity contactMeEntity) {
        em.persist(contactMeEntity);
        em.flush();
        em.refresh(contactMeEntity);
        return contactMeEntity;
    }
}
