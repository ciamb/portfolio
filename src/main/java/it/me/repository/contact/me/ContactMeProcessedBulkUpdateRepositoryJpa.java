package it.me.repository.contact.me;

import it.me.domain.dto.ContactMe;
import it.me.domain.repository.contact.me.ContactMeProcessedBulkUpdateRepository;
import it.me.repository.entity.ContactMeEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class ContactMeProcessedBulkUpdateRepositoryJpa implements ContactMeProcessedBulkUpdateRepository {
    private final Logger logger = Logger.getLogger(ContactMeProcessedBulkUpdateRepositoryJpa.class.getName());

    @Inject
    EntityManager em;

    @Transactional
    public void updateProcessedContactMe(List<ContactMe> processedContactMe) {
        List<ContactMeEntity> updated = processedContactMe.stream()
                .map(pcm -> {
                    if (pcm.id() == null) {
                        logger.warnf("Anomalia, Processed contact me senza id %s", pcm);
                        return null;
                    }

                    var entity = em.find(ContactMeEntity.class, pcm.id());
                    if (entity == null) {
                        logger.warnf("Nessuna entita trovata con id %d", pcm.id());
                        return null;
                    }

                    entity.setStatus(pcm.status());
                    entity.setAttempts(pcm.attempts());
                    entity.setLastAttemptAt(pcm.lastAttemptAt());
                    entity.setUpdatedAt(pcm.updatedAt());
                    entity.setErrorReason(pcm.errorReason());
                    return entity;
                })
                .toList();
        logger.infof("Processed contact me aggiornate: %d", updated.size());
    }
}
