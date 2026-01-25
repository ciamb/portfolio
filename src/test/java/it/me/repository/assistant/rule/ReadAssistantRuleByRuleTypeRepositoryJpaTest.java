package it.me.repository.assistant.rule;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

import it.me.domain.dto.AssistantRule;
import it.me.repository.assistant.rule.mapper.AssistantRuleEntity2RecordMapper;
import it.me.repository.entity.AssistantRuleEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class})
class ReadAssistantRuleByRuleTypeRepositoryJpaTest {

    @Mock
    AssistantRuleEntity2RecordMapper mapper;

    @Mock
    EntityManager em;

    @InjectMocks
    private ReadAssistantRuleByRuleTypeRepositoryJpa sut;

    @Mock
    TypedQuery<AssistantRuleEntity> query;

    @Test
    void shouldCallEmAndReturn() {
        // given
        AssistantRuleEntity entity = new AssistantRuleEntity();

        given(em.createNamedQuery(eq(AssistantRuleEntity.READ_ASSISTANT_BY_SCOPE), eq(AssistantRuleEntity.class)))
                .willReturn(query);
        given(query.setParameter(eq("ruleType"), any(AssistantRuleEntity.RuleType.class)))
                .willReturn(query);
        given(query.getResultStream()).willReturn(Stream.of(entity));
        given(mapper.apply(any(AssistantRuleEntity.class)))
                .willReturn(AssistantRule.builder().build());
        // when
        List<AssistantRule> res = sut.readByRuleType(AssistantRuleEntity.RuleType.IN_SCOPE);

        // then
        assertNotNull(res);
        var inOrder = Mockito.inOrder(em, mapper);
        inOrder.verify(em)
                .createNamedQuery(eq(AssistantRuleEntity.READ_ASSISTANT_BY_SCOPE), eq(AssistantRuleEntity.class));
        inOrder.verify(mapper, times(1)).apply(entity);
    }
}
