package it.me.web.api.chat;

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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Path("api/chat")
public class ChatResource {

    @Inject
    AskChatService askChatService;

    @Inject
    ChatRequestValidator validator;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<Response> askChat(@Valid ChatRequest chatRequest) {
        if (validator.isMessageOutOfScope(chatRequest.message())) {
            return CompletableFuture.completedStage(
                    Response.status(Response.Status.BAD_REQUEST).entity("""
                    Posso rispondere sola a domande su di me/il mio lavoro/progetti.
                    Prova a chiedermi del mio stack, esperienza o contatti!
                    """).build());
        }
        CompletionStage<String> promise = askChatService.askChat(chatRequest);
        return promise.thenApply(response -> Response.ok(response).build());
    }
}
