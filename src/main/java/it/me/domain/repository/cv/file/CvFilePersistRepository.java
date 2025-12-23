package it.me.domain.repository.cv.file;

import it.me.domain.dto.CvFile;

public interface CvFilePersistRepository {
    CvFile persist(CvFile cvFile);
}
