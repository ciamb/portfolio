package it.me.web.api;

import it.me.domain.mapper.ContactBackToMessageMapper;
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

    @Inject
    ContactBackToMessageMapper contactBackToMessageMapper;

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
                        contactBackToMessageMapper.apply(contactMe.contactBack())
                ))
                .build();
    }
}
