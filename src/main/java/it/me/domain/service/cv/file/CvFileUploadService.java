package it.me.domain.service.cv.file;

import it.me.domain.dto.CvFile;
import it.me.domain.mapper.FileDataToSha256Mapper;
import it.me.domain.repository.cv.file.CvFilePersistRepository;
import it.me.domain.repository.cv.file.CvFileReadBySha256Repository;
import it.me.domain.repository.cv.file.CvFileUpdateAllIsActiveFalseRepository;
import it.me.repository.cv.file.CvFileUpdateAllIsActiveToFalseRepositoryJpa;
import it.me.web.dto.request.CvFileUploadRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.time.ZonedDateTime;

@ApplicationScoped
public class CvFileUploadService {
    private static final Logger logger = Logger.getLogger(CvFileUploadService.class.getName());

    @Inject
    CvFileReadBySha256Repository cvFileReadBySha256Repository;

    @Inject
    CvFileUpdateAllIsActiveFalseRepository cvFileUpdateAllIsActiveFalseRepository;

    @Inject
    CvFilePersistRepository cvFilePersistRepository;

    @Inject
    FileDataToSha256Mapper fileDataToSha256Mapper;

    public CvFile uploadCvFile(CvFileUploadRequest cvFileUploadRequest) {
        if (cvFileUploadRequest == null) {
            throw new IllegalArgumentException("Upload request is null");
        }

        if (cvFileUploadRequest.fileData() == null || cvFileUploadRequest.fileData().length == 0) {
            throw new IllegalArgumentException("fileData is null or empty");
        }

        if (cvFileUploadRequest.fileData().length > (10L * 1024 * 1024)) {
            throw new IllegalArgumentException("fileData is too large (max 10MB)");
        }

        var contentType = cvFileUploadRequest.contentType() == null
                ? "application/pdf"
                : cvFileUploadRequest.contentType();
        if (!contentType.toLowerCase().startsWith("application/pdf")) {
            throw new IllegalArgumentException("Invalid content type, only application/pdf is supported");
        }

        if (cvFileUploadRequest.filename() == null || cvFileUploadRequest.filename().isBlank()) {
            throw new IllegalArgumentException("filename is null or blank");
        }

        var sha256 = fileDataToSha256Mapper.apply(cvFileUploadRequest.fileData());
        CvFile exist = cvFileReadBySha256Repository.readBySha256(sha256)
                .orElse(null);
        if (exist != null) {
            throw new IllegalStateException("This CV already exists (sha256 duplicated)");
        }

        logger.info("Set every other CV with is_active = true to false");
        cvFileUpdateAllIsActiveFalseRepository.updateIsActiveToFalseIfAny();

        CvFile cvFile = CvFile.builder()
                .filename(cvFileUploadRequest.filename())
                .contentType(contentType)
                .fileData(cvFileUploadRequest.fileData())
                .sha256(sha256)
                .isActive(true)
                .createdAt(ZonedDateTime.now())
                .updatedAt(ZonedDateTime.now())
                .build();

        logger.infof("Persisting new CvFile filename=%s", cvFile.filename());
        return cvFilePersistRepository.persist(cvFile);
    }
}
