package it.me.repository;

import it.me.entity.CvFile;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class CvFileReadByIsActiveRepository {
    @Inject
    EntityManager em;

    public CvFile readByIsActive() {
        return em.createNamedQuery(CvFile.READ_BY_IS_ACTIVE, CvFile.class)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No CvFile active found, please insert a new CV"));
    }
}
