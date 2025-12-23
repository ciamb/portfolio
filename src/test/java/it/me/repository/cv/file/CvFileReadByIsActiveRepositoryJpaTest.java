package it.me.repository.cv.file;

import it.me.domain.dto.CvFile;
import it.me.repository.cv.file.mapper.CvFileEntity2CvFileMapper;
import it.me.repository.entity.CvFileEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CvFileReadByIsActiveRepositoryJpaTest {

    @InjectMocks
    private CvFileReadByIsActiveRepositoryJpa sut;

    @Mock
    private EntityManager em;

    @Mock
    CvFileEntity2CvFileMapper cvFileEntity2CvFileMapper;

    @Mock
    TypedQuery<CvFileEntity> typedQuery;

    @Test
    void readByIsActive() {
        //given
        CvFile cvFile = CvFile.builder().build();
        given(em.createNamedQuery(eq(CvFileEntity.READ_BY_IS_ACTIVE), eq(CvFileEntity.class)))
                .willReturn(typedQuery);
        given(typedQuery.getResultStream()).willReturn(Stream.of(new CvFileEntity()));
        given(cvFileEntity2CvFileMapper.apply(any(CvFileEntity.class))).willReturn(cvFile);

        //when
        Optional<CvFile> result = assertDoesNotThrow(() -> sut.readByIsActive());

        //then
        assertNotNull(result);
        verify(em, times(1)).createNamedQuery(eq(CvFileEntity.READ_BY_IS_ACTIVE), eq(CvFileEntity.class));
        verify(cvFileEntity2CvFileMapper, times(1)).apply(any(CvFileEntity.class));
    }
}