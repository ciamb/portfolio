package it.me.repository;

import it.me.entity.CvFile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CvFileReadBySha256RepositoryTest {

    @InjectMocks
    private CvFileReadBySha256Repository sut;

    @Mock
    EntityManager em;

    @Mock
    TypedQuery<CvFile> query;

    @Test
    @DisplayName("Read by sha256 will return the first result when there is a single result")
    void readBySha256_returnsFirst_whenSingleResult() {
        //given
        var cvFile = new CvFile();
        given(em.createNamedQuery(CvFile.READ_BY_SHA256, CvFile.class)).willReturn(query);
        given(query.setParameter(eq("sha256"), anyString())).willReturn(query);
        given(query.getResultStream()).willReturn(Stream.of(cvFile));

        //when
        Optional<CvFile> result = assertDoesNotThrow(() -> sut.readBySha256("sha256"));

        //then
        assertThat(result).isPresent().contains(cvFile);
        var inOrder = Mockito.inOrder(query, em);
        inOrder.verify(em).createNamedQuery(CvFile.READ_BY_SHA256, CvFile.class);
        inOrder.verify(query).setParameter(eq("sha256"), anyString());
        inOrder.verify(query).getResultStream();
        inOrder.verifyNoMoreInteractions();
    }
}