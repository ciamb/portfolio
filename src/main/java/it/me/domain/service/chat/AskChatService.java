package it.me.domain.service.chat;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import it.me.domain.mapper.ChatRequestInputMapper;
import it.me.domain.repository.assistant.profile.ReadAssistantProfileRepository;
import it.me.web.dto.request.ChatRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.concurrent.CompletionStage;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.jboss.logging.Logger;

@ApplicationScoped
public class AskChatService {
    private static final Logger logger = Logger.getLogger(AskChatService.class.getName());

    private final OpenAIClient openAIClient;

    @Inject
    ManagedExecutor executor;

    @Inject
    ChatRequestInputMapper chatRequestInputMapper;

    @Inject
    ReadAssistantProfileRepository readAssistantProfileRepository;

    @ConfigProperty(name = "openai.model")
    String model;

    @Inject
    public AskChatService(@ConfigProperty(name = "openai.api-key") String apiKey) {
        this.openAIClient = OpenAIOkHttpClient.builder().apiKey(apiKey).build();
    }

    public CompletionStage<String> askChat(ChatRequest chatRequest) {
        logger.infof("Ask to %s chat request: %s", model, chatRequest.message());

        var promise = supplyAsync(() -> readAssistantProfileRepository.readAssistantProfile(), executor)
                .exceptionally(ex -> {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    logger.errorf(
                            "Error while loading Assistent Profile (%s): %s",
                            cause.getClass().getSimpleName(), cause.getMessage());
                    return null;
                });

        return promise.thenCompose(assistantProfile -> {
            if (assistantProfile == null) {
                return completedFuture("""
                            L'Assistente non e stato caricato correttamente.
                            Mi scuso per il disagio.
                            Riprova piu tardi!
                        """);
            }

            var params = ResponseCreateParams.builder()
                    .input(chatRequestInputMapper.apply(chatRequest, assistantProfile))
                    .model(model)
                    .build();

            return supplyAsync(() -> openAIClient.responses().create(params))
                    .thenApply(this::extractOutput)
                    .exceptionally(ex -> {
                        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                        logger.errorf("OpenAi error (%s): %s", cause.getClass().getSimpleName(), cause.getMessage());

                        return (assistantProfile.fallbackMessage() != null
                                        && !assistantProfile.fallbackMessage().isEmpty())
                                ? assistantProfile.fallbackMessage()
                                : "Questa informazione non è disponibile.";
                    });
        });
    }

    private String extractOutput(Response response) {
        return response.output().stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .map(content -> content.asOutputText().text())
                .findFirst()
                .orElse("Questa informazione non è disponibile.");
    }
}
