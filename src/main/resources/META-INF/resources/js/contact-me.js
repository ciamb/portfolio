(function () {
    "use strict";

    /**
     * Modale "Contact me":
     * - apre/chiude modale
     * - invia JSON a POST /api/contact-me
     * - gestisce feedback e validazione JSON
     * - supporta chiusura con ESC e click sul backdrop
     */
    const openBtn = document.getElementById("contact-me-open-btn");
    const closeBtn = document.getElementById("contact-me-close-btn");
    const cancelBtn = document.getElementById("contact-me-cancel-btn");
    const submitBtn = document.getElementById("contact-me-submit-btn");

    const backdrop = document.getElementById("contact-me-backdrop");
    const modal = backdrop ? backdrop.querySelector(".contact-me") : null;

    const textarea = document.getElementById("contact-json");
    const feedback = document.getElementById("contact-feedback");

    if (!backdrop || !modal || !textarea || !feedback) return;

    let lastFocusEl = null;

    function setFeedback(message, type) {
        feedback.textContent = message;
        feedback.className = "contact-me__feedback contact-me__feedback--" + type;
    }

    function openModal() {
        lastFocusEl = document.activeElement;
        setFeedback("", "info");
        backdrop.hidden = false;
        modal.focus();
        textarea.focus();
    }

    function closeModal() {
        backdrop.hidden = true;
        if (lastFocusEl && typeof lastFocusEl.focus === "function") {
            lastFocusEl.focus();
        }
    }

    function setBusy(busy) {
        submitBtn.disabled = busy;
        textarea.disabled = busy;
    }

    async function readJsonSafe(response) {
        try {
            return await response.json();
        } catch {
            return null;
        }
    }

    async function submitContact() {
        setFeedback("✏️ Scrittura in corso...", "info");

        const raw = textarea.value.trim();
        if (!raw) {
            setFeedback("Ti stai dimenticando di lasciare un messaggio! 😤", "error");
            return;
        }

        let parsed;
        try {
            parsed = JSON.parse(raw);
        } catch {
            setFeedback("JSON non valido. Controlla virgolette, virgole e parentesi.", "error");
            return;
        }

        setBusy(true);

        try {
            const response = await fetch("/api/contact-me", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(parsed),
            });

            const data = await readJsonSafe(response);

            if (response.ok) {
                const msg = (data && typeof data.message === "string")
                    ? data.message
                    : "Messaggio inserito nel sistema correttamente!";
                setFeedback(msg, "success");
                return;
            }

            const err = (data && typeof data.message === "string")
                ? data.message
                : `Qualcosa non quadra (HTTP ${response.status}). Riprova più tardi.`;
            setFeedback(err, "error");
        } catch {
            setFeedback("Impossibile contattare il server. Controlla la connessione.", "error");
        } finally {
            setBusy(false);
        }
    }

    openBtn?.addEventListener("click", openModal);
    closeBtn?.addEventListener("click", closeModal);
    cancelBtn?.addEventListener("click", closeModal);
    submitBtn?.addEventListener("click", submitContact);

    backdrop.addEventListener("click", (event) => {
        if (event.target === backdrop) closeModal();
    });

    document.addEventListener("keydown", (event) => {
        if (event.key === "Escape" && !backdrop.hidden) closeModal();
    });
})();
