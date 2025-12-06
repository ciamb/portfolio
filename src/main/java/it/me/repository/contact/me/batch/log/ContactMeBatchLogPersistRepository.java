package it.me.repository.contact.me.batch.log;

import it.me.repository.entity.ContactMeBatchLogEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationScoped
public class ContactMeBatchLogPersistRepository {
    @Autowired
    EntityManager em;

    @Transactional
    public ContactMeBatchLogEntity persist(ContactMeBatchLogEntity contactMeBatchLogEntity) {
        em.persist(contactMeBatchLogEntity);
        em.flush();
        em.refresh(contactMeBatchLogEntity);
        return contactMeBatchLogEntity;
    }
}
