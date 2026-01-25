package it.me.web.validator;

import static java.util.concurrent.CompletableFuture.completedStage;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import it.me.domain.dto.AssistantRule;
import it.me.domain.repository.assistant.rule.ReadAssistantRuleByRuleTypeRepository;
import it.me.repository.entity.AssistantRuleEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ChatRequestValidator {
    private static final Logger logger = Logger.getLogger(ChatRequestValidator.class.getName());

    @Inject
    ManagedExecutor executor;

    @Inject
    ReadAssistantRuleByRuleTypeRepository readAssistantRuleByRuleTypeRepository;

    public CompletionStage<Boolean> isMessageOutOfScope(String userMessage) {
        String normalized =
                Optional.ofNullable(userMessage).map(String::toLowerCase).orElse("");

        if (normalized.isBlank()) {
            logger.debug("Empty message -> out-of-scope");
            return completedStage(true);
        }

        return readAssistantRulesAsync(AssistantRuleEntity.RuleType.OUT_OF_SCOPE)
                .thenApply(rules -> matchRule(rules, normalized))
                .thenCompose(isOutOfScope -> {
                    if (isOutOfScope) {
                        logger.debug("User message is out-of-scope, no chat processed");
                        return completedStage(true);
                    }

                    return readAssistantRulesAsync(AssistantRuleEntity.RuleType.IN_SCOPE)
                            .thenApply(rules -> matchRule(rules, normalized))
                            .thenApply(isInScope -> {
                                if (isInScope) {
                                    logger.debug("User message is in-scope, proceed with request");
                                    return false;
                                }
                                logger.debug("No rules matching -> block request by default");
                                return true;
                            });
                });
    }

    private CompletionStage<List<AssistantRule>> readAssistantRulesAsync(AssistantRuleEntity.RuleType ruleType) {
        return supplyAsync(() -> readAssistantRuleByRuleTypeRepository.readByRuleType(ruleType), executor)
                .exceptionally(ex -> {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    logger.errorf(
                            "Error while loading Assistent Rule %s (%s): %s",
                            ruleType, cause.getClass().getSimpleName(), cause.getMessage());
                    return List.of();
                });
    }

    private static boolean matchRule(List<AssistantRule> assistantRules, String message) {
        if (assistantRules == null || assistantRules.isEmpty()) {
            return false;
        }
        return assistantRules.stream()
                .map(AssistantRule::keyword)
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .anyMatch(message::contains);
    }
}
