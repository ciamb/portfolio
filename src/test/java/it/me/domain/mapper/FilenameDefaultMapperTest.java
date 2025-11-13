package it.me.domain.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class FilenameDefaultMapperTest {

    @InjectMocks
    private FilenameDefaultMapper sut;

    @Test
    void filename_isNull_returnDefault() {
        //given
        //when
        String defaultFilename = sut.apply(null);

        //then
        assertEquals("CV_Andrea_Ciambella.pdf", defaultFilename);
    }

    @Test
    void filename_isBlank_returnDefault() {
        //given
        //when
        String defaultFilename = sut.apply("       ");

        //then
        assertEquals("CV_Andrea_Ciambella.pdf", defaultFilename);
    }

    @Test
    void filename_returnTrimmed_withExtension() {
        //given
        //when
        String trimmed = sut.apply("    ciaaa   .pdf");

        //then
        assertEquals("ciaaa.pdf", trimmed);
    }

    @Test
    void filename_returnTrimmed_withoutExtension() {
        //given
        //when
        String trimmed = sut.apply("    ciaaa   ");

        //then
        assertEquals("ciaaa.pdf", trimmed);
    }
}