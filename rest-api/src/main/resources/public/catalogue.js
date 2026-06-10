// ==========================
// ÉTAT GLOBAL
// ==========================
const TERM_TO_CODE = { winter: "H25", autumn: "A24", summer: "E24" };
const TERM_LABELS  = { winter: "H25", autumn: "A24", summer: "E24" };

let courses = [];
let visibleCount = 5;
let loadingInterval = null;

// ==========================
// UTILITAIRES
// ==========================
function starsHTML(value, max = 5) {
  const filled = Math.round(parseFloat(value));
  return `<span class="stars">${"★".repeat(filled)}${"☆".repeat(max - filled)}</span>`;
}

function setLoading(btnId, loading) {
  const btn = document.getElementById(btnId);
  if (!btn) return;
  if (loading) {
    btn.disabled = true;
    btn.dataset.original = btn.innerHTML;
    btn.innerHTML = '<span class="spinner"></span>' + btn.dataset.original;
  } else {
    btn.disabled = false;
    btn.innerHTML = btn.dataset.original || btn.textContent;
  }
}

function showGlobalLoading() {
  const loading = document.getElementById("global-loading");
  const dots = document.getElementById("global-dots");
  if (!loading || !dots) return;
  loading.style.display = "block";
  let count = 1;
  dots.textContent = ".";
  loadingInterval = setInterval(() => {
    count = (count % 3) + 1;
    dots.textContent = ".".repeat(count);
  }, 400);
}

function hideGlobalLoading() {
  const loading = document.getElementById("global-loading");
  if (loading) loading.style.display = "none";
  if (loadingInterval) { clearInterval(loadingInterval); loadingInterval = null; }
}

// ==========================
// DONNÉES AVIS / RÉSULTATS
// ==========================
async function getAvisAggreges(courseId) {
  try {
    const res = await fetch(`/api/avis/${courseId}`);
    if (!res.ok) return null;
    const avis = await res.json();
    if (!avis || avis.length === 0) return null;
    return {
      charge:       (avis.reduce((s, a) => s + a.charge, 0) / avis.length).toFixed(1),
      difficulte:   (avis.reduce((s, a) => s + a.difficulte, 0) / avis.length).toFixed(1),
      commentaires: avis.map(a => a.commentaire).filter(c => c?.trim().length > 0)
    };
  } catch (e) { return null; }
}

async function getResultatsAggreges(courseId) {
  try {
    const res = await fetch(`/results/${courseId}`);
    if (!res.ok) return null;
    const r = await res.json();
    return { moyenne: r.moyenne, score: r.score, participants: r.participants, trimestres: r.trimestres };
  } catch (e) { return null; }
}

// ==========================
// CHARGEMENT DES COURS
// ==========================
async function loadCourses() {
  showGlobalLoading();
  try {
    const res = await fetch("/courses");
    const raw = await res.json();

    if (Array.isArray(raw) && raw[0]?.id) {
      courses = raw;
    } else if (Array.isArray(raw) && raw[0]?.courses) {
      courses = await Promise.all(
        raw[0].courses.map(id => fetch(`/courses/${id}`).then(r => r.json()))
      );
    } else {
      throw new Error("Format catalogue invalide");
    }

    renderCatalogue();
    hideGlobalLoading();
    const btns = document.getElementById("pagination-buttons");
    if (btns) btns.style.display = "flex";
  } catch (err) {
    console.error("Erreur catalogue", err);
    hideGlobalLoading();
  }
}

// ==========================
// VOIR PLUS / MOINS
// ==========================
function afficherMoinsDeCours() {
  visibleCount = Math.max(5, visibleCount - 5);
  renderCatalogue();
}

function afficherPlusDeCours() {
  visibleCount += 5;
  renderCatalogue();
}

// ==========================
// AFFICHAGE + FILTRES
// ==========================
function renderCatalogue() {
  const container = document.getElementById("courses");
  const countEl   = document.getElementById("course-count");
  const query = searchInput.value.toLowerCase();
  const term  = termFilter.value;

  container.innerHTML = "";

  const filtered = courses.filter(c => {
    const matchText =
      c.id.toLowerCase().includes(query) ||
      c.name.toLowerCase().includes(query) ||
      (c.description || "").toLowerCase().includes(query);
    const matchTerm = term === "" || c.available_terms?.[term] === true;
    return matchText && matchTerm;
  });

  if (countEl) {
    countEl.textContent = filtered.length > 0
      ? `${Math.min(filtered.length, visibleCount)} cours affichés parmi ${filtered.length.toLocaleString('fr-FR')} cours disponibles`
      : "";
  }

  if (filtered.length === 0) {
    container.innerHTML = `
      <div class="empty-state">
        <p>Aucun cours trouvé${query ? ` pour « ${query} »` : ""}.</p>
      </div>`;
    return;
  }

  filtered.slice(0, visibleCount).forEach(c => {
    const div = document.createElement("div");
    div.className = "course";

    const availableTags = Object.entries(TERM_LABELS)
      .filter(([k]) => c.available_terms?.[k])
      .map(([, v]) => `<span class="term-tag available">${v}</span>`)
      .join("");

    div.innerHTML = `
      <div class="course-header">
        <h3>${c.id}</h3>
        <span class="credit-badge">${c.credits} cr.</span>
      </div>
      <p>${c.name}</p>
      <div class="term-tags">${availableTags || '<span class="term-tag unavailable">Non disponible</span>'}</div>
    `;

    div.onclick = () => {
      const semester = TERM_TO_CODE[term] || "H25";
      window.location.href = `course.html?id=${c.id}&semester=${semester}`;
    };

    container.appendChild(div);
  });
}

