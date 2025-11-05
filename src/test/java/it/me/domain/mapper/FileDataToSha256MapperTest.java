package it.me.domain.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FileDataToSha256MapperTest {

    @InjectMocks
    private FileDataToSha256Mapper sut;

    @Test
    @DisplayName("1. Apply a null fileDate should return null")
    void applyNull_returnsNull() {
        //given
        //when
        //then
        assertNull(sut.apply(null));
    }

    @Test
    @DisplayName("2. Apply same dataFile should return same sha256")
    void applySameInput_returnsSameSha256() {
        //given
        var fileData = "EJIUJNSAInijdnijngiownvj49usdionv83ibnso".getBytes(StandardCharsets.UTF_8);

        //when
        var sha256_1 = sut.apply(fileData);
        var sha256_2 = sut.apply(fileData);

        //then
        assertEquals(sha256_1, sha256_2);
    }

    @Test
    @DisplayName("3. Apply with a wrong algorithm should throw RuntimeException")
    void apply_shouldThrowNoSuchAlgorithmException_returnsRuntimeException() {
        //given
        var fileData = "EJIUJNSAInijdnijngiownvj49usdionv83ibnso".getBytes(StandardCharsets.UTF_8);

        try (MockedStatic<MessageDigest> md = Mockito.mockStatic(MessageDigest.class)) {

            md.when(() -> MessageDigest.getInstance("SHA-256"))
                    .thenThrow(new NoSuchAlgorithmException("error"));

            //when
            RuntimeException re = assertThrows(RuntimeException.class,
                    () -> sut.apply(fileData));

            //then
            assertThat(re.getCause()).isInstanceOf(NoSuchAlgorithmException.class);
            assertThat(re.getMessage()).contains("Error while digesting SHA-256");
        }
    }
}