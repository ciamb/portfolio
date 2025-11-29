package it.me.repository;

import it.me.entity.ContactMe;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class ContactMeCountByEmailAndStatusPendingRepository {
    @Inject
    EntityManager em;

    public Long countContactMeByEmailAndStatusPending(String email) {
        return em.createNamedQuery(ContactMe.COUNT_BY_EMAIL_AND_STATUS_PENDING, Long.class)
                .setParameter("email", email)
                .setParameter("status", ContactMe.Status.PENDING)
                .getSingleResult();
    }
}
