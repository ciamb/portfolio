package it.me.domain.service.cv.file;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import it.me.domain.dto.CvFile;
import it.me.domain.mapper.FileDataToSha256Mapper;
import it.me.domain.repository.cv.file.CvFileDeleteOldRepository;
import it.me.domain.repository.cv.file.CvFilePersistRepository;
import it.me.domain.repository.cv.file.CvFileReadBySha256Repository;
import it.me.domain.repository.cv.file.CvFileUpdateAllIsActiveFalseRepository;
import it.me.web.dto.request.CvFileUploadRequest;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CvFileUploadServiceTest {

    @InjectMocks
    private CvFileUploadService sut;

    @Mock
    CvFileReadBySha256Repository cvFileReadBySha256Repository;

    @Mock
    CvFileUpdateAllIsActiveFalseRepository cvFileUpdateAllIsActiveFalseRepository;

    @Mock
    CvFilePersistRepository cvFilePersistRepository;

    @Mock
    FileDataToSha256Mapper fileDataToSha256Mapper;

    @Mock
    CvFileDeleteOldRepository cvFileDeleteOldRepository;

    @Test
    @DisplayName("1. Upload should throw exceptin because request is null")
    void uploadCvFile_shouldThrowIllegalArgumentException_whenRequestIsNull() {
        // given
        // when
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> sut.uploadCvFile(null));

        // then
        assertThat(iae.getMessage()).contains("Upload request is null");
    }

    @Test
    @DisplayName("2. Upload should throw exception because filaData is null")
    void uploadCvFile_shouldThrowIllegalArgumentException_whenFilaDataIsNull() {
        // given
        var cvFileUploadRequest = new CvFileUploadRequest(null, "name", "application/pdf");

        // when
        IllegalArgumentException iae =
                assertThrows(IllegalArgumentException.class, () -> sut.uploadCvFile(cvFileUploadRequest));

        // then
        assertThat(iae.getMessage()).contains("fileData is null");
    }

    @Test
    @DisplayName("3. Upload should throw exception because fileData is empty")
    void uploadCvFile_shouldThrowIllegalArgumentException_whenFilaDataIsEmpty() {
        // given
        var cvFileUploadRequest =
                new CvFileUploadRequest("".getBytes(StandardCharsets.UTF_8), "name", "application/pdf");

        // when
        IllegalArgumentException iae =
                assertThrows(IllegalArgumentException.class, () -> sut.uploadCvFile(cvFileUploadRequest));

        // then
        assertThat(iae.getMessage()).contains("empty");
    }

    @Test
    @DisplayName("4. Upload should throw exception because fileData is too large")
    void uploadCvFile_shouldThrowIllegalArgumentException_whenFilaDataTooLarge() {
        // given
        var cvFileUploadRequest = new CvFileUploadRequest(new byte[11 * 1024 * 1024], "name", "application/pdf");

        // when
        IllegalArgumentException iae =
                assertThrows(IllegalArgumentException.class, () -> sut.uploadCvFile(cvFileUploadRequest));

        // then
        assertThat(iae.getMessage()).contains("fileData is too large");
    }

    @Test
    @DisplayName("5. Upload should throw exception because invalid content type")
    void uploadCvFile_shouldThrowIllegalArgumentException_whenContentTypeIsNotValid() {
        // given
        var cvFileUploadRequest = new CvFileUploadRequest(new byte[10], "name", "application/xlsx");

        // when
        IllegalArgumentException iae =
                assertThrows(IllegalArgumentException.class, () -> sut.uploadCvFile(cvFileUploadRequest));

        // then
        assertThat(iae.getMessage()).contains("Invalid content type");
    }

    @Test
    @DisplayName("6. Upload should throw exception because filename is null")
    void uploadCvFile_shouldThrowIllegalArgumentException_whenFilenameIsNull() {
        // given
        var cvFileUploadRequest = new CvFileUploadRequest(new byte[10], null, null);
        // when
        IllegalArgumentException iae =
                assertThrows(IllegalArgumentException.class, () -> sut.uploadCvFile(cvFileUploadRequest));

        // then
        assertThat(iae.getMessage()).contains("filename is null");
    }

    @Test
    @DisplayName("7. Upload should throw exception because filename is blank")
    void uploadCvFile_shouldThrowIllegalArgumentException_whenFilenameIsBlank() {
        // given
        var cvFileUploadRequest = new CvFileUploadRequest(new byte[10], "", null);
        // when
        IllegalArgumentException iae =
                assertThrows(IllegalArgumentException.class, () -> sut.uploadCvFile(cvFileUploadRequest));

        // then
        assertThat(iae.getMessage()).contains("blank");
    }

    @Test
    @DisplayName("8. Upload should throw exception because cvFile is already present")
    void uploadCvFile_shouldThrowIllegalArgumentException_whenCvFileIsAlreadyPresent() {
        // given
        var cvFileUploadRequest = new CvFileUploadRequest(new byte[10], "name", "application/pdf");
        var cvFile = CvFile.builder().build();
        given(fileDataToSha256Mapper.apply(any(byte[].class))).willReturn("sha256");
        given(cvFileReadBySha256Repository.readBySha256(anyString())).willReturn(Optional.of(cvFile));

        // when
        IllegalStateException iae =
                assertThrows(IllegalStateException.class, () -> sut.uploadCvFile(cvFileUploadRequest));

        // then
        assertThat(iae.getMessage()).contains("This CV already exists");
    }

    @Test
    @DisplayName("9. Upload should success")
    void uploadCvFile_shouldSuccess() {
        // given
        var cvFileUploadRequest = new CvFileUploadRequest(new byte[10], "name", "application/pdf");

        given(fileDataToSha256Mapper.apply(any(byte[].class))).willReturn("sha256");
        given(cvFileReadBySha256Repository.readBySha256(anyString())).willReturn(Optional.empty());
        given(cvFilePersistRepository.persist(any(CvFile.class)))
                .willAnswer(invocation -> invocation.getArgument(0, CvFile.class));

        // when
        CvFile result = assertDoesNotThrow(() -> sut.uploadCvFile(cvFileUploadRequest));
        ArgumentCaptor<CvFile> cvFile = ArgumentCaptor.forClass(CvFile.class);

        // then
        var inOrder = Mockito.inOrder(
                fileDataToSha256Mapper,
                cvFileReadBySha256Repository,
                cvFileUpdateAllIsActiveFalseRepository,
                cvFilePersistRepository);
        inOrder.verify(fileDataToSha256Mapper).apply(any(byte[].class));
        inOrder.verify(cvFileReadBySha256Repository).readBySha256(anyString());
        inOrder.verify(cvFileUpdateAllIsActiveFalseRepository).updateIsActiveToFalseIfAny();
        inOrder.verify(cvFilePersistRepository).persist(cvFile.capture());
        inOrder.verifyNoMoreInteractions();

        assertSame(result, cvFile.getValue());
        assertSame(result.filename(), cvFile.getValue().filename());
        assertTrue(cvFile.getValue().isActive());
    }
}
