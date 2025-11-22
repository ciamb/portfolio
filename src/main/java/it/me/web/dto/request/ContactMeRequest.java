package it.me.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ContactMeRequest(
        @NotBlank(message = "Inserisci l'email a cui vuoi essere contattato")
        @Email(message = "Inserisci una email valida per favore")
        @Size(max = 255, message = "L'email \u00E8 troppo lunga")
        String email,

        @NotBlank(message = "Inserisci il tuo nome per favore")
        @Size(max = 150, message = "Il nome \u00E8 troppo lungo")
        String name,

        @NotBlank(message = """
                Lascia un messaggio carino per me.
                Dimmi se ci siamo gi\u00E0 conosciuti o abbiamo gi\u00E0 lavorato insieme
                """)
        String message,

        @NotNull(message = "Fammi sapere se vuoi essere contatto con true = si, false = no")
        boolean contactBack
) {
}
