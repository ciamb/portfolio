package it.me.repository;

import it.me.entity.CvFile;
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
    TypedQuery<CvFile> typedQuery;

    @Test
    void readByIsActive() {
        //given
        given(em.createNamedQuery(eq(CvFile.READ_BY_IS_ACTIVE), eq(CvFile.class)))
                .willReturn(typedQuery);
        given(typedQuery.getResultStream()).willReturn(Stream.of(new CvFile()));

        //when
        Optional<CvFile> result = assertDoesNotThrow(() -> sut.readByIsActive());

        //then
        assertNotNull(result);
        verify(em, times(1)).createNamedQuery(eq(CvFile.READ_BY_IS_ACTIVE), eq(CvFile.class));
    }
}