// ==========================
// COMPARAISON DE COURS
// ==========================
async function compareCourses() {
  const result = document.getElementById("comparison");
  result.innerHTML = "";
  setLoading("compareBtn", true);

  const input = document.getElementById("compareInput").value
    .toUpperCase().split(",").map(s => s.trim()).filter(Boolean);

  if (input.length < 2) {
    result.innerHTML = `<div class="error">Veuillez entrer au moins deux sigles de cours.</div>`;
    setLoading("compareBtn", false);
    return;
  }

  const selectedCourses = input.map(code => courses.find(c => c.id === code)).filter(Boolean);

  if (selectedCourses.length < 2) {
    result.innerHTML = `<div class="error">Au moins un sigle est invalide ou introuvable.</div>`;
    setLoading("compareBtn", false);
    return;
  }

  const [avisList, resultatsList] = await Promise.all([
    Promise.all(selectedCourses.map(c => getAvisAggreges(c.id))),
    Promise.all(selectedCourses.map(c => getResultatsAggreges(c.id)))
  ]);

  const html = `
    <h3>Comparaison des cours</h3>
    <table>
      <tr><th>Sigle</th>${selectedCourses.map(c => `<td><strong>${c.id}</strong></td>`).join("")}</tr>
      <tr><th>Nom</th>${selectedCourses.map(c => `<td>${c.name}</td>`).join("")}</tr>
      <tr><th>Crédits</th>${selectedCourses.map(c => `<td>${c.credits}</td>`).join("")}</tr>
      <tr><th>Description</th>${selectedCourses.map(c => `<td style="font-size:13px;text-align:left">${c.description || "—"}</td>`).join("")}</tr>
      <tr><th>Hiver</th>${selectedCourses.map(c => `<td>${c.available_terms?.winter ? "✔" : "—"}</td>`).join("")}</tr>
      <tr><th>Automne</th>${selectedCourses.map(c => `<td>${c.available_terms?.autumn ? "✔" : "—"}</td>`).join("")}</tr>
      <tr><th>Été</th>${selectedCourses.map(c => `<td>${c.available_terms?.summer ? "✔" : "—"}</td>`).join("")}</tr>
      <tr><th>Difficulté (avis)</th>${avisList.map(a => `<td>${a ? starsHTML(a.difficulte) + " " + a.difficulte + "/5" : "—"}</td>`).join("")}</tr>
      <tr><th>Charge (avis)</th>${avisList.map(a => `<td>${a ? starsHTML(a.charge) + " " + a.charge + "/5" : "—"}</td>`).join("")}</tr>
      <tr><th>Moyenne finale</th>${resultatsList.map(r => `<td>${r?.moyenne ?? "—"}</td>`).join("")}</tr>
      <tr><th>Score académique</th>${resultatsList.map(r => `<td>${r ? r.score + "/5" : "—"}</td>`).join("")}</tr>
      <tr><th>Participants</th>${resultatsList.map(r => `<td>${r?.participants ?? "—"}</td>`).join("")}</tr>
      <tr><th>Commentaires</th>${avisList.map(a => `
        <td>${a?.commentaires?.length
          ? `<ul class="avis-comments">${a.commentaires.map(c => `<li>${c}</li>`).join("")}</ul>`
          : "—"}</td>`).join("")}</tr>
    </table>
  `;

  setLoading("compareBtn", false);
  result.innerHTML = html;
}

// ==========================
// ÉLIGIBILITÉ
// ==========================
function checkEligibility() {
  const targetCourse = document.getElementById("targetCourse").value.trim().toUpperCase();
  const resultEl     = document.getElementById("eligibilityResult");

  if (!targetCourse) {
    resultEl.innerHTML = `<div class="error">Veuillez entrer un cours à vérifier.</div>`;
    return;
  }

  const completedCourses = document.getElementById("completedCourses").value
    .split(",").map(c => c.trim().toUpperCase()).filter(Boolean);
  const cycle = document.getElementById("cycle").value;

  setLoading("eligibilityBtn", true);
  resultEl.innerHTML = "";

  fetch(`/courses/${targetCourse}/eligibility`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ completedCourses, cycle })
  })
    .then(res => {
      if (!res.ok) throw new Error("Cours introuvable : " + targetCourse);
      return res.json();
    })
    .then(data => {
      setLoading("eligibilityBtn", false);
      renderEligibilityResult(data);
    })
    .catch(err => {
      setLoading("eligibilityBtn", false);
      resultEl.innerHTML = `<div class="error">${err.message}</div>`;
    });
}

