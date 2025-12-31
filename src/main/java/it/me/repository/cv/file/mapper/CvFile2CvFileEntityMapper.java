package it.me.repository.cv.file.mapper;

import it.me.domain.dto.CvFile;
import it.me.repository.entity.CvFileEntity;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.function.Function;
import org.jboss.logging.Logger;

@ApplicationScoped
public class CvFile2CvFileEntityMapper implements Function<CvFile, CvFileEntity> {
    private final Logger logger = Logger.getLogger(CvFile2CvFileEntityMapper.class);

    @Override
    public CvFileEntity apply(CvFile cvFile) {
        if (cvFile == null) {
            logger.warn("mapping null CvFile");
            return null;
        }

        return new CvFileEntity()
                .setId(cvFile.id())
                .setFilename(cvFile.filename())
                .setContentType(cvFile.contentType())
                .setFileData(cvFile.fileData())
                .setFilesizeBytes(cvFile.filesizeBytes())
                .setSha256(cvFile.sha256())
                .setIsActive(cvFile.isActive())
                .setCreatedAt(cvFile.createdAt())
                .setUpdatedAt(cvFile.updatedAt());
    }
}
