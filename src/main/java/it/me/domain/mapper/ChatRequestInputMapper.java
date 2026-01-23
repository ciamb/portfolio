package it.me.domain.mapper;

import it.me.domain.PortfolioPublicK;
import it.me.domain.service.cv.knowledge.CvKnowledgeProvider;
import it.me.web.dto.request.ChatRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;
import java.util.function.Function;

@ApplicationScoped
public class ChatRequestInputMapper implements Function<ChatRequest, String> {

    @Inject
    CvKnowledgeProvider cvKnowledgeProvider;

    @Override
    public String apply(ChatRequest chatRequest) {
        String userMessage = Optional.ofNullable(chatRequest)
                .map(cr -> Optional.ofNullable(cr.message())
                        .filter(message -> !message.isEmpty())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid chat message")))
                .orElseThrow(() -> new IllegalArgumentException("chatRequest is null"));

        String cv4Input = cvKnowledgeProvider.getCvFromResources();

        String input = """
                %s

                ### CV / PROFILO UFFICIALE (fonte unica)
                %s

                ### DOMANDA UTENTE
                %s
                """.formatted(PortfolioPublicK.OpenAi.SYSTEM_PROMPT, cv4Input, userMessage);

        return input;
    }
}
