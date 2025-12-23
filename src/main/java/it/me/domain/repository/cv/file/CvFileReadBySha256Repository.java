package it.me.domain.repository.cv.file;

import it.me.domain.dto.CvFile;

import java.util.Optional;

public interface CvFileReadBySha256Repository {
    Optional<CvFile> readBySha256(String sha256);
}
