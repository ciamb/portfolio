// const and variable
const MAX_INGREDIENTS = 10;
const API_URL = "/api/food-cost/estimate";

// ===================
// == ELEMENTI DOM  ==
// ===================

// food cost
const foodCostForm = document.getElementById("foodCostForm");
const foodCostPercent = document.getElementById("foodCostPercent");
const estimateFoodCostButton = document.getElementById("estimateFoodCostButton");

// ingredient
const addIngredientButton = document.getElementById("addIngredientButton");
const ingredientRowTemplate = document.getElementById("ingredientRowTemplate");
const ingredientListContainer = document.getElementById("ingredientListContainer");

// result box
const resultBox = document.getElementById("resultBox");
const resourceCost = document.getElementById("resourceCost");
const targetCost = document.getElementById("targetCost");

// error box
const errorBox = document.getElementById("errorBox");
const errorMessage = document.getElementById("errorMessage");

// ====================
// == METODI HELPERS ==
// ====================

/**
 * Fa il parse del numero in stile italiano trasformando la virgola in punto
 * @param value valore da parsare
 * @returns {number} numero parsato
 */
function sanitizeItNumber(value) {
    // handle italian number for price like "1,60"
    const itValue = String(value ?? "")
        .trim()
        .replace(",", ".");
    // per info: l'operatore ?? in js significa ->
    // SE value e' null or undefined allora usa "" altrimenti usa value
    // questo e' particolarmente utile perche' poi
    // Number("") === 0, quindi ancora valido
    return Number(itValue);
}

/**
 * Fa il parso del valore di soldi in euro (quindi ad un valore alla seconda cifra
 * decimale)
 * @param value valore da formattare
 * @returns {string} valore da mostrare
 */
function formatEuro(value) {
    const euroValue = Number(value);
    if (!Number.isFinite(euroValue)) {
        return "- euro";
    }
    const fixedValue = euroValue.toFixed(2);
    return `${fixedValue} euro`;
}

/**
 * Resetta il risultato del calcolo del costo quando viene
 * aggiunta una nuova riga di ingrediente
 */
function resetResult() {
    resultBox.hidden = true;
    errorBox.hidden = true;

    resourceCost.textContent = "- euro";
    targetCost.textContent = "- euro";
    errorMessage.textContent = "-";
}

function getIngredientRowCount() {
    return ingredientListContainer.querySelectorAll(".ingredient-row").length;
}

function updateEstimateButton() {
    const count = getIngredientRowCount();
    estimateFoodCostButton.disabled = count === 0;
}

function updateAddIngredientButton() {
    const count = getIngredientRowCount();
    addIngredientButton.disabled = count >= MAX_INGREDIENTS;
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

// ==============================
// == INGREDIENT ROW MANAGMENT ==
// ==============================

/**
 * Aggiunge una riga per l'ingrediente clonandola
 * dal template presente in HTML
 */
function addIngredientRow() {
    resetResult();

    const count = getIngredientRowCount();
    if (count >= MAX_INGREDIENTS) {
        log.info("Troppi ingredienti")
        return;
    }

    // cloneNode clona tutto, anche i figli (quindi fa un deep clone)
    const clonedRowTemplate = ingredientRowTemplate.content
        .cloneNode(true);

    const removeIngredientButton = clonedRowTemplate
        .querySelector(".removeIngredientButton");

    removeIngredientButton.addEventListener("click", (event) => {
        const row = event.target.closest(".ingredient-row");
        if (row) {
            row.remove();
        }
        updateAddIngredientButton();
        updateEstimateButton()
        resetResult();
    });

    ingredientListContainer.appendChild(clonedRowTemplate);
    updateAddIngredientButton();
    updateEstimateButton();
}

// ==========================
// == INGREDIENT FORM DATA ==
// ==========================

function readFormData() {
    const foodCostPercentValue = foodCostPercent.value;

    const rows = ingredientListContainer
        .querySelectorAll(".ingredient-row");

    const ingredientList = Array.from(rows)
        .map(row => {
            const name = row
                .querySelector('input[name="ingredientName"]').value
                .trim();

            const quantity = sanitizeItNumber(
                row.querySelector('input[name="ingredientQuantity"]').value
            );

            const pricePerKgEur = sanitizeItNumber(
                row.querySelector('input[name="ingredientPricePerKg"]').value
            );

            return { name, quantity, unit: "GRAMS", pricePerKgEur };
        });

    return { foodCostPercentValue, ingredientList}
}

// ============================
// == FOOD COST ESTIMATE API ==
// ============================
async function callFoodCostEstimate(data) {
    const payload = {
        foodCost: data.foodCostPercentValue,
        ingredientList: data.ingredientList
    };

    const responseAPI = await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    });

    // passaggio da capire
    if (!responseAPI.ok) {
        let errorMessage = `${responseAPI.status}`;
        try {
            const errorResponse = await responseAPI.json();
            if (errorResponse?.message) {
                errorMessage += `: ${errorResponse.message}`;
            }
        } catch (ignored) {
            // errore ignorato, capire quali tipi di errori potrebbero presentarsi
        }
        throw new Error(errorMessage);
    }

    return responseAPI.json();
}

// ==========================
// == EVENTI LEGATI A HTML ==
// ==========================

// attacca al bottone + l'aggiunta di una nuova riga
addIngredientButton.addEventListener("click", addIngredientRow);

// attacca al form l'evento di submit per inviare i dati all'api
// di calcolo
foodCostForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    resetResult();

    const data = readFormData();
    // gestione degli errori nel form eventuali

    try {
        let result;
        if (API_URL && API_URL.trim().length > 0) {
            result = await callFoodCostEstimate(data);
        } else {
            throw new Error("API not ready")
        }
        renderResult(result);
    } catch (error) {
        console.error(error);
        renderError(error?.message)
    }
});

// ==============
// == PARTENZA ==
// ==============
addIngredientRow();
updateAddIngredientButton();
updateEstimateButton();
resetResult();
