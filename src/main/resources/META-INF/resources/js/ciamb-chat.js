(function () {
    const chatBox = document.getElementById("chatBox");
    const chatForm = document.getElementById("chatForm");
    const chatInput = document.getElementById("chatInput");
    const chatSendBtn = document.getElementById("chatSend");

    if (!chatBox || !chatForm || !chatInput) return;

    function addMsg(text, who) {
        const div = document.createElement("div");
        div.className = "msg " + who;
        div.textContent = text;
        chatBox.appendChild(div);
        chatBox.scrollTop = chatBox.scrollHeight;
    }

    async function ask(question) {
        addMsg(question, "user");
        chatSendBtn.disabled = true;

        // piccolo placeholder
        const typing = document.createElement("div");
        typing.className = "msg bot";
        typing.textContent = "cIAmb sta scrivendo...";
        chatBox.appendChild(typing);
        chatBox.scrollTop = chatBox.scrollHeight;

        try {
            const res = await fetch("/api/chat", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ message: question })
            });

            const text = await res.text();
            typing.remove();
            addMsg(text || "Nessuna risposta.", "bot");
        } catch (e) {
            typing.remove();
            addMsg("Errore di rete. Riprova.", "bot");
        } finally {
            chatSendBtn.disabled = false;
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

    // messaggio iniziale
    addMsg("Ciao! Chiedimi qualcosa su di me!", "bot");
})();
