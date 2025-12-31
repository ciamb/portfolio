package it.me.web.dto.request;

import it.me.domain.dto.Ingredient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

@Builder
public record FoodCostRequest(
        @NotNull(
                message =
                        "Devi specificare un valore di food cost rappresentato in percentuale (es. 30 = 30% food cost)")
        Integer foodCost,

        @NotEmpty(message = "Aggiungi almeno un ingrediente") List<Ingredient> ingredientList) {}
