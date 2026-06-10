// ==========================
// UTILITAIRE URL
// ==========================
function getProgramIdFromURL() {
  const params = new URLSearchParams(window.location.search);
  return params.get("id");
}

// ==========================
// CHARGEMENT PROGRAMME
// ==========================
async function loadProgram() {
  const programId = getProgramIdFromURL();

  const infoDiv = document.getElementById("programInfo");
  const list = document.getElementById("programCourses");

  infoDiv.innerHTML = "";
  list.innerHTML = "";

  if (!programId) {
    infoDiv.innerHTML =
      "<p style='color:red'>Aucun identifiant de programme fourni.</p>";
    return;
  }

  try {
    const res = await fetch(`/programs/${programId}`);
    if (!res.ok) throw new Error("Programme introuvable");

    const raw = await res.json();

    console.log("JSON reçu :", raw);

    // ==========================
    // NORMALISATION DU FORMAT
    // ==========================
    let program;

    if (Array.isArray(raw)) {
      program = raw[0];
    } else if (Array.isArray(raw.program)) {
      program = raw.program[0];
    } else if (Array.isArray(raw.data)) {
      program = raw.data[0];
    } else {
      program = raw;
    }

    if (!program || !Array.isArray(program.segments)) {
      throw new Error("Format du programme inattendu");
    }

    renderProgram(program);

  } catch (err) {
    infoDiv.innerHTML = `
      <p style="color:red">❌ ${err.message}</p>
    `;
  }
}

// ==========================
// AFFICHAGE PROGRAMME
// ==========================
function renderProgram(program) {
  const infoDiv = document.getElementById("programInfo");
  const list = document.getElementById("programCourses");

  infoDiv.innerHTML = `
    <h2>${program.name}</h2>
    <p>ID : ${program.id}</p>
  `;

  program.segments.forEach(segment => {
    const segLi = document.createElement("li");
    segLi.innerHTML = `<strong>${segment.name}</strong>`;
    list.appendChild(segLi);

    if (!Array.isArray(segment.blocs)) return;

    segment.blocs.forEach(bloc => {
      const blocLi = document.createElement("li");
      blocLi.style.marginLeft = "20px";
      blocLi.textContent = `${bloc.id} — ${bloc.type}`;
      list.appendChild(blocLi);

      if (!Array.isArray(bloc.courses)) return;

      bloc.courses.forEach(course => {
        const courseLi = document.createElement("li");
        courseLi.style.marginLeft = "40px";
        courseLi.textContent = `• ${course}`;
        list.appendChild(courseLi);
      });
    });
  });
}

// ==========================
// START
// ==========================
loadProgram();