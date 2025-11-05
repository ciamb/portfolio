package it.me.repository;

import it.me.entity.CvFile;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CvFilePersistRepositoryTest {

    @InjectMocks
    private CvFilePersistRepository sut;

    @Mock
    EntityManager em;

    @Test
    @DisplayName("Persist should call flush and refresh and return")
    void persistCvFile_callPersistFlushRefresh_inOrder() {
        // given
        var cvFile = new CvFile();

        // when
        CvFile result = sut.persistCvFile(cvFile);

        //then
        assertSame(cvFile, result);
        var inOrder = Mockito.inOrder(em);
        inOrder.verify(em, times(1)).persist(cvFile);
        inOrder.verify(em, times(1)).flush();
        inOrder.verify(em, times(1)).refresh(cvFile);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Persist with null input should fail and not call flush and refresh")
    void persistCvFile_callPersist_withNullInput_shouldFail() {
        // given
        doThrow(new IllegalArgumentException("input is null"))
                .when(em).persist(isNull());

        // when
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> sut.persistCvFile(null));

        //then
        assertThat(iae.getMessage()).contains("input is null");
        var inOrder = Mockito.inOrder(em);
        inOrder.verify(em, times(1)).persist(null);
        inOrder.verify(em, never()).flush();
        inOrder.verify(em, never()).refresh(any());
        inOrder.verifyNoMoreInteractions();
    }

}