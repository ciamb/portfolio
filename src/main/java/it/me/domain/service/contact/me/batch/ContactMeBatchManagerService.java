package it.me.domain.service.contact.me.batch;

import it.me.domain.dto.ContactMeBatchLog;
import it.me.domain.dto.ProcessedInfo;
import it.me.domain.repository.contact.me.batch.config.ContactMeBatchConfigReadByIdRepository;
import it.me.domain.repository.contact.me.batch.log.ContactMeBatchLogPersistRepository;
import it.me.domain.service.contact.me.ContactMeEmailSenderService;
import it.me.domain.service.contact.me.ContactMeProcessingService;
import it.me.web.dto.response.ContactMeBatchResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.concurrent.atomic.AtomicBoolean;
import org.jboss.logging.Logger;

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
        var contactMeBatchConfig = contactMeBatchConfigReadByIdRepository
                .readByIdEquals1()
                .orElseThrow(() -> new IllegalStateException("No contact me batch config present"));

        if (!contactMeBatchConfig.isActive()) {
            logger.warnf("Contact me batch config is not active");
            return new ContactMeBatchResponse(false, null, 0, 0, null, false);
        }

        ProcessedInfo processedInfo = contactMeProcessingService.processPendingContactMe();

        var isProcessed = processedInfo.processedContactMe() != null
                && !processedInfo.processedContactMe().isEmpty();
        if (!isProcessed) {
            logger.warnf("No contact me processed");
            return new ContactMeBatchResponse(true, null, 0, 0, null, false);
        }

        AtomicBoolean mailSuccess = new AtomicBoolean(false);

        try {
            contactMeEmailSenderService.sendSummaryEmailForPendingList(
                    processedInfo.processedContactMe(), contactMeBatchConfig.targetEmail());
            mailSuccess.compareAndSet(false, true);
        } catch (Exception e) {
            logger.errorf("Invio della email sommario di contact me non riuscita. Errore %s", e.getMessage());
            mailSuccess.compareAndSet(false, false);
        }

        ContactMeBatchLog contactMeBatchLog = ContactMeBatchLog.builder()
                .runAt(processedInfo.runAt())
                .processed(processedInfo.processed())
                .withError(processedInfo.withError())
                .sentTo(contactMeBatchConfig.targetEmail())
                .build();

        ContactMeBatchLog log = contactMeBatchLogPersistRepository.persist(contactMeBatchLog);

        return new ContactMeBatchResponse(
                true,
                log.id(),
                processedInfo.processed(),
                processedInfo.withError(),
                contactMeBatchConfig.targetEmail(),
                mailSuccess.get());
    }
}
