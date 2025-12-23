package it.me.repository.contact.me;

import it.me.domain.repository.contact.me.ContactMeCountByEmailAndStatusPendingRepository;
import it.me.repository.entity.ContactMeEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class ContactMeCountByEmailAndStatusPendingRepositoryJpa implements ContactMeCountByEmailAndStatusPendingRepository {
    @Inject
    EntityManager em;

    public Long countContactMeByEmailAndStatusPending(String email) {
        return em.createNamedQuery(ContactMeEntity.COUNT_BY_EMAIL_AND_STATUS_PENDING, Long.class)
                .setParameter("email", email)
                .setParameter("status", ContactMeEntity.Status.PENDING)
                .getSingleResult();
    }
}
