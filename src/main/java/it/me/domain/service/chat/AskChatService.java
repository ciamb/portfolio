package it.me.domain.service.chat;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import it.me.domain.mapper.ChatRequestInputMapper;
import it.me.web.dto.request.ChatRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public class AskChatService {
    private static final Logger logger = Logger.getLogger(AskChatService.class.getName());

    private final OpenAIClient openAIClient;

    @Inject
    ChatRequestInputMapper chatRequestInputMapper;

    @ConfigProperty(name = "openai.model")
    String model;

    @Inject
    public AskChatService(@ConfigProperty(name = "openai.api-key") String apiKey) {
        this.openAIClient = OpenAIOkHttpClient.builder().apiKey(apiKey).build();
    }

    public CompletionStage<String> askChat(ChatRequest chatRequest) {
        ResponseCreateParams params = ResponseCreateParams.builder()
                .input(chatRequestInputMapper.apply(chatRequest))
                .model(model)
                .build();

        logger.infof("Ask to %s chat request: %s", model, chatRequest.message());

        return CompletableFuture.supplyAsync(() -> openAIClient.responses().create(params))
                .thenApply(this::extractOutput)
                .exceptionally(ex -> {
                    logger.errorf(
                            "Openai error (%s): %s", ex.getCause().getClass().getSimpleName(), ex.getMessage());
                    return "Errore cIAmb in fase di risposta, riprova piu tardi!";
                });
    }

    private String extractOutput(Response response) {
        return response.output().stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .map(content -> content.asOutputText().text())
                .findFirst()
                .orElse("Nessun messagio in risposta da cIAmb");
    }
}
