package it.me.domain.service.food.cost;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import it.me.domain.dto.Ingredient;
import it.me.web.dto.request.FoodCostRequest;
import it.me.web.dto.response.FoodCostResponse;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FoodCostEstimateServiceTest {

    @InjectMocks
    private FoodCostEstimateService sut;

    @Test
    void estimateCorrectly() {
        // given
        List<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(Ingredient.builder()
                .name("ingredient1")
                .pricePerKgEur(2.0)
                .quantity(200)
                .unit(Ingredient.Unit.GRAMS)
                .build());
        FoodCostRequest req = new FoodCostRequest(30, ingredientList);

        // when
        FoodCostResponse res = sut.estimateFoodCost(req);

        // then
        assertNotNull(res);
        assertThat(res).hasNoNullFieldsOrProperties();
    }

    @Test
    void errorName_whileEstimate() {
        // given
        List<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(Ingredient.builder()
                .name("")
                .pricePerKgEur(2.0)
                .quantity(200)
                .unit(Ingredient.Unit.GRAMS)
                .build());
        FoodCostRequest req = new FoodCostRequest(30, ingredientList);

        // when
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> sut.estimateFoodCost(req));

        // then
        assertThat(iae.getMessage()).isEqualTo("Inserisci il nome dell'ingrediente");
    }

    @Test
    void errorQuantity_whileEstimate() {
        // given
        List<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(Ingredient.builder()
                .name("ingredient1")
                .pricePerKgEur(2.0)
                .unit(Ingredient.Unit.GRAMS)
                .build());
        FoodCostRequest req = new FoodCostRequest(30, ingredientList);

        // when
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> sut.estimateFoodCost(req));

        // then
        assertThat(iae.getMessage()).isEqualTo("Aggiungi una quantita reale (non negativa e diversa da 0)");
    }

    @Test
    void errorPricePerKg_whileEstimate() {
        // given
        List<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(Ingredient.builder()
                .name("ingredient1")
                .quantity(100)
                .unit(Ingredient.Unit.GRAMS)
                .build());
        FoodCostRequest req = new FoodCostRequest(30, ingredientList);

        // when
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> sut.estimateFoodCost(req));

        // then
        assertThat(iae.getMessage()).isEqualTo("Inserisci il prezzo al kg reale (non negativo e diverso da 0)");
    }

    @Test
    void errorUnit_whileEstimate() {
        // given
        List<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(Ingredient.builder()
                .name("ingredient1")
                .quantity(100)
                .pricePerKgEur(2.0)
                .build());
        FoodCostRequest req = new FoodCostRequest(30, ingredientList);

        // when
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> sut.estimateFoodCost(req));

        // then
        assertThat(iae.getMessage()).isEqualTo("Al momento l'unica unit disponibile e GRAMS");
    }
}
