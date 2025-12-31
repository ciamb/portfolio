package it.me.repository.cv.file;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

import it.me.repository.entity.CvFileEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CvFileUpdateAllIsActiveToFalseRepositoryJpaTest {

    @Mock
    EntityManager em;

    @Mock
    Query query;

    @InjectMocks
    private CvFileUpdateAllIsActiveToFalseRepositoryJpa sut;

    @Test
    void updateIsActiveToFalseIfAny() {
        // given
        given(em.createNamedQuery(CvFileEntity.UPDATE_IS_ACTIVE_TO_FALSE_IF_ANY))
                .willReturn(query);
        given(query.setParameter(eq("updatedAt"), any(ZonedDateTime.class))).willReturn(query);
        given(query.executeUpdate()).willReturn(1);

        // when
        assertDoesNotThrow(() -> sut.updateIsActiveToFalseIfAny());

        // then
        Mockito.verify(query).setParameter(eq("updatedAt"), any(ZonedDateTime.class));
        Mockito.verify(query, times(1)).executeUpdate();
    }
}
