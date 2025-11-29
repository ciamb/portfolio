package it.me.web.api;

import it.me.domain.service.ContactMeBatchManagerService;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/admin/contact-me/batch")
public class ContactMeBatchExecuteAdminResource {

    @Inject
    ContactMeBatchManagerService contactMeBatchManagerService;

    @Path("/execute")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response executeBatch() {
        var contactMeBatchResponse = contactMeBatchManagerService.executeBatch();
        return Response.ok(contactMeBatchResponse).build();
    }
}
