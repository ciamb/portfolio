(function () {
    "use strict";

    /**
     * Food cost calculator UI:
     * - gestisce lista ingredienti (max 10) via template
     * - valida input lato client
     * - chiama POST /api/food-cost/estimate
     * - renderizza risultato o errore
     */

    const MAX_INGREDIENTS = 10;
    const API_URL = "/api/food-cost/estimate";
    const EUR = new Intl.NumberFormat("it-IT", { style: "currency", currency: "EUR" });

    // DOM
    const foodCostForm = document.getElementById("foodCostForm");
    const foodCostPercent = document.getElementById("foodCostPercent");
    const estimateFoodCostButton = document.getElementById("estimateFoodCostButton");

    const addIngredientButton = document.getElementById("addIngredientButton");
    const ingredientRowTemplate = document.getElementById("ingredientRowTemplate");
    const ingredientListContainer = document.getElementById("ingredientListContainer");

    const resultBox = document.getElementById("resultBox");
    const resourceCost = document.getElementById("resourceCost");
    const targetCost = document.getElementById("targetCost");

    const errorBox = document.getElementById("errorBox");
    const errorMessage = document.getElementById("errorMessage");

    if (
        !foodCostForm || !foodCostPercent || !estimateFoodCostButton ||
        !addIngredientButton || !ingredientRowTemplate || !ingredientListContainer ||
        !resultBox || !resourceCost || !targetCost || !errorBox || !errorMessage
    ) return;

    function sanitizeItNumber(value) {
        const raw = String(value ?? "").trim();
        if (!raw) return Number.NaN;
        return Number(raw.replace(",", "."));
    }

    function formatEuro(value) {
        const n = Number(value);
        return Number.isFinite(n) ? EUR.format(n) : "-";
    }

    function resetResult() {
        resultBox.hidden = true;
        errorBox.hidden = true;
        resourceCost.textContent = "-";
        targetCost.textContent = "-";
        errorMessage.textContent = "-";
    }

    function getIngredientRowCount() {
        return ingredientListContainer.querySelectorAll(".ingredient-row").length;
    }

    function updateButtons() {
        const count = getIngredientRowCount();
        addIngredientButton.disabled = count >= MAX_INGREDIENTS;
        estimateFoodCostButton.disabled = count === 0;
    }

    function renderResult(result) {
        resultBox.hidden = false;
        errorBox.hidden = true;
        resourceCost.textContent = formatEuro(result.resourceCost);
        targetCost.textContent = formatEuro(result.targetCost);
    }

    function renderError(message) {
        resultBox.hidden = true;
        errorBox.hidden = false;
        errorMessage.textContent = message || "Errore imprevisto";
    }

    function setBusy(busy) {
        estimateFoodCostButton.disabled = busy || getIngredientRowCount() === 0;
        addIngredientButton.disabled = busy || getIngredientRowCount() >= MAX_INGREDIENTS;
    }

    function addIngredientRow() {
        resetResult();

        const count = getIngredientRowCount();
        if (count >= MAX_INGREDIENTS) {
            console.info("Troppi ingredienti");
            return;
        }

        const cloned = ingredientRowTemplate.content.cloneNode(true);
        ingredientListContainer.appendChild(cloned);
        updateButtons();
    }

    function readFormData() {
        const foodCost = sanitizeItNumber(foodCostPercent.value);

        const rows = ingredientListContainer.querySelectorAll(".ingredient-row");
        const ingredientList = Array.from(rows).map((row) => {
            const name = row.querySelector('input[name="ingredientName"]').value.trim();
            const quantity = sanitizeItNumber(row.querySelector('input[name="ingredientQuantity"]').value);
            const pricePerKgEur = sanitizeItNumber(row.querySelector('input[name="ingredientPricePerKg"]').value);

            return { name, quantity, unit: "GRAMS", pricePerKgEur };
        });

        return { foodCost, ingredientList };
    }

    function validate(data) {
        if (!Number.isFinite(data.foodCost) || data.foodCost < 1 || data.foodCost > 100) {
            return "Food cost non valido: inserisci un numero tra 1 e 100.";
        }
        if (!data.ingredientList.length) {
            return "Aggiungi almeno un ingrediente.";
        }
        if (data.ingredientList.length > MAX_INGREDIENTS) {
            return `Puoi inserire massimo ${MAX_INGREDIENTS} ingredienti.`;
        }

        for (let i = 0; i < data.ingredientList.length; i++) {
            const ing = data.ingredientList[i];
            if (!ing.name) return `Ingrediente #${i + 1}: nome mancante.`;
            if (!Number.isFinite(ing.quantity) || ing.quantity <= 0) return `Ingrediente #${i + 1}: quantità non valida.`;
            if (!Number.isFinite(ing.pricePerKgEur) || ing.pricePerKgEur < 0) return `Ingrediente #${i + 1}: prezzo non valido.`;
        }

        return null;
    }

    async function callFoodCostEstimate(data) {
        const payload = {
            foodCost: data.foodCost,
            ingredientList: data.ingredientList,
        };

        const response = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload),
        });

        if (!response.ok) {
            let msg = `HTTP ${response.status}`;
            try {
                const err = await response.json();
                if (err?.message) msg += `: ${err.message}`;
            } catch { /* ignore */ }
            throw new Error(msg);
        }

        return response.json();
    }

    // Events
    addIngredientButton.addEventListener("click", addIngredientRow);

    // Event delegation: rimuovi ingrediente
    ingredientListContainer.addEventListener("click", (event) => {
        const btn = event.target.closest(".removeIngredientButton");
        if (!btn) return;

        const row = btn.closest(".ingredient-row");
        if (row) row.remove();

        resetResult();
        updateButtons();
    });

    // Reset result on any input change inside the form
    foodCostForm.addEventListener("input", () => resetResult());

    foodCostForm.addEventListener("submit", async (event) => {
        event.preventDefault();
        resetResult();

        const data = readFormData();
        const err = validate(data);
        if (err) {
            renderError(err);
            return;
        }

        setBusy(true);
        try {
            const result = await callFoodCostEstimate(data);
            renderResult(result);
        } catch (error) {
            console.error(error);
            renderError(error?.message);
        } finally {
            setBusy(false);
            updateButtons();
        }
    });

    // Boot
    addIngredientRow();
    updateButtons();
    resetResult();
})();
