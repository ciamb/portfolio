package it.me.domain.mapper;

import it.me.domain.dto.ContactMe;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

@ApplicationScoped
public class ContactMeEmailBodyMapper implements Function<List<ContactMe>, String> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMMM yyyy - HH:mm");

    @ConfigProperty(name = "contact.me.username")
    String username;

    @Override
    public String apply(List<ContactMe> pendingList) {
        var body = """
                
                Ciao %s,
                Ecco il riepilogo delle persone che hanno lasciato un messaggio per te in questo periodo:
                
                """.formatted(username);

        for (var item : pendingList) {
            if (item.errorReason() != null && !item.errorReason().isBlank()) {
                body = body.concat("""
                        -----------------------------------------------------------------------------------
                        Il messaggio di %s \u00E8 andato in errore:
                        %s
                        
                        Questa \u00E8 la email per ricontattarlo: %s
                        """.formatted(
                        item.name(),
                        item.errorReason(),
                        item.email()
                ));
            } else {
                body = body.concat("""
                        -----------------------------------------------------------------------------------
                        %s ti ha scritto:
                        %s
                        
                        %s
                        Messaggio salvato il: %s
                        """.formatted(
                        item.name(),
                        item.message(),
                        item.contactBack() ? "Vuole essere ricontattato all'indirizzo email %s".formatted(item.email()) : "Non vuole essere ricontattato",
                        item.createdAt().format(formatter)

                ));
            }
        }

        body = body.concat("""
                -----------------------------------------------------------------------------------
                Totale dei messaggi processati: %d.
                
                A presto dal mailer del tuo portfolio!
                
                """.formatted(pendingList.size()));

        return body;
    }
}
