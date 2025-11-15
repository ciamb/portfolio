package it.me.domain.service;

import it.me.entity.CvFile;
import it.me.repository.CvFileReadByIsActiveRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class CvFileDownloadService {
    @Inject
    CvFileReadByIsActiveRepository cvFileReadByIsActiveRepository;

    public CvFile downloadActiveCvFile() {
        return cvFileReadByIsActiveRepository.readByIsActive()
                .orElseThrow(() -> new NotFoundException("No CvFile active found, please insert a new CV"));
    }
}
