package it.me.repository.assistant.profile;

import it.me.domain.dto.AssistantProfile;
import it.me.domain.repository.assistant.profile.ReadAssistantProfileRepository;
import it.me.repository.assistant.profile.mapper.AssistantProfileEntity2RecordMapper;
import it.me.repository.entity.AssistantProfileEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ReadAssistantProfileRepositoryJpa implements ReadAssistantProfileRepository {

    @Inject
    EntityManager em;

    @Inject
    AssistantProfileEntity2RecordMapper mapper;

    @Transactional
    @Override
    public AssistantProfile readAssistantProfile() {
        return em.createNamedQuery(AssistantProfileEntity.READ_ASSISTANT_PROFILE, AssistantProfileEntity.class)
                .getResultStream()
                .map(mapper)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No assistent profile found"));
    }
}
