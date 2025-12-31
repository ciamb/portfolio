package it.me.repository.contact.me;

import it.me.domain.dto.ContactMe;
import it.me.domain.repository.contact.me.ContactMeReadByStatusPendingRepository;
import it.me.repository.contact.me.mapper.ContactMeEntity2ContactMeMapper;
import it.me.repository.entity.ContactMeEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class ContactMeReadByStatusPendingRepositoryJpa implements ContactMeReadByStatusPendingRepository {
    @Inject
    EntityManager em;

    @Inject
    ContactMeEntity2ContactMeMapper mapper;

    public List<ContactMe> readAllByStatusPending() {
        return em.createNamedQuery(ContactMeEntity.READ_ALL_BY_STATUS_PENDING, ContactMeEntity.class)
                .getResultStream()
                .map(mapper)
                .toList();
    }
}
