package it.me.repository;

import it.me.entity.ContactMeBatchLog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationScoped
public class ContactMeBatchLogPersistRepository {
    @Autowired
    EntityManager em;

    public ContactMeBatchLog persist(ContactMeBatchLog contactMeBatchLog) {
        em.persist(contactMeBatchLog);
        em.flush();
        em.refresh(contactMeBatchLog);
        return contactMeBatchLog;
    }
}
