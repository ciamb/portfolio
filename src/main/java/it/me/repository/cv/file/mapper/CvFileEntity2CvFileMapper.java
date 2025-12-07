package it.me.repository.cv.file.mapper;

import it.me.domain.dto.CvFile;
import it.me.repository.entity.CvFileEntity;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.function.Function;

@ApplicationScoped
public class CvFileEntity2CvFileMapper implements Function<CvFileEntity, CvFile> {
    private final Logger logger = Logger.getLogger(CvFileEntity2CvFileMapper.class.getName());

    @Override
    public CvFile apply(CvFileEntity cvFileEntity) {
        if (cvFileEntity == null) {
            logger.warnf("mapping null CvFileEntity");
            return null;
        }

        return CvFile.builder()
                .id(cvFileEntity.id())
                .filename(cvFileEntity.filename())
                .contentType(cvFileEntity.contentType())
                .fileData(cvFileEntity.fileData())
                .filesizeBytes(cvFileEntity.filesizeBytes())
                .sha256(cvFileEntity.sha256())
                .isActive(cvFileEntity.isActive())
                .createdAt(cvFileEntity.createdAt())
                .updatedAt(cvFileEntity.updatedAt())
                .build();
    }
}
