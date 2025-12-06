package it.me.repository.contact.me;

import it.me.repository.entity.ContactMeEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;

@ApplicationScoped
public class ContactMeReadByStatusPendingRepository {
    @Inject
    EntityManager em;

    public List<ContactMeEntity> readAllByStatusPending() {
        return em.createNamedQuery(ContactMeEntity.READ_ALL_BY_STATUS_PENDING, ContactMeEntity.class)
                .getResultList();
    }
}
