package it.me.web.api.cv.file;

import it.me.domain.dto.CvFile;
import it.me.domain.service.cv.file.CvFileUploadService;
import it.me.repository.entity.CvFileEntity;
import it.me.web.dto.request.CvFileUploadRequest;
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
    @Path("/upload/json")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadCv(@Valid CvFileUploadRequest cvFileUploadRequest) {
        CvFile persistedCvFile = cvFileUploadService.uploadCvFile(cvFileUploadRequest);
        return Response.status(Response.Status.CREATED)
                .entity(persistedCvFile)
                .build();
    }
}
