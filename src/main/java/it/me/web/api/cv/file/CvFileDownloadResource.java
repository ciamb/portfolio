package it.me.web.api.cv.file;

import it.me.domain.mapper.FilenameDefaultMapper;
import it.me.domain.service.cv.file.CvFileDownloadService;
import it.me.repository.entity.CvFileEntity;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.*;

@Path("/api/cv")
public class CvFileDownloadResource {

    @Inject
    FilenameDefaultMapper filenameDefaultMapper;

    @Inject
    CvFileDownloadService cvFileDownloadService;

    @GET
    @Path("/download")
    @Produces("application/pdf")
    public Response downloadCv(@Context Request request) {
        CvFileEntity cvFileEntity = cvFileDownloadService.downloadActiveCvFile();

        EntityTag entityTag = new EntityTag(cvFileEntity.sha256());
        Response.ResponseBuilder preConditions = request.evaluatePreconditions(entityTag);
        if (preConditions != null) {
            return preConditions.build();
        }

        var filename = filenameDefaultMapper.apply(cvFileEntity.filename());
        Response.ResponseBuilder responseBuilder = Response.ok(cvFileEntity.fileData(), cvFileEntity.contentType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .header(HttpHeaders.CACHE_CONTROL, "private, no-cache")
                .tag(entityTag);

        if (cvFileEntity.filesizeBytes() != null) {
            responseBuilder.header(HttpHeaders.CONTENT_LENGTH, cvFileEntity.filesizeBytes());
        }

        return responseBuilder.build();
    }
}
