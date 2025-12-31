package it.me.repository.cv.file;

import it.me.domain.repository.cv.file.CvFileExistsIsActiveRepository;
import it.me.repository.entity.CvFileEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class CvFileExistsIsActiveRepositoryJpa implements CvFileExistsIsActiveRepository {
    @Inject
    EntityManager em;

    /**
     * Recupera il Cv File attivo e torna true / false in base a se
     * e stato trovato il risultato oppure no
     *
     * @return true / false
     */
    @Override
    public boolean existsActiveCvFile() {
        return em.createNamedQuery(CvFileEntity.READ_BY_IS_ACTIVE, CvFileEntity.class)
                .getResultStream()
                .findFirst()
                .isPresent();
    }
}
