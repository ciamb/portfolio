package it.me.repository.assistant.rule;

import it.me.domain.dto.AssistantRule;
import it.me.domain.repository.assistant.rule.ReadAssistantRuleByRuleTypeRepository;
import it.me.repository.assistant.rule.mapper.AssistantRuleEntity2RecordMapper;
import it.me.repository.entity.AssistantRuleEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ReadAssistantRuleByRuleTypeRepositoryJpa implements ReadAssistantRuleByRuleTypeRepository {
    private static final Logger logger = Logger.getLogger(ReadAssistantRuleByRuleTypeRepositoryJpa.class.getName());

    @Inject
    AssistantRuleEntity2RecordMapper mapper;

    @Inject
    EntityManager em;

    @Override
    public List<AssistantRule> readByRuleType(AssistantRuleEntity.RuleType ruleType) {
        logger.infof("Reading assistant rule with type %s", ruleType);
        return em.createNamedQuery(AssistantRuleEntity.READ_ASSISTANT_BY_SCOPE, AssistantRuleEntity.class)
                .setParameter("ruleType", ruleType)
                .getResultStream()
                .map(mapper)
                .toList();
    }
}
