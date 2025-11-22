package it.me.web.api;

import it.me.domain.service.CvFileUploadService;
import it.me.entity.CvFile;
import it.me.web.dto.request.CvFileUploadRequest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CvFileUploadAdminResourceTest {

    @InjectMocks
    private CvFileUploadAdminResource sut;

    @Mock
    CvFileUploadService cvFileUploadService;

    @Mock
    CvFileUploadRequest cvFileUploadRequest;

    @Test
    void shouldCallCvFileUploadService() {
        // given
        var cvFile = new CvFile().setFilename("cvFile.pdf");
        given(cvFileUploadService.uploadCvFile(any()))
                .willReturn(cvFile);

        // when
        Response result = sut.uploadCv(cvFileUploadRequest);

        // then
        assertThat(result.getStatus()).isEqualTo(201);
        assertSame(result.getEntity(), cvFile);
        verify(cvFileUploadService, times(1)).uploadCvFile(any());

    }
}