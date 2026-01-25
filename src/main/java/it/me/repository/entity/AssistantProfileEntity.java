package it.me.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "assistant_profile")
@NamedQueries({@NamedQuery(name = AssistantProfileEntity.READ_ASSISTANT_PROFILE, query = """
                    select ape from AssistantProfileEntity as ape
                    where ape.id = 1
                    and ape.enabled = true
                """)})
public class AssistantProfileEntity {
    public static final String READ_ASSISTANT_PROFILE = "AssistantProfileEntity.readAssistantProfile";

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 20) @NotNull @Column(name = "name", nullable = false, length = 20)
    private String name;

    @NotNull @Column(name = "system_prompt", nullable = false, length = Integer.MAX_VALUE)
    private String systemPrompt;

    @NotNull @Column(name = "fallback_message", nullable = false, length = Integer.MAX_VALUE)
    private String fallbackMessage;

    @NotNull @ColumnDefault("true")
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false;

    @NotNull @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @NotNull @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
