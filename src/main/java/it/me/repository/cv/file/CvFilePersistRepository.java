package it.me.repository.cv.file;

import it.me.repository.entity.CvFileEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class CvFilePersistRepository {

    @Inject
    EntityManager em;

    public CvFileEntity persistCvFile(CvFileEntity cvFileEntity) {
        em.persist(cvFileEntity);
        em.flush();
        em.refresh(cvFileEntity);
        return cvFileEntity;
    }
}
