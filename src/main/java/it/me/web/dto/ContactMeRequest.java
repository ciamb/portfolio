package it.me.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ContactMeRequest(
        @NotBlank(message = "L'email non puo essere vuota")
        @Email(message = "Inserisci una email valida")
        @Size(max = 255)
        String email,

        @NotBlank(message = "Inserisci il tuo nome completo")
        @Size(max = 150, message = "Il nome non puo contenere piu di 150 caratteri")
        String name,

        @NotBlank(message = "Lascia un messaggio")
        String message,

        @NotNull(message = "Fammi sapere se vuoi essere contattato")
        boolean contactBack
) {
}
