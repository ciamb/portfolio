package it.me.repository.contact.me.batch.config;

import it.me.domain.repository.contact.me.batch.config.ContactMeBatchConfigUpdateRepository;
import it.me.repository.entity.ContactMeBatchConfigEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ContactMeBatchConfigUpdateRepositoryJpa implements ContactMeBatchConfigUpdateRepository {
    private final Logger logger = Logger.getLogger(ContactMeBatchConfigUpdateRepositoryJpa.class);

    @Inject
    EntityManager em;

    @Transactional
    @Override
    public int update(boolean isActive) {
        return em.createNamedQuery(ContactMeBatchConfigEntity.UPDATE_BY_ID)
                .setParameter("isActive", isActive)
                .executeUpdate();
    }
}
