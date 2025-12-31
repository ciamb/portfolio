package it.me.domain.service.cv.file;

import it.me.domain.dto.CvFile;
import it.me.repository.cv.file.CvFileReadByIsActiveRepositoryJpa;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class CvFileDownloadService {
    @Inject
    CvFileReadByIsActiveRepositoryJpa cvFileReadByIsActiveRepository;

    public CvFile downloadActiveCvFile() {
        return cvFileReadByIsActiveRepository
                .readByIsActive()
                .orElseThrow(() -> new NotFoundException("No CvFile active found, please insert a new CV"));
    }
}
