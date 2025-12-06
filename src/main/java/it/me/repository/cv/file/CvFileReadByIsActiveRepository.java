package it.me.repository.cv.file;

import it.me.repository.entity.CvFileEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.Optional;

@ApplicationScoped
public class CvFileReadByIsActiveRepository {
    @Inject
    EntityManager em;

    public Optional<CvFileEntity> readByIsActive() {
        return em.createNamedQuery(CvFileEntity.READ_BY_IS_ACTIVE, CvFileEntity.class)
                .getResultStream()
                .findFirst();
    }
}
