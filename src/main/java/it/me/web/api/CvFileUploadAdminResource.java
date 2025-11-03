package it.me.web.api;

import it.me.domain.CvFileUploadService;
import it.me.entity.CvFile;
import it.me.web.dto.CvFileUploadRequest;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/admin/cv")
public class CvFileUploadAdminResource {

    @Inject
    CvFileUploadService cvFileUploadService;

    @POST
    @Path("upload/json")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadCv(CvFileUploadRequest cvFileUploadRequest) {
        CvFile persistedCvFile = cvFileUploadService.uploadCvFile(cvFileUploadRequest);
        return Response.status(Response.Status.CREATED).entity(persistedCvFile).build();
    }
}
