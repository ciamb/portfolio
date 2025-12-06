package it.me.domain.service.cv.file;

import it.me.repository.entity.CvFileEntity;
import it.me.repository.cv.file.CvFileReadByIsActiveRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class CvFileDownloadService {
    @Inject
    CvFileReadByIsActiveRepository cvFileReadByIsActiveRepository;

    public CvFileEntity downloadActiveCvFile() {
        return cvFileReadByIsActiveRepository.readByIsActive()
                .orElseThrow(() -> new NotFoundException("No CvFile active found, please insert a new CV"));
    }
}
