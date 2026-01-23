(function () {
    "use strict";

    /**
     * Semplice chat UI:
     * - stampa messaggi in chatBox
     * - invia { message } a POST /api/chat
     * - mostra placeholder "sta scrivendo..."
     */
    const chatBox = document.getElementById("chatBox");
    const chatForm = document.getElementById("chatForm");
    const chatInput = document.getElementById("chatInput");
    const chatSendBtn = document.getElementById("chatSend");

    if (!chatBox || !chatForm || !chatInput || !chatSendBtn) return;

    let isBusy = false;

    /**
     * @param {string} text
     * @param {"user"|"bot"} who
     */
    function addMsg(text, who) {
        const div = document.createElement("div");
        div.className = "msg " + who;
        div.textContent = text;

        chatBox.appendChild(div);
        requestAnimationFrame(() => {
            chatBox.scrollTop = chatBox.scrollHeight;
        });
    }

    function setBusy(busy) {
        isBusy = busy;
        chatSendBtn.disabled = busy;
        chatInput.disabled = busy;
    }

    /**
     * Invia la domanda al backend e stampa la risposta.
     * @param {string} question
     */
    async function ask(question) {
        if (isBusy) return;

        addMsg(question, "user");
        setBusy(true);

        const typing = document.createElement("div");
        typing.className = "msg bot";
        typing.textContent = "cIAmb sta scrivendo...";
        chatBox.appendChild(typing);
        chatBox.scrollTop = chatBox.scrollHeight;

        try {
            const res = await fetch("/api/chat", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ message: question }),
            });

            const text = await res.text();

            typing.remove();
            if (!res.ok) {
                addMsg(text || `Errore (${res.status}).`, "bot");
                return;
            }

            addMsg(text || "Nessuna risposta.", "bot");
        } catch (e) {
            typing.remove();
            addMsg("Errore di rete. Riprova.", "bot");
        } finally {
            setBusy(false);
            chatInput.focus();
        }
    }

    chatForm.addEventListener("submit", (e) => {
        e.preventDefault();
        const q = (chatInput.value || "").trim();
        if (!q) return;

        chatInput.value = "";
        ask(q);
    });

    addMsg("Ciao! Chiedimi qualcosa su di me!", "bot");
})();
