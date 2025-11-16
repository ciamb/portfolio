package it.me.web.api;

import it.me.domain.service.ContactMeCreateService;
import it.me.web.dto.ContactMeRequest;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/contact-me")
public class ContactMeCreateResource {

    @Inject
    ContactMeCreateService contactMeCreateService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response contactMe(@Valid ContactMeRequest contactMeRequest) {
        var contactMe = contactMeCreateService.createContactMe(contactMeRequest);
        return Response.status(Response.Status.CREATED)
                .entity("""
                        {
                          "id":"%s",
                          "status":"%s",
                          "message": "%s"
                        }
                        """.formatted(
                        contactMe.id(),
                        contactMe.status(),
                        mapContactBackMessage(contactMe.contactBack())
                ))
                .build();
    }

    private String mapContactBackMessage(boolean contactBack) {
        return contactBack ?
                """
                        Grazie per aver registrato un messaggio.
                        Il messaggio sar\u00E0 processato durante il fine settimana.
                        Ci sentiamo presto!
                        """
                :
                """
                        Grazie per aver registrato un messaggio per me.
                        Ti auguro una buona giornata!
                        """;
    }
}
