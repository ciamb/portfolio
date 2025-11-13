package it.me.domain.mapper;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.function.Function;

@ApplicationScoped
public class FilenameDefaultMapper implements Function<String, String> {
    @Override
    public String apply(String filename) {
        if (filename == null || filename.isBlank()) {
            return "CV_Andrea_Ciambella.pdf";
        }

        var trimmed = filename.trim();
        if (trimmed.toLowerCase().endsWith(".pdf")) {
            trimmed = trimmed.replace(".pdf", "").trim();
        }
        trimmed = trimmed.concat(".pdf");

        return trimmed;
    }
}
