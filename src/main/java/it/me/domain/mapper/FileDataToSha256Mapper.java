package it.me.domain.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.function.Function;

@ApplicationScoped
public class FileDataToSha256Mapper implements Function<byte[], String> {
    @Override
    public String apply(byte[] fileData) {
        if (fileData == null) {
            return null;
        }

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] digest = messageDigest.digest(fileData);
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while digesting SHA-256", e);
        }
    }
}
