package it.me.web.api.chat;

import static java.util.concurrent.CompletableFuture.completedStage;

import it.me.domain.service.chat.AskChatService;
import it.me.web.dto.request.ChatRequest;
import it.me.web.validator.ChatRequestValidator;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.concurrent.CompletionStage;

@Path("api/chat")
public class ChatResource {

    // TEXT
    public static final String FALLBACK_MSG = """
            Posso rispondere sola a domande sul mio lavoro/progetti.
            Prova a chiedermi del mio stack, esperienza o contatti!
            """;

    @Inject
    AskChatService askChatService;

    @Inject
    ChatRequestValidator validator;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<Response> askChat(@Valid ChatRequest chatRequest) {
        return validator.isUserMessageInScope(chatRequest.message()).thenCompose(isInScope -> {
            if (!isInScope) {
                return completedStage(Response.status(Response.Status.BAD_REQUEST)
                        .entity(FALLBACK_MSG)
                        .build());
            }
            return askChatService.askChat(chatRequest).thenApply(response -> Response.status(Response.Status.OK)
                    .entity(response)
                    .build());
        });
    }
}
