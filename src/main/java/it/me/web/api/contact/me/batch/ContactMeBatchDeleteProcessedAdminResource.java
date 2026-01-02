package it.me.web.api.contact.me.batch;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import it.me.domain.dto.ProcessedInfo;
import it.me.domain.service.contact.me.ContactMeDeleteAllProcessedService;
import jakarta.inject.Inject;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("/api/admin/contact-me/batch")
public class ContactMeBatchDeleteProcessedAdminResource {
    private final Logger logger = Logger.getLogger(ContactMeBatchDeleteProcessedAdminResource.class.getName());

    @Inject
    ContactMeDeleteAllProcessedService contactMeDeleteAllProcessedService;

    @Path("/execute-delete")
    @PUT
    @Produces(APPLICATION_JSON)
    public Response executeDelete() {
        logger.warning("Deleting all PROCESSED contact me data, this is irrevertible");
        ProcessedInfo deletedInfo = contactMeDeleteAllProcessedService.deleteAllProcessed();
        return Response.ok().entity(deletedInfo).build();
    }
}
