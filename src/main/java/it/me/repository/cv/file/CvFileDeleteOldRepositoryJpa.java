package it.me.repository.cv.file;

import it.me.domain.repository.cv.file.CvFileDeleteOldRepository;
import it.me.repository.entity.CvFileEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

@ApplicationScoped
public class CvFileDeleteOldRepositoryJpa implements CvFileDeleteOldRepository {
    private final Logger logger = Logger.getLogger(CvFileDeleteOldRepositoryJpa.class);

    @Inject
    EntityManager em;

    @Override
    @Transactional
    public void deleteAllNotActive() {
        int deleted =
                em.createNamedQuery(CvFileEntity.DELETE_ALL_IS_ACTIVE_FALSE).executeUpdate();
        logger.infof("Deleted %d records from CvFileEntity", deleted);
        em.clear();
    }
}
