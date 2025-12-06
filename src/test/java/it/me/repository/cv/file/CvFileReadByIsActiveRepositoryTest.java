package it.me.repository.cv.file;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CvFileReadByIsActiveRepositoryTest {

    @InjectMocks
    private CvFileReadByIsActiveRepository sut;

    @Mock
    private EntityManager em;

    @Mock
    TypedQuery<CvFileEntity> typedQuery;

    @Test
    void readByIsActive() {
        //given
        given(em.createNamedQuery(eq(CvFileEntity.READ_BY_IS_ACTIVE), eq(CvFileEntity.class)))
                .willReturn(typedQuery);
        given(typedQuery.getResultStream()).willReturn(Stream.of(new CvFileEntity()));

        //when
        Optional<CvFileEntity> result = assertDoesNotThrow(() -> sut.readByIsActive());

        //then
        assertNotNull(result);
        verify(em, times(1)).createNamedQuery(eq(CvFileEntity.READ_BY_IS_ACTIVE), eq(CvFileEntity.class));
    }
}