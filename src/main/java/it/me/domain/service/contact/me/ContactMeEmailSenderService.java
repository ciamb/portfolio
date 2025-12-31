package it.me.domain.service.contact.me;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import it.me.domain.dto.ContactMe;
import it.me.domain.mapper.ContactMeEmailBodyMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.ZonedDateTime;
import java.util.List;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ContactMeEmailSenderService {
    private final Logger logger = Logger.getLogger(ContactMeEmailSenderService.class);

    @Inject
    ContactMeEmailBodyMapper contactMeEmailBodyMapper;

    @Inject
    Mailer mailer;

    public void sendSummaryEmailForPendingList(List<ContactMe> processedList, String targetEmail) {
        var emailBody = contactMeEmailBodyMapper.apply(processedList);
        var emailSubject = "Riepilogo dei messaggi Contact Me al %s".formatted(ZonedDateTime.now());
        logger.infof("Sending email via quarkus mailer");
        try {
            mailer.send(Mail.withText(targetEmail, emailSubject, emailBody));
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }
}
