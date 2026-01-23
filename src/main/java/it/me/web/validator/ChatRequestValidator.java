package it.me.web.validator;

import it.me.domain.PortfolioPublicK;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.Optional;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ChatRequestValidator {
    private static final Logger logger = Logger.getLogger(ChatRequestValidator.class.getName());

    public boolean isMessageOutOfScope(String userMessage) {
        String normalized =
                Optional.ofNullable(userMessage).map(String::toLowerCase).orElse("");

        boolean inScope = Arrays.stream(PortfolioPublicK.OpenAi.IN_SCOPE).anyMatch(normalized::contains);
        if (inScope) {
            logger.info("User message is in scope, proceed with request");
            return false;
        }

        boolean outOfScope = Arrays.stream(PortfolioPublicK.OpenAi.OUT_OF_SCOPE).anyMatch(normalized::contains);
        if (outOfScope) {
            return true;
        }

        // di default blocca tutte le richieste che non sono in scope
        // ma in un futuro potrevve cambiare
        return true;
    }
}
