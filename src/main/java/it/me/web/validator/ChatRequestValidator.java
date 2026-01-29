package it.me.web.validator;

import static java.util.concurrent.CompletableFuture.completedStage;

import it.me.domain.dto.AssistantRule;
import it.me.domain.service.assistant.rule.cache.AssistantRuleCacheService;
import it.me.repository.entity.AssistantRuleEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ChatRequestValidator {
    private static final Logger logger = Logger.getLogger(ChatRequestValidator.class.getName());

    @Inject
    AssistantRuleCacheService assistantRuleCacheService;

    public CompletionStage<Boolean> isUserMessageInScope(String userMessage) {
        String normalized =
                Optional.ofNullable(userMessage).map(String::toLowerCase).orElse("");

        if (normalized.isBlank()) {
            logger.debug("Empty message -> out-of-scope");
            return completedStage(false);
        }

        return assistantRuleCacheService
                .getAssistantRuleByRuleType(AssistantRuleEntity.RuleType.OUT_OF_SCOPE)
                .thenApply(rules -> isUserMessageContainKeyword(rules, normalized))
                .thenCompose(isOutOfScope -> {
                    if (isOutOfScope) {
                        logger.debug("User message is out-of-scope, no chat processed");
                        return completedStage(false);
                    }

                    return assistantRuleCacheService
                            .getAssistantRuleByRuleType(AssistantRuleEntity.RuleType.IN_SCOPE)
                            .thenApply(rules -> isUserMessageContainKeyword(rules, normalized))
                            .thenApply(isInScope -> {
                                if (isInScope) {
                                    logger.debug("User message is in-scope, proceed with request");
                                    return true;
                                }
                                logger.debug("No rules matching -> block request by default");
                                return false;
                            });
                });
    }

    private static boolean isUserMessageContainKeyword(List<AssistantRule> assistantRules, String userMessage) {
        if (assistantRules == null || assistantRules.isEmpty()) {
            return false;
        }
        return assistantRules.stream()
                .map(AssistantRule::keyword)
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .anyMatch(userMessage::contains);
    }
}
