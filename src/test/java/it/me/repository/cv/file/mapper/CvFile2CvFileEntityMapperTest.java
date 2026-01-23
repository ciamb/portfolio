package it.me.repository.cv.file.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import it.me.domain.dto.CvFile;
import it.me.repository.entity.CvFileEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CvFile2CvFileEntityMapperTest {

    @InjectMocks
    private CvFile2CvFileEntityMapper sut;

    @Test
    void shouldReturnNull() {
        // given
        // when
        CvFileEntity result = sut.apply(null);

        // then
        assertThat(result).isNull();
    }

    @Test
    void shouldApply() {
        // given
        CvFile cv = CvFile.builder().id(1L).build();
        // when
        CvFileEntity entity = sut.apply(cv);
        // then
        assertThat(entity).isNotNull();
        assertEquals(entity.id(), cv.id());
    }
}
