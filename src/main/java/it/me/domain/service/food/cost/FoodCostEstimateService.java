package it.me.domain.service.food.cost;

import it.me.domain.dto.Ingredient;
import it.me.web.dto.request.FoodCostRequest;
import it.me.web.dto.response.FoodCostResponse;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class FoodCostEstimateService {

    public FoodCostResponse estimateFoodCost(FoodCostRequest foodCostRequest) {
        double resourceCost = 0.0;

        List<Ingredient> ingredientList = foodCostRequest.ingredientList();
        for (Ingredient ingredient : ingredientList) {
            validateIngredient(ingredient);
            Integer quantity = ingredient.quantity();
            Double pricePerKgEur = ingredient.pricePerKgEur();
            double ingredientCost = ((quantity * pricePerKgEur) / 1000);
            resourceCost += ingredientCost;
        }

        var foodCost = foodCostRequest.foodCost();
        double targetCost = (resourceCost * 100) / foodCost;

        return FoodCostResponse.builder()
                .foodCostRequest(foodCostRequest)
                .resourceCost(resourceCost)
                .targetCost(targetCost)
                .build();
    }

    private void validateIngredient(Ingredient ingredient) {
        String name = ingredient.name();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Inserisci il nome dell'ingrediente");
        }

        Integer quantity = ingredient.quantity();
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Aggiungi una quantita reale (non negativa e diversa da 0)");
        }

        Double pricePerKgEur = ingredient.pricePerKgEur();
        if (pricePerKgEur == null || pricePerKgEur <= 0) {
            throw new IllegalArgumentException("Inserisci il prezzo al kg reale (non negativo e diverso da 0)");
        }

        Ingredient.Unit unit = ingredient.unit();
        if (!unit.equals(Ingredient.Unit.GRAMS)) {
            throw new IllegalArgumentException("Al momento l'unica unit disponibile e GRAMS");
        }
    }
}
