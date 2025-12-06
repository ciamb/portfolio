package it.me.web.api.contact.me;

import it.me.domain.mapper.ContactBackToMessageMapper;
import it.me.domain.service.contact.me.ContactMeCreateService;
import it.me.web.dto.request.ContactMeRequest;
import it.me.web.dto.response.ContactMeResponse;
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
        var contactMeResponse = new ContactMeResponse(
                contactMe.id(),
                contactMe.status(),
                contactBackToMessageMapper.apply(contactMe.contactBack())
        );

        return Response.status(Response.Status.CREATED)
                .entity(contactMeResponse)
                .build();
    }
}
