// ==========================
// UTILITAIRES
// ==========================

/** Lit un paramètre de l'URL (ex: ?id=IFT2255 → "IFT2255") */
function getParam(name) {
  return new URLSearchParams(window.location.search).get(name);
}

/** Génère des étoiles HTML à partir d'une note (ex: 3.5 → ★★★★☆) */
function starsHTML(value, max = 5) {
  const filled = Math.round(parseFloat(value));
  return `<span class="stars">${"★".repeat(filled)}${"☆".repeat(max - filled)}</span>`;
}

/** Échappe les caractères HTML pour éviter les injections XSS */
function escapeHtml(text) {
  return String(text)
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;");
}

// ==========================
// AVIS ÉTUDIANTS
// ==========================
async function loadAvis(courseId) {
  try {
    const res = await fetch(`/api/avis/${courseId}`);
    const allAvis = await res.json();
    const avisDuCours = allAvis.filter(
      a => a.cours.toUpperCase() === courseId.toUpperCase()
    );
    renderAvis(avisDuCours);
  } catch (err) {
    console.error("Erreur chargement avis", err);
  }
}

function renderAvis(avis) {
  let html = `<h3>Avis étudiants</h3>`;

  if (!avis || avis.length === 0) {
    html += `<p class="muted">Aucun avis pour ce cours.</p>`;
  } else {
    const avgDiff   = avis.reduce((s, a) => s + a.difficulte, 0) / avis.length;
    const avgCharge = avis.reduce((s, a) => s + a.charge, 0) / avis.length;

    html += `
      <div class="avis-summary-box">
        <div class="avis-stat">
          <span class="avis-stat-value">${avgDiff.toFixed(1)}</span>
          ${starsHTML(avgDiff)}
          <span class="avis-stat-label">Difficulté / 5</span>
        </div>
        <div class="avis-stat">
          <span class="avis-stat-value">${avgCharge.toFixed(1)}</span>
          ${starsHTML(avgCharge)}
          <span class="avis-stat-label">Charge / 5</span>
        </div>
        <div class="avis-stat">
          <span class="avis-stat-value">${avis.length}</span>
          <span class="avis-stat-label">avis au total</span>
        </div>
      </div>
      <div class="avis-list" id="avis-list">
        ${avis.map(a => renderAvisCard(a)).join("")}
      </div>
    `;
  }

  document.getElementById("course-details").insertAdjacentHTML("beforeend", html);
}

function renderAvisCard(a) {
  return `
    <div class="avis-card">
      <div class="avis-meta">
        <span>${starsHTML(a.difficulte)} Difficulté ${a.difficulte}/5</span>
        <span>${starsHTML(a.charge)} Charge ${a.charge}/5</span>
        ${a.auteur ? `<span class="muted">${a.auteur}</span>` : ""}
      </div>
      <div class="avis-commentaire">${a.commentaire ? escapeHtml(a.commentaire) : "<em>Pas de commentaire</em>"}</div>
    </div>
  `;
}

// ==========================
// CHARGEMENT DU COURS
// ==========================
async function loadCourse() {
  const courseId = getParam("id");
  const semester = getParam("semester");

  if (!courseId) {
    document.getElementById("course-details").innerHTML =
      `<div class="error">Identifiant de cours manquant.</div>`;
    return;
  }

  try {
    let url = `/courses/${courseId}?include_schedule=true`;
    if (semester) url += `&schedule_semester=${semester}`;

    const response = await fetch(url);
    if (!response.ok) throw new Error("Cours introuvable");
    const course = await response.json();
    renderCourse(course);
  } catch (err) {
    console.error(err);
    document.getElementById("course-details").innerHTML =
      `<div class="error">Erreur lors du chargement du cours.</div>`;
  }
}

// ==========================
// AFFICHAGE DU COURS
// ==========================
function renderCourse(c) {
  const container = document.getElementById("course-details");

  const prereqs = c.prerequisite_courses?.length
    ? c.prerequisite_courses.join(", ")
    : "Aucun";

  const equivalents = c.equivalent_courses?.length
    ? c.equivalent_courses.join(", ")
    : "Aucun";

  let html = `
    <h2>${c.id} – ${c.name}</h2>
    <p><strong>Crédits :</strong> ${c.credits}</p>
    <p style="color:var(--muted); line-height:1.6">${c.description || ""}</p>

    <h3>Conditions</h3>
    <p><strong>Prérequis :</strong> ${prereqs}</p>
    <p><strong>Équivalents :</strong> ${equivalents}</p>
  `;

  if (Array.isArray(c.schedules) && c.schedules.length > 0) {
    html += `<h3>Horaires</h3>`;

    c.schedules.forEach(schedule => {
      html += `<div class="schedule"><h4>Trimestre ${schedule.semester}</h4>`;

      schedule.sections.forEach(section => {
        html += `
          <div class="section">
            <h5>Section ${section.name}</h5>
            <p>
              <strong>Enseignant(s) :</strong> ${section.teachers?.join(", ") || "—"}<br>
              <strong>Capacité :</strong> ${section.capacity ?? "—"}
            </p>
        `;

        section.volets.forEach(volet => {
          html += `<div class="volet"><strong>${volet.name}</strong>`;

          volet.activities.forEach(act => {
            html += `
              <div class="activity">
                <p>
                  ${act.days?.join(", ")} &nbsp;${act.start_time} – ${act.end_time}<br>
                  ${act.pavillon_name} – Salle ${act.room}<br>
                  ${act.campus} | Mode ${act.mode}
                </p>
              </div>`;
          });

          html += `</div>`;
        });

        html += `</div>`;
      });

      html += `</div>`;
    });
  } else {
    html += `<h3>Horaires</h3><p class="muted">Aucun horaire disponible pour ce trimestre.</p>`;
  }

  container.innerHTML = html;
  loadAvis(c.id);
  loadResultats(c.id);
}

// ==========================
// RÉSULTATS ACADÉMIQUES
// ==========================
async function loadResultats(courseId) {
  try {
    const res = await fetch(`/results/${courseId}`);
    if (!res.ok) { renderResultatsErreur(); return; }
    const r = await res.json();
    renderResultats(r);
  } catch (err) {
    renderResultatsErreur();
  }
}

function renderResultats(r) {
  const html = `
    <h3>Résultats académiques</h3>
    <div class="resultats-grid">
      <div class="resultat-item">
        <div class="resultat-value">${r.moyenne}</div>
        <div class="resultat-label">Moyenne finale</div>
      </div>
      <div class="resultat-item">
        <div class="resultat-value">${r.score}/5</div>
        <div class="resultat-label">Score</div>
      </div>
      <div class="resultat-item">
        <div class="resultat-value">${r.participants}</div>
        <div class="resultat-label">Participants</div>
      </div>
      <div class="resultat-item">
        <div class="resultat-value">${r.trimestres}</div>
        <div class="resultat-label">Trimestres analysés</div>
      </div>
    </div>
  `;
  document.getElementById("course-details").insertAdjacentHTML("beforeend", html);
}

function renderResultatsErreur() {
  const html = `
    <h3>Résultats académiques</h3>
    <p class="muted">Aucun résultat académique disponible pour ce cours.</p>
  `;
  document.getElementById("course-details").insertAdjacentHTML("beforeend", html);
}

// ==========================
// DÉMARRAGE
// ==========================
loadCourse();
