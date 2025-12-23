package it.me.domain.repository.cv.file;

import it.me.domain.dto.CvFile;

import java.util.Optional;

public interface CvFileReadByIsActiveRepository {
    Optional<CvFile> readByIsActive();
}
