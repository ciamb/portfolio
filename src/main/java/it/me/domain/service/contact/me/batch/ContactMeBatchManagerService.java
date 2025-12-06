package it.me.domain.service.contact.me.batch;

import it.me.domain.dto.ProcessedInfo;
import it.me.domain.service.contact.me.ContactMeEmailSenderService;
import it.me.domain.service.contact.me.ContactMeProcessingService;
import it.me.repository.entity.ContactMeBatchLogEntity;
import it.me.repository.contact.me.batch.config.ContactMeBatchConfigReadByIdRepository;
import it.me.repository.contact.me.batch.log.ContactMeBatchLogPersistRepository;
import it.me.web.dto.response.ContactMeBatchResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

@ApplicationScoped
public class ContactMeBatchManagerService {
    private final Logger logger = Logger.getLogger(ContactMeBatchManagerService.class.getName());

    @Inject
    ContactMeBatchConfigReadByIdRepository contactMeBatchConfigReadByIdRepository;

    @Inject
    ContactMeBatchLogPersistRepository contactMeBatchLogPersistRepository;

    @Inject
    ContactMeProcessingService contactMeProcessingService;

    @Inject
    ContactMeEmailSenderService contactMeEmailSenderService;

    public ContactMeBatchResponse executeBatch() {
        var contactMeBatchConfig = contactMeBatchConfigReadByIdRepository.readByIdEquals1()
                .orElseThrow(() -> new IllegalStateException("No contact me batch config present"));

        if (!contactMeBatchConfig.isActive()) {
            logger.warnf("Contact me batch config is not active");
            return new ContactMeBatchResponse(contactMeBatchConfig.isActive(), null, 0, 0, null, false);
        }

        ProcessedInfo processedInfo = contactMeProcessingService.processPendingContactMe();

        var isProcessed = processedInfo.processedContactMe() != null
                && !processedInfo.processedContactMe().isEmpty();
        if (!isProcessed) {
            logger.warnf("No contact me processed");
            return new ContactMeBatchResponse(contactMeBatchConfig.isActive(), null, 0, 0, null, false);
        }

        AtomicBoolean mailSuccess = new AtomicBoolean(false);

        try {
            contactMeEmailSenderService.sendSummaryEmailForPendingList(
                    processedInfo.processedContactMe(), contactMeBatchConfig.targetEmail());
            mailSuccess.compareAndSet(false, true);
        } catch (Exception e) {
            logger.errorf("Invio della email sommario di contact me non riuscita. Errore %s", e.getMessage());
            mailSuccess.set(false);
        }

        ContactMeBatchLogEntity log = contactMeBatchLogPersistRepository.persist(new ContactMeBatchLogEntity()
                .setRunAt(processedInfo.runAt())
                .setProcessed(processedInfo.processed())
                .setWithError(processedInfo.withError())
                .setSentTo(contactMeBatchConfig.targetEmail()));

        return new ContactMeBatchResponse(
                contactMeBatchConfig.isActive(),
                log.id(),
                processedInfo.processed(),
                processedInfo.withError(),
                contactMeBatchConfig.targetEmail(),
                mailSuccess.get());
    }
}
