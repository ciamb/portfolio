package it.me.repository;

import it.me.entity.CvFile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CvFileUpdateIsActiveToFalseIfAnyRepositoryTest {

    @Mock
    EntityManager em;

    @Mock
    Query query;

    @InjectMocks
    private CvFileUpdateIsActiveToFalseIfAnyRepository sut;

    @Test
    void updateIsActiveToFalseIfAny() {
        // given
        given(em.createNamedQuery(CvFile.UPDATE_IS_ACTIVE_TO_FALSE_IF_ANY))
                .willReturn(query);
        given(query.setParameter(eq("updatedAt"), any(ZonedDateTime.class)))
                .willReturn(query);
        given(query.executeUpdate()).willReturn(1);

        // when
        assertDoesNotThrow(() -> sut.updateIsActiveToFalseIfAny());

        // then
        Mockito.verify(query).setParameter(eq("updatedAt"), any(ZonedDateTime.class));
        Mockito.verify(query, times(1)).executeUpdate();
    }
}