package it.me.domain.service;

import it.me.domain.mapper.FileDataToSha256Mapper;
import it.me.entity.CvFile;
import it.me.repository.CvFilePersistRepository;
import it.me.repository.CvFileReadBySha256Repository;
import it.me.repository.CvFileUpdateIsActiveToFalseIfAnyRepository;
import it.me.web.dto.request.CvFileUploadRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.time.ZonedDateTime;

@ApplicationScoped
public class CvFileUploadService {
    private static final Logger logger = Logger.getLogger(CvFileUploadService.class.getName());

    @Inject
    CvFileReadBySha256Repository cvFileReadBySha256Repository;

    @Inject
    CvFileUpdateIsActiveToFalseIfAnyRepository cvFileUpdateIsActiveToFalseIfAnyRepository;

    @Inject
    CvFilePersistRepository cvFilePersistRepository;

    @Inject
    FileDataToSha256Mapper fileDataToSha256Mapper;

    @Transactional
    public CvFile uploadCvFile(CvFileUploadRequest cvFileUploadRequest) {
        if (cvFileUploadRequest == null) {
            throw new IllegalArgumentException("cvFileUploadRequest is null");
        }

        if (cvFileUploadRequest.fileData() == null || cvFileUploadRequest.fileData().length == 0) {
            throw new IllegalArgumentException("fileData is null or empty");
        }

        if (cvFileUploadRequest.fileData().length > (10L * 1024 * 1024)) {
            throw new IllegalArgumentException("fileData is too large (max 10MB)");
        }

        var contentType = cvFileUploadRequest.contentType() == null
                ? "application/pdf" : cvFileUploadRequest.contentType();
        if (!contentType.toLowerCase().startsWith("application/pdf")) {
            throw new IllegalArgumentException("Invalid content type, only application/pdf is supported");
        }

        if (cvFileUploadRequest.filename() == null || cvFileUploadRequest.filename().isBlank()) {
            throw new IllegalArgumentException("filename is null or blank");
        }

        var sha256 = fileDataToSha256Mapper.apply(cvFileUploadRequest.fileData());
        CvFile cvFile = cvFileReadBySha256Repository.readBySha256(sha256).orElse(null);
        if (cvFile != null) {
            throw new IllegalStateException("This CV already exists (sha256 duplicated)");
        }

        logger.info("Set every CV with is_active = true to false");
        cvFileUpdateIsActiveToFalseIfAnyRepository.updateIsActiveToFalseIfAny();

        CvFile cvFileToPersist = new CvFile()
                .setFilename(cvFileUploadRequest.filename())
                .setContentType(contentType)
                .setFileData(cvFileUploadRequest.fileData())
                .setSha256(sha256)
                .setActive(true)
                .setCreatedAt(ZonedDateTime.now())
                .setUpdatedAt(ZonedDateTime.now());

        logger.infof("Persisting new CvFile filename=%s", cvFileToPersist.filename());
        return cvFilePersistRepository.persistCvFile(cvFileToPersist);
    }
}
