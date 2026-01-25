package it.me.domain.mapper;

import it.me.domain.dto.AssistantProfile;
import it.me.domain.service.cv.knowledge.CvKnowledgeProvider;
import it.me.web.dto.request.ChatRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;
import java.util.function.BiFunction;

@ApplicationScoped
public class ChatRequestInputMapper implements BiFunction<ChatRequest, AssistantProfile, String> {

    @Inject
    CvKnowledgeProvider cvKnowledgeProvider;

    @Override
    public String apply(ChatRequest chatRequest, AssistantProfile assistantProfile) {
        String systemPrompt = Optional.ofNullable(assistantProfile)
                .map(AssistantProfile::systemPrompt)
                .filter(sp -> !sp.isBlank())
                .orElseThrow(() -> new IllegalArgumentException("Assistant profile is null"));

        String userMessage = Optional.ofNullable(chatRequest)
                .map(ChatRequest::message)
                .filter(message -> !message.isBlank())
                .orElseThrow(() -> new IllegalArgumentException("Invalid chat message"));

        String cv4Input = cvKnowledgeProvider.getCvFromResources();

        String input = """
                <<<SYSTEM PROMPT>>>
                %s

                <<<CV - FONTE AUTORIZZATA UNICA>>>
                %s

                <<<DOMANDA UTENTE>>>
                %s
                """.formatted(systemPrompt, cv4Input, userMessage);

        return input;
    }
}
