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
    const raw = textarea.value.trim();

    if (!raw) {
      setFeedback('Inserisci un JSON prima di inviare.', 'error');
      return;
    }

    let parsed;
    try {
      parsed = JSON.parse(raw);
    } catch (e) {
      setFeedback(
        'Il JSON non è valido. Controlla virgolette, virgole e parentesi!',
        'error'
      );
      return;
    }

    submitBtn.disabled = true;
    setFeedback('Invio in corso...', 'info');

    try {
      const response = await fetch('/api/contact-me', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(parsed)
      });

      if (response.ok) {
        setFeedback('Messaggio inviato correttamente, grazie!', 'success');
      } else if (response.status === 400) {
        let errorText = 'Richiesta non valida.';
        try {
          const data = await response.json();
          if (data && Array.isArray(data.violations)) {
            errorText = data.violations.map(v => v.message).join(' | ');
          }
        } catch (e) {
          // lascio il generico
        }
        setFeedback(errorText, 'error');
      } else {
        setFeedback(
          'Errore inatteso. Riprova più tardi.',
          'error'
        );
      }
    } catch (e) {
      setFeedback(
        'Impossibile contattare il server. Sei connesso a internet?',
        'error'
      );
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