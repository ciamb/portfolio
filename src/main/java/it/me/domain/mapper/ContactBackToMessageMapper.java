package it.me.domain.mapper;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.function.Function;

@ApplicationScoped
public class ContactBackToMessageMapper implements Function<Boolean, String> {
    @Override
    public String apply(Boolean contactBack) {
        return contactBack ?
                """
                        Grazie per aver registrato un messaggio.
                        Il messaggio sar\u00E0 processato durante il fine settimana.
                        Ci sentiamo presto!
                        """
                :
                """
                        Grazie per aver registrato un messaggio per me.
                        Ti auguro una buona giornata!
                        """;
    }
}