function renderEligibilityResult(result) {
  const container = document.getElementById("eligibilityResult");
  if (result.eligible) {
    container.innerHTML = `<div class="success">${result.message}</div>`;
  } else {
    let prereqHtml = "";
    if (result.missingPrerequisites?.length) {
      prereqHtml = `<ul>${result.missingPrerequisites.map(p => `<li>${p}</li>`).join("")}</ul>`;
    }
    container.innerHTML = `<div class="error"><strong>${result.message}</strong>${prereqHtml}</div>`;
  }
}

// ==========================
// CONFLITS D'HORAIRES
// ==========================
async function fetchCourseWithSchedule(courseId, term) {
  const semester = TERM_TO_CODE[term];
  const res = await fetch(`/courses/${courseId}?include_schedule=true&schedule_semester=${semester}`);
  if (!res.ok) throw new Error("Cours introuvable : " + courseId);
  return await res.json();
}

function extractActivities(course) {
  const activities = [];
  const seen = new Set();
  if (!Array.isArray(course.schedules)) return [];
  course.schedules.forEach(s => {
    s.sections.forEach(section => {
      section.volets.forEach(volet => {
        volet.activities.forEach(a => {
          const key = `${section.name}-${volet.name}-${a.days}-${a.start_time}-${a.end_time}`;
          if (!seen.has(key)) {
            seen.add(key);
            activities.push({
              course: course.id, section: section.name, type: volet.name,
              day: a.days[0], start: a.start_time, end: a.end_time,
              room: a.room, pavillon: a.pavillon_name
            });
          }
        });
      });
    });
  });
  return activities;
}

function keepOneSection(list) {
  if (list.length === 0) return [];
  const section = list[0].section;
  return list.filter(a => a.section === section);
}

function renderConflictSchedules(list1, list2) {
  let html = "<h3>Horaires des cours</h3>";
  [list1, list2].forEach(list => {
    if (list.length === 0) return;
    html += `<h4>${list[0].course}</h4><ul>`;
    list.forEach(a => {
      html += `<li>${a.day} ${a.start}–${a.end} (${a.type}, section ${a.section}) – ${a.pavillon} ${a.room}</li>`;
    });
    html += "</ul>";
  });
  document.getElementById("conflictSchedules").innerHTML = html;
}

function overlap(a, b) {
  return a.day === b.day && a.start < b.end && b.start < a.end;
}

async function checkConflictBetweenTwoCourses() {
  const input = document.getElementById("conflictInput").value
    .toUpperCase().split(",").map(s => s.trim()).filter(Boolean);
  const term         = document.getElementById("conflictTerm").value;
  const schedulesDiv = document.getElementById("conflictSchedules");
  const resultDiv    = document.getElementById("conflictResult");

  schedulesDiv.innerHTML = "";
  resultDiv.innerHTML = "";

  if (input.length !== 2) {
    resultDiv.innerHTML = `<div class="error">Veuillez entrer exactement deux cours.</div>`;
    return;
  }

  setLoading("conflictBtn", true);

  try {
    const [c1, c2] = await Promise.all([
      fetchCourseWithSchedule(input[0], term),
      fetchCourseWithSchedule(input[1], term)
    ]);

    const a1 = keepOneSection(extractActivities(c1));
    const a2 = keepOneSection(extractActivities(c2));

    renderConflictSchedules(a1, a2);

    const conflicts = [];
    a1.forEach(x => a2.forEach(y => { if (overlap(x, y)) conflicts.push({ x, y }); }));

    if (conflicts.length === 0) {
      resultDiv.innerHTML = `<div class="success">Aucun conflit d'horaire détecté.</div>`;
    } else {
      resultDiv.innerHTML = `
        <div class="error">
          <strong>Conflits détectés (${conflicts.length})</strong>
          <ul>${conflicts.map(c => `<li>${c.x.day} ${c.x.start}–${c.x.end} ↔ ${c.y.start}–${c.y.end}</li>`).join("")}</ul>
        </div>`;
    }
  } catch (e) {
    resultDiv.innerHTML = `<div class="error">${e.message}</div>`;
  }

  setLoading("conflictBtn", false);
}

// ==========================
// EVENTS
// ==========================
searchInput.addEventListener("input", renderCatalogue);
termFilter.addEventListener("change", renderCatalogue);

document.addEventListener("DOMContentLoaded", () => {
  document.getElementById("compareInput")?.addEventListener("keydown", e => {
    if (e.key === "Enter") compareCourses();
  });
  document.getElementById("conflictInput")?.addEventListener("keydown", e => {
    if (e.key === "Enter") checkConflictBetweenTwoCourses();
  });
  document.getElementById("targetCourse")?.addEventListener("keydown", e => {
    if (e.key === "Enter") checkEligibility();
  });
  document.getElementById("programId")?.addEventListener("keydown", e => {
    if (e.key === "Enter") goToProgramPage();
  });
});

// ==========================
// START
// ==========================
loadCourses();
