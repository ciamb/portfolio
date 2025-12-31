package it.me.repository.cv.file;

import it.me.domain.dto.CvFile;
import it.me.domain.repository.cv.file.CvFileReadByIsActiveRepository;
import it.me.repository.cv.file.mapper.CvFileEntity2CvFileMapper;
import it.me.repository.entity.CvFileEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.Optional;

@ApplicationScoped
public class CvFileReadByIsActiveRepositoryJpa implements CvFileReadByIsActiveRepository {
    @Inject
    EntityManager em;

    @Inject
    CvFileEntity2CvFileMapper cvFileEntity2CvFileMapper;

    public Optional<CvFile> readByIsActive() {
        return em.createNamedQuery(CvFileEntity.READ_BY_IS_ACTIVE, CvFileEntity.class)
                .getResultStream()
                .findFirst()
                .map(cvFileEntity2CvFileMapper);
    }
}
