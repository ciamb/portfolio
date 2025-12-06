package it.me.domain.service.cv.file;

import it.me.repository.entity.CvFileEntity;
import it.me.repository.cv.file.CvFileReadByIsActiveRepository;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CvFileDownloadServiceTest {

    @InjectMocks
    private CvFileDownloadService sut;

    @Mock
    CvFileReadByIsActiveRepository cvFileReadByIsActiveRepository;

    @Test
    void downloadActiveFile() {
        //given
        given(cvFileReadByIsActiveRepository.readByIsActive())
                .willReturn(Optional.of(new CvFileEntity()));

        //when
        CvFileEntity result = assertDoesNotThrow(() -> sut.downloadActiveCvFile());

        // then
        assertNotNull(result);
        verify(cvFileReadByIsActiveRepository, times(1)).readByIsActive();
    }

    @Test
    void downloadActiveFile_shouldThrowNotFoundException() {
        //given
        given(cvFileReadByIsActiveRepository.readByIsActive())
                .willReturn(Optional.empty());

        //when
        NotFoundException nfe = assertThrows(NotFoundException.class, () -> sut.downloadActiveCvFile());

        // then
        assertNotNull(nfe);
        assertThat(nfe.getMessage()).contains("No CvFile active found");
        verify(cvFileReadByIsActiveRepository, times(1)).readByIsActive();
    }
}