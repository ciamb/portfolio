//package it.me.repository.cv.file;
//
//import it.me.repository.entity.CvFileEntity;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.TypedQuery;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//import java.util.stream.Stream;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.BDDMockito.given;
//
//@ExtendWith(MockitoExtension.class)
//class CvFileReadBySha256RepositoryJpaTest {
//
//    @InjectMocks
//    private CvFileReadBySha256RepositoryJpa sut;
//
//    @Mock
//    EntityManager em;
//
//    @Mock
//    TypedQuery<CvFileEntity> query;
//
//    @Test
//    @DisplayName("Read by sha256 will return the first result when there is a single result")
//    void readBySha256_returnsFirst_whenSingleResult() {
//        //given
//        var cvFile = new CvFileEntity();
//        given(em.createNamedQuery(CvFileEntity.READ_BY_SHA256, CvFileEntity.class)).willReturn(query);
//        given(query.setParameter(eq("sha256"), anyString())).willReturn(query);
//        given(query.getResultStream()).willReturn(Stream.of(cvFile));
//
//        //when
//        Optional<CvFileEntity> result = assertDoesNotThrow(() -> sut.readBySha256("sha256"));
//
//        //then
//        assertThat(result).isPresent().contains(cvFile);
//        var inOrder = Mockito.inOrder(query, em);
//        inOrder.verify(em).createNamedQuery(CvFileEntity.READ_BY_SHA256, CvFileEntity.class);
//        inOrder.verify(query).setParameter(eq("sha256"), anyString());
//        inOrder.verify(query).getResultStream();
//        inOrder.verifyNoMoreInteractions();
//    }
//}