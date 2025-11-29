package it.me.repository;

import it.me.entity.ContactMe;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;

@ApplicationScoped
public class ContactMeReadByStatusPendingRepository {
    @Inject
    EntityManager em;

    public List<ContactMe> readAllByStatusPending() {
        return em.createNamedQuery(ContactMe.READ_ALL_BY_STATUS_PENDING, ContactMe.class)
                .getResultList();
    }
}
