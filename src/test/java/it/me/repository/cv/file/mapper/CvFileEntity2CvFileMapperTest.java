package it.me.repository.cv.file.mapper;

import static org.junit.jupiter.api.Assertions.*;

import it.me.domain.dto.CvFile;
import it.me.repository.entity.CvFileEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CvFileEntity2CvFileMapperTest {

    @InjectMocks
    private CvFileEntity2CvFileMapper sut;

    @Test
    void shouldReturnNull() {
        // given
        // when
        // then
        assertNull(sut.apply(null));
    }

    @Test
    void shouldApply() {
        // given
        CvFileEntity cfe = new CvFileEntity();
        cfe.setId(1L);
        // when
        CvFile res = sut.apply(cfe);
        // then
        assertNotNull(res);
        assertEquals(cfe.id(), res.id());
    }
}
