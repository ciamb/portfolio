package it.me.repository.assistant.profile.mapper;

import it.me.domain.dto.AssistantProfile;
import it.me.repository.entity.AssistantProfileEntity;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.function.Function;
import org.jboss.logging.Logger;

@ApplicationScoped
public class AssistantProfileEntity2RecordMapper implements Function<AssistantProfileEntity, AssistantProfile> {
    private static final Logger logger = Logger.getLogger(AssistantProfileEntity2RecordMapper.class);

    @Override
    public AssistantProfile apply(AssistantProfileEntity entity) {
        if (entity == null) {
            logger.warn("assistant profile entity is null, return null");
            return null;
        }

        return AssistantProfile.builder()
                .id(entity.getId())
                .name(entity.getName())
                .enabled(entity.getEnabled())
                .systemPrompt(entity.getSystemPrompt())
                .fallbackMessage(entity.getFallbackMessage())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
