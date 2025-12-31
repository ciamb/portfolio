package it.me.repository.cv.file;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import it.me.domain.dto.CvFile;
import it.me.repository.cv.file.mapper.CvFile2CvFileEntityMapper;
import it.me.repository.entity.CvFileEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CvFilePersistRepositoryJpaTest {

    @InjectMocks
    private CvFilePersistRepositoryJpa sut;

    @Mock
    CvFile2CvFileEntityMapper cvFile2CvFileEntityMapper;

    @Mock
    EntityManager em;

    @Test
    @DisplayName("Persist should call flush and refresh and return")
    void persistCvFile_callPersistFlushRefresh_inOrder() {
        // given
        var cvFile = new CvFileEntity();
        var dto = CvFile.builder().build();
        given(cvFile2CvFileEntityMapper.apply(dto)).willReturn(cvFile);

        // when
        CvFile result = sut.persist(dto);

        // then
        assertSame(dto, result);
        var inOrder = Mockito.inOrder(em, cvFile2CvFileEntityMapper);
        inOrder.verify(cvFile2CvFileEntityMapper, times(1)).apply(dto);
        inOrder.verify(em, times(1)).persist(cvFile);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Persist with null input should fail and not call flush and refresh")
    void persistCvFile_callPersist_withNullInput_shouldFail() {
        // given
        doThrow(new IllegalArgumentException("input is null")).when(em).persist(isNull());

        // when
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> sut.persist(null));

        // then
        assertThat(iae.getMessage()).contains("input is null");
        var inOrder = Mockito.inOrder(em);
        inOrder.verify(em, times(1)).persist(null);
        inOrder.verifyNoMoreInteractions();
    }
}
