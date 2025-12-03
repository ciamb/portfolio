package it.me.domain.service;

import it.me.domain.dto.ProcessedInfo;
import it.me.entity.ContactMeBatchLog;
import it.me.repository.ContactMeBatchConfigReadByIdRepository;
import it.me.repository.ContactMeBatchLogPersistRepository;
import it.me.repository.ContactMeReadByStatusPendingRepository;
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

        ContactMeBatchLog log = contactMeBatchLogPersistRepository.persist(new ContactMeBatchLog()
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
