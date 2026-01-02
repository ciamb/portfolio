package it.me.repository.contact.me;

import it.me.domain.repository.contact.me.ContactMeDeleteByStatusProcessedRepository;
import it.me.repository.entity.ContactMeEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ContactMeDeleteByStatusProcessedRepositoryJpa implements ContactMeDeleteByStatusProcessedRepository {
    @Inject
    EntityManager em;

    @Transactional
    @Override
    public int deleteAllProcessed() {
        return em.createNamedQuery(ContactMeEntity.DELETE_ALL_BY_STATUS_PROCESSED)
                .executeUpdate();
    }
}
