package it.me.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

@Getter
@Setter
@Entity
@Table(name = "assistant_rule")
@NamedQueries({@NamedQuery(name = AssistantRuleEntity.READ_ASSISTANT_BY_SCOPE, query = """
            select are from AssistantRuleEntity as are
            where are.ruleType = :ruleType
            and are.enabled = true
            and are.assistantProfile.id = 1
            order by are.priority
        """)})
public class AssistantRuleEntity {
    public static final String READ_ASSISTANT_BY_SCOPE = "AssistantRuleEntity.readRuleByScopeAndAssistantId";

    /**
     * Rule type definition
     */
    public enum RuleType {
        IN_SCOPE,
        OUT_OF_SCOPE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "assistant_id", nullable = false)
    private AssistantProfileEntity assistantProfile;

    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "rule_type", columnDefinition = "rule_type not null")
    private RuleType ruleType;

    @Size(max = 64) @NotNull @Column(name = "keyword", nullable = false, length = 64)
    private String keyword;

    @NotNull @ColumnDefault("1")
    @Column(name = "priority", nullable = false)
    private Integer priority;

    @NotNull @ColumnDefault("true")
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @NotNull @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @NotNull @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
