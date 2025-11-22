(function () {
  const openBtn = document.getElementById('contact-me-open-btn');
  const closeBtn = document.getElementById('contact-me-close-btn');
  const cancelBtn = document.getElementById('contact-me-cancel-btn');
  const submitBtn = document.getElementById('contact-me-submit-btn');

  const backdrop = document.getElementById('contact-me-backdrop');
  const textarea = document.getElementById('contact-json');
  const feedback = document.getElementById('contact-feedback');

  function openModal() {
    feedback.textContent = '';
    feedback.className = 'contact-me__feedback';
    backdrop.hidden = false;
  }

  function closeModal() {
    backdrop.hidden = true;
  }

  function setFeedback(message, type) {
    feedback.textContent = message;
    feedback.className =
      'contact-me__feedback contact-me__feedback--' + type;
  }

    async function submitContact() {
        setFeedback('✏️ Scrittura in corso...', 'info');
        const textareaValue = textarea.value.trim();

        if (!textareaValue) {
            setFeedback('Ti stai dimenticando di lasciare un messaggio! 😤', 'error');
            return;
        }

        let parsed;
        try {
            parsed = JSON.parse(textareaValue);
        } catch (e) {
            setFeedback(
                'Aia! Il JSON non è valido. 😶‍🌫️ Controlla virgolette, virgole e parentesi..',
                'error'
            );
            return;
        }

        submitBtn.disabled = true;

        try {
            const response = await fetch('/api/contact-me', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(parsed)
            });

            // response ok
            if (response.ok) {
                let msgDefault = 'Messaggio inserito nel sistema correttamente!';
                try {
                    const successData = await response.json();
                    if (successData && typeof successData.message === 'string') {
                        setFeedback(successData.message, 'success');
                    } else {
                        setFeedback(msgDefault, 'success');
                    }
                } catch (ignored) {
                    console.debug('parsing response.json failed')
                    setFeedback(msgDefault, 'success');
                }
                return;
            }

            // bad response
            let errorMsgDefault = 'Qualcosa non quadra, riprova piu tardi.';
            try {
                const data = await response.json();
                if (data && typeof data.message === 'string') {
                    errorMsgDefault = data.message;
                }
            } catch (ignored) {
            }
            setFeedback(errorMsgDefault, 'error');

        } catch (e) {
            let msg500 = 'Impossibile contattare il server. Controlla la connessione'
            setFeedback(msg500, 'error');
        } finally {
            submitBtn.disabled = false;
        }
    }

  if (openBtn) {
    openBtn.addEventListener('click', openModal);
  }
  if (closeBtn) {
    closeBtn.addEventListener('click', closeModal);
  }
  if (cancelBtn) {
    cancelBtn.addEventListener('click', closeModal);
  }

  if (backdrop) {
    backdrop.addEventListener('click', function (event) {
      if (event.target === backdrop) {
        closeModal();
      }
    });
  }

  if (submitBtn) {
    submitBtn.addEventListener('click', submitContact);
  }
})();