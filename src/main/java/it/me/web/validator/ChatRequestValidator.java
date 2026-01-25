package it.me.web.validator;

import it.me.domain.dto.AssistantRule;
import it.me.domain.repository.assistant.rule.ReadAssistantRuleByRuleTypeRepository;
import it.me.repository.entity.AssistantRuleEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ChatRequestValidator {
    private static final Logger logger = Logger.getLogger(ChatRequestValidator.class.getName());

    @Inject
    ReadAssistantRuleByRuleTypeRepository readAssistantRuleByRuleTypeRepository;

    public boolean isMessageOutOfScope(String userMessage) {
        String normalized =
                Optional.ofNullable(userMessage).map(String::toLowerCase).orElse("");

        if (normalized.isBlank()) {
            logger.debug("Empty message -> out-of-scope");
            return true;
        }

        List<AssistantRule> inScopeRules =
                readAssistantRuleByRuleTypeRepository.readByRuleType(AssistantRuleEntity.RuleType.IN_SCOPE);

        boolean inScope = inScopeRules.stream()
                .map(AssistantRule::keyword)
                .map(String::toLowerCase)
                .anyMatch(normalized::contains);

        if (inScope) {
            logger.debug("User message is in-scope, proceed with request");
            return false;
        }

        List<AssistantRule> outOfScopeRules =
                readAssistantRuleByRuleTypeRepository.readByRuleType(AssistantRuleEntity.RuleType.OUT_OF_SCOPE);

        boolean outOfScope = outOfScopeRules.stream()
                .map(AssistantRule::keyword)
                .map(String::toLowerCase)
                .anyMatch(normalized::contains);

        if (outOfScope) {
            logger.debug("User message is out-of-scope, no chat processed");
            return true;
        }

        // di default blocca tutte le richieste che non sono in scope
        // ma in un futuro potrevve cambiare
        logger.debug("Message was neither in scope nor out-of-scope, handle default");
        return true;
    }
}
