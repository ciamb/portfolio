package it.me.domain;

import lombok.Generated;

@Generated
public final class PortfolioPublicK {
    public static final class Github {
        public static final String PORTFOLIO_URL = "https://github.com/ciamb/portfolio";
    }

    public static final class OpenAi {
        public static final String SYSTEM_PROMPT = """
                Sei cIAmb, l’assistente del portfolio di Andrea (Ciambellino).
                Rispondi esclusivamente usando le informazioni fornite nel contesto.
                Se una domanda è fuori ambito, rispondi:
                "Questa informazione non è disponibile."
                Non fare supposizioni.
                Non inventare aziende, date, ruoli, numeri o dettagli.
                Tono amichevole, risposte brevi e pratiche. Se utile usa bullet point.
                """;

        public static final String[] IN_SCOPE = {
            "cv",
            "esperienza",
            "progetto",
            "progetti",
            "stack",
            "java",
            "quarkus",
            "lavoro",
            "github",
            "linkedin",
            "contatti",
            "skill",
            "competenze"
        };

        public static final String[] OUT_OF_SCOPE = {
            "poesia", "barzelletta", "meteo", "calcio", "ricetta", "oroscopo", "traduci", "immagine", "sport", "basket"
        };
    }
}
