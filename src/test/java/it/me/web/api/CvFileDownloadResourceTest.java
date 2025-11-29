package it.me.web.api;

import it.me.domain.mapper.FilenameDefaultMapper;
import it.me.domain.service.CvFileDownloadService;
import it.me.entity.CvFile;
import jakarta.ws.rs.core.EntityTag;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CvFileDownloadResourceTest {

    @InjectMocks
    private CvFileDownloadResource sut;

    @Mock
    FilenameDefaultMapper filenameDefaultMapper;

    @Mock
    CvFileDownloadService cvFileDownloadService;

    @Mock
    Request request;

    private CvFile cvFile;

    @BeforeEach
    void setUp() {
        cvFile = new CvFile()
                .setFilename("Andrea_CV")
                .setContentType("application/pdf")
                .setFileData("PDF".getBytes())
                .setSha256("abc123")
                .setIsActive(true)
                .setFilesizeBytes(3L);
    }

    @Test
    void shouldDownloadCvFile() {
        // given
        given(cvFileDownloadService.downloadActiveCvFile()).willReturn(cvFile);
        given(request.evaluatePreconditions(any(EntityTag.class))).willReturn(null);
        given(filenameDefaultMapper.apply(eq(cvFile.filename()))).willReturn("Andrea_CV.pdf");

        // when
        Response result = sut.downloadCv(request);

        // then
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getHeaderString(HttpHeaders.CONTENT_TYPE))
                .isEqualTo("application/pdf");
        assertThat(result.getHeaderString(HttpHeaders.CONTENT_DISPOSITION))
                .isEqualTo("attachment; filename=\"Andrea_CV.pdf\"");
        assertThat(result.getHeaderString(HttpHeaders.CACHE_CONTROL))
                .isEqualTo("private, no-cache");
        assertThat(result.getHeaderString(HttpHeaders.CONTENT_LENGTH))
                .isEqualTo("3");

        // entity tag captor
        ArgumentCaptor<EntityTag> entityTag = ArgumentCaptor.forClass(EntityTag.class);
        verify(request, times(1)).evaluatePreconditions(entityTag.capture());
        assertThat(entityTag.getValue().getValue()).isEqualTo("abc123");
        verify(cvFileDownloadService, times(1)).downloadActiveCvFile();
        verify(filenameDefaultMapper, times(1)).apply("Andrea_CV");
    }

}