package it.me.repository.assistant.rule.mapper;

import it.me.domain.dto.AssistantRule;
import it.me.repository.assistant.profile.mapper.AssistantProfileEntity2RecordMapper;
import it.me.repository.entity.AssistantRuleEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.function.Function;
import org.jboss.logging.Logger;

@ApplicationScoped
public class AssistantRuleEntity2RecordMapper implements Function<AssistantRuleEntity, AssistantRule> {
    private static final Logger log = Logger.getLogger(AssistantProfileEntity2RecordMapper.class.getName());

    @Inject
    AssistantProfileEntity2RecordMapper assistantProfileEntity2RecordMapper;

    @Override
    public AssistantRule apply(AssistantRuleEntity entity) {
        if (entity == null) {
            log.warn("assistant rule entity is null, return null");
            return null;
        }

        return AssistantRule.builder()
                .id(entity.getId())
                .assistantProfile(assistantProfileEntity2RecordMapper.apply(entity.getAssistantProfile()))
                .ruleType(entity.getRuleType())
                .keyword(entity.getKeyword())
                .priority(entity.getPriority())
                .enabled(entity.getEnabled())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
