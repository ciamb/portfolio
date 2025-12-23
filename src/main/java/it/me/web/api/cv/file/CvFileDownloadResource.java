package it.me.web.api.cv.file;

import it.me.domain.dto.CvFile;
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
        CvFile cvFile = cvFileDownloadService.downloadActiveCvFile();

        EntityTag entityTag = new EntityTag(cvFile.sha256());
        Response.ResponseBuilder preConditions = request.evaluatePreconditions(entityTag);
        if (preConditions != null) {
            return preConditions.build();
        }

        var filename = filenameDefaultMapper.apply(cvFile.filename());
        Response.ResponseBuilder responseBuilder = Response.ok(cvFile.fileData(), cvFile.contentType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .header(HttpHeaders.CACHE_CONTROL, "private, no-cache")
                .tag(entityTag);

        if (cvFile.filesizeBytes() != null) {
            responseBuilder.header(HttpHeaders.CONTENT_LENGTH, cvFile.filesizeBytes());
        }

        return responseBuilder.build();
    }
}
