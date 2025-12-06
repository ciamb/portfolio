package it.me.web.api.contact.me.batch.config;

import it.me.domain.service.contact.me.batch.config.ContactMeBatchConfigUpdateIsActiveService;
import it.me.web.dto.request.ContactMeBatchConfigUpdateRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/admin/contact-me/batch-config")
public class ContactMeBatchConfigUpdateAdminResource {

    @Inject
    ContactMeBatchConfigUpdateIsActiveService contactMeBatchConfigUpdateIsActiveService;

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateContactMeBatchConfig(
            ContactMeBatchConfigUpdateRequest contactMeBatchConfigUpdateRequest) {
        var isActive = contactMeBatchConfigUpdateIsActiveService
                .updateContactMeBatchConfig(contactMeBatchConfigUpdateRequest);
        return Response.ok(isActive ? "Batch Contact Me Execution: true" : "Batch Contact Me execution: false")
                .build();
    }
}
