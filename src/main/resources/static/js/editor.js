// Level Editor JavaScript
const API_BASE = "/api/levels";

// State
let currentLevelId = null;
let currentLevel = null;
let profileRelationships = [];
let profileVulnerabilities = [];

// ========== INITIALIZATION ==========

document.addEventListener("DOMContentLoaded", () => {
  loadLevels();
  setupFormHandlers();
  setupTagInputs();
});

function setupFormHandlers() {
  // Level form
  document.getElementById("levelForm").addEventListener("submit", saveLevel);

  // Defender Profile form
  document
    .getElementById("defenderProfileForm")
    .addEventListener("submit", saveDefenderProfile);

  // Attack Scenario form
  document
    .getElementById("attackScenarioForm")
    .addEventListener("submit", saveAttackScenario);

  // Attack Option form
  document
    .getElementById("attackOptionForm")
    .addEventListener("submit", saveAttackOption);

  // Defender Choice form
  document
    .getElementById("defenderChoiceForm")
    .addEventListener("submit", saveDefenderChoice);
}

function setupTagInputs() {
  // Relationships
  document
    .getElementById("relationshipInput")
    .addEventListener("keydown", (e) => {
      if (e.key === "Enter") {
        e.preventDefault();
        const value = e.target.value.trim();
        if (value && !profileRelationships.includes(value)) {
          profileRelationships.push(value);
          renderTags("relationshipTags", profileRelationships, "relationship");
          e.target.value = "";
        }
      }
    });

  // Vulnerabilities
  document
    .getElementById("vulnerabilityInput")
    .addEventListener("keydown", (e) => {
      if (e.key === "Enter") {
        e.preventDefault();
        const value = e.target.value.trim();
        if (value && !profileVulnerabilities.includes(value)) {
          profileVulnerabilities.push(value);
          renderTags(
            "vulnerabilityTags",
            profileVulnerabilities,
            "vulnerability",
          );
          e.target.value = "";
        }
      }
    });
}

function renderTags(containerId, tags, type) {
  const container = document.getElementById(containerId);
  container.innerHTML = tags
    .map(
      (tag, index) => `
        <span class="tag">
            ${tag}
            <span class="remove-tag" onclick="removeTag('${type}', ${index})">&times;</span>
        </span>
    `,
    )
    .join("");
}

function removeTag(type, index) {
  if (type === "relationship") {
    profileRelationships.splice(index, 1);
    renderTags("relationshipTags", profileRelationships, "relationship");
  } else if (type === "vulnerability") {
    profileVulnerabilities.splice(index, 1);
    renderTags("vulnerabilityTags", profileVulnerabilities, "vulnerability");
  }
}

// ========== LEVELS ==========

async function loadLevels() {
  try {
    const response = await fetch(API_BASE);
    const levels = await response.json();
    renderLevelList(levels);
  } catch (error) {
    console.error("Error loading levels:", error);
  }
}

function renderLevelList(levels) {
  const container = document.getElementById("levelList");

  if (levels.length === 0) {
    container.innerHTML =
      '<div class="empty-state"><p>No levels yet. Create one!</p></div>';
    return;
  }

  container.innerHTML = levels
    .map(
      (level) => `
        <div class="level-item ${currentLevelId === level.id ? "active" : ""}" 
             data-level-id="${level.id}"
             onclick="selectLevel(${level.id})">
            <h4>${level.name}</h4>
            <div class="level-meta">
                <span class="badge ${level.enabled ? "enabled" : "disabled"}">
                    ${level.enabled ? "Active" : "Disabled"}
                </span>
                <span class="badge">${level.difficulty}</span>
            </div>
        </div>
    `,
    )
    .join("");
}

async function selectLevel(id) {
  try {
    const response = await fetch(`${API_BASE}/${id}`);
    currentLevel = await response.json();
    currentLevelId = id;

    // Update sidebar selection
    document
      .querySelectorAll(".level-item")
      .forEach((item) => item.classList.remove("active"));

    // Find and highlight the selected level item
    const selectedItem = document.querySelector(
      `.level-item[data-level-id="${id}"]`,
    );
    if (selectedItem) {
      selectedItem.classList.add("active");
    }

    // Show panels
    showLevelPanels();
    populateLevelForm(currentLevel);
    renderDefenderProfiles(currentLevel.defenderProfiles || []);
    renderAttackScenarios(currentLevel.attackScenarios || []);
  } catch (error) {
    console.error("Error loading level:", error);
  }
}

function showLevelPanels() {
  document.getElementById("levelInfoPanel").classList.remove("hidden");
  document.getElementById("defenderProfilesPanel").classList.remove("hidden");
  document.getElementById("attackScenariosPanel").classList.remove("hidden");
}

function populateLevelForm(level) {
  document.getElementById("levelId").value = level.id || "";
  document.getElementById("levelName").value = level.name || "";
  document.getElementById("levelDescription").value = level.description || "";
  document.getElementById("levelDifficulty").value =
    level.difficulty || "MEDIUM";
  document.getElementById("levelOrder").value = level.orderIndex || 0;
  document.getElementById("levelEnabled").checked = level.enabled !== false;
}

function createNewLevel() {
  currentLevelId = null;
  currentLevel = null;

  // Clear selection
  document
    .querySelectorAll(".level-item")
    .forEach((item) => item.classList.remove("active"));

  // Show and clear form
  document.getElementById("levelInfoPanel").classList.remove("hidden");
  document.getElementById("defenderProfilesPanel").classList.add("hidden");
  document.getElementById("attackScenariosPanel").classList.add("hidden");

  populateLevelForm({});
  document.getElementById("levelName").focus();
}

async function saveLevel(e) {
  e.preventDefault();

  const levelData = {
    name: document.getElementById("levelName").value,
    description: document.getElementById("levelDescription").value,
    difficulty: document.getElementById("levelDifficulty").value,
    orderIndex: parseInt(document.getElementById("levelOrder").value) || 0,
    enabled: document.getElementById("levelEnabled").checked,
  };

  try {
    let response;
    if (currentLevelId) {
      response = await fetch(`${API_BASE}/${currentLevelId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(levelData),
      });
    } else {
      response = await fetch(API_BASE, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(levelData),
      });
    }

    const savedLevel = await response.json();
    currentLevelId = savedLevel.id;
    currentLevel = savedLevel;

    loadLevels();
    showLevelPanels();
    renderDefenderProfiles([]);
    renderAttackScenarios([]);

    alert("Level saved successfully!");
  } catch (error) {
    console.error("Error saving level:", error);
    alert("Error saving level");
  }
}

async function deleteCurrentLevel() {
  if (!currentLevelId) return;

  if (
    !confirm(
      "Are you sure you want to delete this level? This will delete all profiles, scenarios, and choices within it.",
    )
  ) {
    return;
  }

  try {
    await fetch(`${API_BASE}/${currentLevelId}`, { method: "DELETE" });
    currentLevelId = null;
    currentLevel = null;

    document.getElementById("levelInfoPanel").classList.add("hidden");
    document.getElementById("defenderProfilesPanel").classList.add("hidden");
    document.getElementById("attackScenariosPanel").classList.add("hidden");

    loadLevels();
  } catch (error) {
    console.error("Error deleting level:", error);
    alert("Error deleting level");
  }
}

// ========== DEFENDER PROFILES ==========

function renderDefenderProfiles(profiles) {
  const container = document.getElementById("defenderProfilesList");

  if (profiles.length === 0) {
    container.innerHTML =
      '<div class="empty-state"><p>No defender profiles. Add one to define who can be targeted.</p></div>';
    return;
  }

  container.innerHTML = profiles
    .map(
      (profile) => `
        <div class="item-card">
            <div class="item-card-header">
                <h4>
                    <span class="avatar">${profile.avatarIcon || "👤"}</span>
                    ${profile.name}
                </h4>
                <div class="item-card-actions">
                    <button class="btn btn-icon btn-secondary" onclick="editDefenderProfile(${
                      profile.id
                    })">✎</button>
                    <button class="btn btn-icon btn-danger" onclick="deleteDefenderProfile(${
                      profile.id
                    })">✕</button>
                </div>
            </div>
            <div class="item-card-body">
                <p>${profile.description || "No description"}</p>
                <p><strong>Age:</strong> ${profile.age} (${
                  profile.ageGroup
                }) | <strong>Occupation:</strong> ${profile.occupation || "N/A"}</p>
                <p><strong>Tech Savviness:</strong> ${
                  profile.techSavviness
                } | <strong>Mental State:</strong> ${profile.mentalState}</p>
            </div>
            <div class="item-card-meta">
                ${(profile.relationships || [])
                  .map((r) => `<span class="badge">${r}</span>`)
                  .join("")}
            </div>
        </div>
    `,
    )
    .join("");
}

function addDefenderProfile() {
  if (!currentLevelId) {
    alert("Please save the level first");
    return;
  }

  document.getElementById("profileModalTitle").textContent =
    "Add Defender Profile";
  document.getElementById("profileId").value = "";
  document.getElementById("defenderProfileForm").reset();
  profileRelationships = [];
  profileVulnerabilities = [];
  renderTags("relationshipTags", [], "relationship");
  renderTags("vulnerabilityTags", [], "vulnerability");

  openModal("defenderProfileModal");
}

function editDefenderProfile(id) {
  const profile = currentLevel.defenderProfiles.find((p) => p.id === id);
  if (!profile) return;

  document.getElementById("profileModalTitle").textContent =
    "Edit Defender Profile";
  document.getElementById("profileId").value = profile.id;
  document.getElementById("profileName").value = profile.name || "";
  document.getElementById("profileDescription").value =
    profile.description || "";
  document.getElementById("profileAge").value = profile.age || 25;
  document.getElementById("profileAgeGroup").value =
    profile.ageGroup || "YOUNG";
  document.getElementById("profileOccupation").value = profile.occupation || "";
  document.getElementById("profileTechSavviness").value =
    profile.techSavviness || "MEDIUM";
  document.getElementById("profileMentalState").value =
    profile.mentalState || "CALM";
  document.getElementById("profileFinancialStatus").value =
    profile.financialStatus || "STABLE";
  document.getElementById("profileAvatar").value = profile.avatarIcon || "👨‍🎓";

  profileRelationships = [...(profile.relationships || [])];
  profileVulnerabilities = [...(profile.vulnerabilities || [])];
  renderTags("relationshipTags", profileRelationships, "relationship");
  renderTags("vulnerabilityTags", profileVulnerabilities, "vulnerability");

  openModal("defenderProfileModal");
}

async function saveDefenderProfile(e) {
  e.preventDefault();

  const profileId = document.getElementById("profileId").value;
  const profileData = {
    name: document.getElementById("profileName").value,
    description: document.getElementById("profileDescription").value,
    age: parseInt(document.getElementById("profileAge").value) || 25,
    ageGroup: document.getElementById("profileAgeGroup").value,
    occupation: document.getElementById("profileOccupation").value,
    techSavviness: document.getElementById("profileTechSavviness").value,
    mentalState: document.getElementById("profileMentalState").value,
    financialStatus: document.getElementById("profileFinancialStatus").value,
    avatarIcon: document.getElementById("profileAvatar").value,
    relationships: profileRelationships,
    vulnerabilities: profileVulnerabilities,
  };

  try {
    let response;
    if (profileId) {
      response = await fetch(`${API_BASE}/defender-profiles/${profileId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(profileData),
      });
    } else {
      response = await fetch(
        `${API_BASE}/${currentLevelId}/defender-profiles`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(profileData),
        },
      );
    }

    closeModal("defenderProfileModal");
    selectLevel(currentLevelId);
  } catch (error) {
    console.error("Error saving profile:", error);
    alert("Error saving profile");
  }
}

async function deleteDefenderProfile(id) {
  if (!confirm("Delete this defender profile?")) return;

  try {
    const response = await fetch(`${API_BASE}/defender-profiles/${id}`, {
      method: "DELETE",
    });
    if (!response.ok) {
      const errorText = await response.text();
      console.error("Delete failed:", errorText);
      alert(`Failed to delete defender profile: ${errorText}`);
      return;
    }
    await selectLevel(currentLevelId);
  } catch (error) {
    console.error("Error deleting profile:", error);
    alert(`Error deleting defender profile: ${error.message}`);
  }
}

// ========== ATTACK SCENARIOS ==========

function renderAttackScenarios(scenarios) {
  const container = document.getElementById("attackScenariosList");

  if (scenarios.length === 0) {
    container.innerHTML =
      '<div class="empty-state"><p>No attack scenarios. Add one to define attack types.</p></div>';
    return;
  }

  container.innerHTML = scenarios
    .map(
      (scenario) => `
        <div class="scenario-card" id="scenario-${scenario.id}">
            <div class="scenario-card-header" onclick="toggleScenario(${
              scenario.id
            })">
                <h4>
                    <span class="attack-type-badge">${
                      scenario.attackType
                    }</span>
                    ${scenario.name}
                </h4>
                <div style="display: flex; align-items: center; gap: 0.5rem;">
                    <button class="btn btn-icon btn-secondary" onclick="event.stopPropagation(); editAttackScenario(${
                      scenario.id
                    })">✎</button>
                    <button class="btn btn-icon btn-danger" onclick="event.stopPropagation(); deleteAttackScenario(${
                      scenario.id
                    })">✕</button>
                    <span class="toggle-icon">▼</span>
                </div>
            </div>
            <div class="scenario-card-body">
                <div class="scenario-description">
                    <p><strong>Description:</strong> ${
                      scenario.description || "N/A"
                    }</p>
                    <p><strong>Attacker Narrative:</strong> ${
                      scenario.attackerNarrative || "N/A"
                    }</p>
                </div>
                <div class="attack-options-section">
                    <h5>
                        Attack Options
                        <button class="btn btn-small btn-secondary" onclick="addAttackOption(${
                          scenario.id
                        })">+ Add Option</button>
                    </h5>
                    ${renderAttackOptions(
                      scenario.attackOptions || [],
                      scenario.id,
                    )}
                </div>
            </div>
        </div>
    `,
    )
    .join("");
}

function toggleScenario(id) {
  const card = document.getElementById(`scenario-${id}`);
  card.classList.toggle("expanded");
}

function renderAttackOptions(options, scenarioId) {
  if (options.length === 0) {
    return '<div class="empty-state"><p>No attack options. Add options like "Call as Mother".</p></div>';
  }

  return options
    .map(
      (option) => `
        <div class="attack-option-card" id="option-${option.id}">
            <div class="attack-option-header" onclick="toggleOption(${
              option.id
            })">
                <h6>🎯 ${option.label}</h6>
                <div style="display: flex; align-items: center; gap: 0.5rem;">
                    <span class="badge">Risk: ${option.riskLevel}/5</span>
                    ${
                      option.criticalRisk
                        ? '<span class="badge" style="background: rgba(255,100,100,0.2); color: #ff6464;">⚠ Critical</span>'
                        : ""
                    }
                    <button class="btn btn-icon btn-secondary" onclick="event.stopPropagation(); editAttackOption(${
                      option.id
                    }, ${scenarioId})">✎</button>
                    <button class="btn btn-icon btn-danger" onclick="event.stopPropagation(); deleteAttackOption(${
                      option.id
                    })">✕</button>
                </div>
            </div>
            <div class="attack-option-body">
                <div class="attack-option-info">
                    <p><strong>Impersonating:</strong> ${
                      option.impersonatedEntity || "N/A"
                    }</p>
                    <p><strong>Message:</strong> ${
                      option.attackerMessage || "N/A"
                    }</p>
                    <p><strong>Base Points:</strong> ${
                      option.baseAttackerPoints
                    }</p>
                </div>
                <div class="defender-choices-section">
                    <h6>
                        Defender Choices
                        <button class="btn btn-small btn-secondary" onclick="addDefenderChoice(${
                          option.id
                        })">+ Add Choice</button>
                    </h6>
                    ${renderDefenderChoices(
                      option.defenderChoices || [],
                      option.id,
                    )}
                </div>
            </div>
        </div>
    `,
    )
    .join("");
}

function toggleOption(id) {
  const card = document.getElementById(`option-${id}`);
  card.classList.toggle("expanded");
}

function findAttackOptionById(id) {
  if (!id || !currentLevel) return null;
  for (const scenario of currentLevel.attackScenarios || []) {
    const option = (scenario.attackOptions || []).find((o) => o.id === id);
    if (option) return option;
  }
  return null;
}

function getAllAttackOptions() {
  if (!currentLevel) return [];
  const options = [];
  for (const scenario of currentLevel.attackScenarios || []) {
    for (const option of scenario.attackOptions || []) {
      options.push({
        id: option.id,
        label: option.label,
        scenarioName: scenario.name,
        description: option.description,
      });
    }
  }
  return options;
}

function renderDefenderChoices(choices, optionId, depth = 0) {
  if (!choices.length) {
    return `
      <div class="empty-state">
        <p>No defender choices. Add responses the defender can make.</p>
      </div>
    `;
  }

  return choices
    .map((choice) => {
      const followUpAttack = findAttackOptionById(
        choice.followUpAttackOptionId,
      );
      const hasFollowUp = choice.followUpAttackOptionId && followUpAttack;

      const indent = depth * 20;

      return `
        <div class="defender-choice-item"
             style="
               margin-left: ${indent}px;
               border-left: ${depth > 0 ? "3px solid rgba(0, 255, 255, 0.3)" : "none"};
               padding-left: ${depth > 0 ? "10px" : "0"};
             ">
          
          <div class="choice-info">
            <h6>
              ${depth > 0 ? "↳ " : ""}${choice.label}

              <span class="choice-type-badge ${
                choice.choiceType?.toLowerCase() || "neutral"
              }">
                ${choice.choiceType || "NEUTRAL"}
              </span>

              ${
                choice.criticallyWrong
                  ? '<span class="choice-type-badge wrong">💀 Fatal</span>'
                  : ""
              }

              ${
                choice.criticallyRight
                  ? '<span class="choice-type-badge correct">⭐ Perfect</span>'
                  : ""
              }

              ${
                hasFollowUp
                  ? '<span class="choice-type-badge" style="background: rgba(100, 200, 255, 0.2); color: #64c8ff;">🔗 Has Follow-up</span>'
                  : ""
              }

              ${
                !choice.endsScenario && !hasFollowUp
                  ? '<span class="choice-type-badge" style="background: rgba(255, 165, 0, 0.2); color: #ffa500;">⚠ No End/Follow-up</span>'
                  : ""
              }
            </h6>

            <p>${choice.description || choice.outcome || "No description"}</p>

            <div class="score-display">
              Defender:
              <span class="${
                choice.defenderScoreDelta >= 0 ? "positive" : "negative"
              }">
                ${choice.defenderScoreDelta >= 0 ? "+" : ""}
                ${choice.defenderScoreDelta}
              </span>
              |
              Attacker:
              <span class="${
                choice.attackerScoreDelta >= 0 ? "positive" : "negative"
              }">
                ${choice.attackerScoreDelta >= 0 ? "+" : ""}
                ${choice.attackerScoreDelta}
              </span>
            </div>

            ${
              hasFollowUp
                ? `
                <div class="follow-up-indicator"
                     style="margin-top: .5rem; padding: .5rem; background: rgba(100,200,255,.1); border-left: 3px solid #64c8ff;">
                  <strong>→ Leads to:</strong> ${followUpAttack.label}
                  <small style="display:block;color:#888;margin-top:.25rem;">
                    ${followUpAttack.description || "Next attack in decision tree"}
                  </small>
                </div>
              `
                : ""
            }
          </div>

          <div class="item-card-actions">
            <button class="btn btn-icon btn-secondary"
              onclick="editDefenderChoice(${choice.id}, ${optionId})">✎</button>

            <button class="btn btn-icon btn-danger"
              onclick="deleteDefenderChoice(${choice.id})">✕</button>
          </div>
        </div>
      `;
    })
    .join("");
}

// ========== ATTACK SCENARIO CRUD ==========

function addAttackScenario() {
  if (!currentLevelId) {
    alert("Please save the level first");
    return;
  }

  document.getElementById("scenarioModalTitle").textContent =
    "Add Attack Scenario";
  document.getElementById("scenarioId").value = "";
  document.getElementById("attackScenarioForm").reset();

  openModal("attackScenarioModal");
}

function editAttackScenario(id) {
  const scenario = currentLevel.attackScenarios.find((s) => s.id === id);
  if (!scenario) return;

  document.getElementById("scenarioModalTitle").textContent =
    "Edit Attack Scenario";
  document.getElementById("scenarioId").value = scenario.id;
  document.getElementById("scenarioName").value = scenario.name || "";
  document.getElementById("scenarioAttackType").value =
    scenario.attackType || "FAKE_CALL";
  document.getElementById("scenarioDescription").value =
    scenario.description || "";
  document.getElementById("scenarioNarrative").value =
    scenario.attackerNarrative || "";

  openModal("attackScenarioModal");
}

async function saveAttackScenario(e) {
  e.preventDefault();

  const scenarioId = document.getElementById("scenarioId").value;
  const scenarioData = {
    name: document.getElementById("scenarioName").value,
    attackType: document.getElementById("scenarioAttackType").value,
    description: document.getElementById("scenarioDescription").value,
    attackerNarrative: document.getElementById("scenarioNarrative").value,
  };

  try {
    if (scenarioId) {
      await fetch(`${API_BASE}/attack-scenarios/${scenarioId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(scenarioData),
      });
    } else {
      await fetch(`${API_BASE}/${currentLevelId}/attack-scenarios`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(scenarioData),
      });
    }

    closeModal("attackScenarioModal");
    selectLevel(currentLevelId);
  } catch (error) {
    console.error("Error saving scenario:", error);
    alert("Error saving scenario");
  }
}

async function deleteAttackScenario(id) {
  if (!confirm("Delete this attack scenario and all its options?")) return;

  try {
    const response = await fetch(`${API_BASE}/attack-scenarios/${id}`, {
      method: "DELETE",
    });
    if (!response.ok) {
      const errorText = await response.text();
      console.error("Delete failed:", errorText);
      alert(`Failed to delete scenario: ${errorText}`);
      return;
    }
    await selectLevel(currentLevelId);
  } catch (error) {
    console.error("Error deleting scenario:", error);
    alert(`Error deleting scenario: ${error.message}`);
  }
}

// ========== ATTACK OPTION CRUD ==========

function addAttackOption(scenarioId) {
  document.getElementById("optionModalTitle").textContent = "Add Attack Option";
  document.getElementById("optionId").value = "";
  document.getElementById("optionScenarioId").value = scenarioId;
  document.getElementById("attackOptionForm").reset();
  document.getElementById("optionRiskLevel").value = 1;
  document.getElementById("optionBasePoints").value = 10;

  openModal("attackOptionModal");
}

function editAttackOption(id, scenarioId) {
  const scenario = currentLevel.attackScenarios.find(
    (s) => s.id === scenarioId,
  );
  const option = scenario?.attackOptions?.find((o) => o.id === id);
  if (!option) return;

  document.getElementById("optionModalTitle").textContent =
    "Edit Attack Option";
  document.getElementById("optionId").value = option.id;
  document.getElementById("optionScenarioId").value = scenarioId;
  document.getElementById("optionLabel").value = option.label || "";
  document.getElementById("optionDescription").value = option.description || "";
  document.getElementById("optionAttackerMessage").value =
    option.attackerMessage || "";
  document.getElementById("optionImpersonatedEntity").value =
    option.impersonatedEntity || "";
  document.getElementById("optionBasePoints").value =
    option.baseAttackerPoints || 10;
  document.getElementById("optionRiskLevel").value = option.riskLevel || 1;
  document.getElementById("optionCriticalRisk").checked =
    option.criticalRisk || false;

  openModal("attackOptionModal");
}

async function saveAttackOption(e) {
  e.preventDefault();

  const optionId = document.getElementById("optionId").value;
  const scenarioId = document.getElementById("optionScenarioId").value;

  const optionData = {
    label: document.getElementById("optionLabel").value,
    description: document.getElementById("optionDescription").value,
    attackerMessage: document.getElementById("optionAttackerMessage").value,
    impersonatedEntity: document.getElementById("optionImpersonatedEntity")
      .value,
    baseAttackerPoints:
      parseInt(document.getElementById("optionBasePoints").value) || 10,
    riskLevel: parseInt(document.getElementById("optionRiskLevel").value) || 1,
    criticalRisk: document.getElementById("optionCriticalRisk").checked,
  };

  try {
    if (optionId) {
      await fetch(`${API_BASE}/attack-options/${optionId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(optionData),
      });
    } else {
      await fetch(`${API_BASE}/attack-scenarios/${scenarioId}/options`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(optionData),
      });
    }

    closeModal("attackOptionModal");
    selectLevel(currentLevelId);
  } catch (error) {
    console.error("Error saving option:", error);
    alert("Error saving option");
  }
}

async function deleteAttackOption(id) {
  if (!confirm("Delete this attack option and all its choices?")) return;

  try {
    const response = await fetch(`${API_BASE}/attack-options/${id}`, {
      method: "DELETE",
    });
    if (!response.ok) {
      const errorText = await response.text();
      console.error("Delete failed:", errorText);
      alert(`Failed to delete attack option: ${errorText}`);
      return;
    }
    await selectLevel(currentLevelId);
  } catch (error) {
    console.error("Error deleting option:", error);
    alert(`Error deleting attack option: ${error.message}`);
  }
}

// ========== DEFENDER CHOICE CRUD ==========

function addDefenderChoice(attackOptionId) {
  document.getElementById("choiceModalTitle").textContent =
    "Add Defender Choice";
  document.getElementById("choiceId").value = "";
  document.getElementById("choiceAttackOptionId").value = attackOptionId;
  document.getElementById("defenderChoiceForm").reset();
  document.getElementById("choiceEndsScenario").checked = true;

  populateFollowUpAttackOptions(attackOptionId);
  openModal("defenderChoiceModal");
}

function populateFollowUpAttackOptions(currentAttackOptionId) {
  const select = document.getElementById("choiceFollowUpAttack");
  const allOptions = getAllAttackOptions();

  select.innerHTML = '<option value="">-- None (ends this branch) --</option>';

  allOptions.forEach((opt) => {
    // Don't allow self-reference to prevent infinite loops
    if (opt.id !== currentAttackOptionId) {
      const option = document.createElement("option");
      option.value = opt.id;
      option.textContent = `[${opt.scenarioName}] ${opt.label}`;
      select.appendChild(option);
    }
  });
}

function editDefenderChoice(id, attackOptionId) {
  // Find the choice in the nested structure
  let choice = null;
  for (const scenario of currentLevel.attackScenarios) {
    for (const option of scenario.attackOptions || []) {
      const found = (option.defenderChoices || []).find((c) => c.id === id);
      if (found) {
        choice = found;
        break;
      }
    }
    if (choice) break;
  }

  if (!choice) return;

  document.getElementById("choiceModalTitle").textContent =
    "Edit Defender Choice";
  document.getElementById("choiceId").value = choice.id;
  document.getElementById("choiceAttackOptionId").value = attackOptionId;
  document.getElementById("choiceLabel").value = choice.label || "";
  document.getElementById("choiceDescription").value = choice.description || "";
  document.getElementById("choiceOutcome").value = choice.outcome || "";
  document.getElementById("choiceDefenderScore").value =
    choice.defenderScoreDelta || 0;
  document.getElementById("choiceAttackerScore").value =
    choice.attackerScoreDelta || 0;
  document.getElementById("choiceType").value = choice.choiceType || "NEUTRAL";
  document.getElementById("choiceCriticallyWrong").checked =
    choice.criticallyWrong || false;
  document.getElementById("choiceCriticallyRight").checked =
    choice.criticallyRight || false;
  document.getElementById("choiceEndsScenario").checked =
    choice.endsScenario !== false;
  document.getElementById("choiceEducationalNote").value =
    choice.educationalNote || "";

  populateFollowUpAttackOptions(attackOptionId);
  document.getElementById("choiceFollowUpAttack").value =
    choice.followUpAttackOptionId || "";

  openModal("defenderChoiceModal");
}

async function saveDefenderChoice(e) {
  e.preventDefault();

  const choiceId = document.getElementById("choiceId").value;
  const attackOptionId = document.getElementById("choiceAttackOptionId").value;

  const followUpValue = document.getElementById("choiceFollowUpAttack").value;

  const choiceData = {
    label: document.getElementById("choiceLabel").value,
    description: document.getElementById("choiceDescription").value,
    outcome: document.getElementById("choiceOutcome").value,
    defenderScoreDelta:
      parseInt(document.getElementById("choiceDefenderScore").value) || 0,
    attackerScoreDelta:
      parseInt(document.getElementById("choiceAttackerScore").value) || 0,
    choiceType: document.getElementById("choiceType").value,
    criticallyWrong: document.getElementById("choiceCriticallyWrong").checked,
    criticallyRight: document.getElementById("choiceCriticallyRight").checked,
    endsScenario: document.getElementById("choiceEndsScenario").checked,
    educationalNote: document.getElementById("choiceEducationalNote").value,
    followUpAttackOptionId: followUpValue ? parseInt(followUpValue) : null,
  };

  try {
    if (choiceId) {
      await fetch(`${API_BASE}/defender-choices/${choiceId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(choiceData),
      });
    } else {
      await fetch(`${API_BASE}/attack-options/${attackOptionId}/choices`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(choiceData),
      });
    }

    closeModal("defenderChoiceModal");
    selectLevel(currentLevelId);
  } catch (error) {
    console.error("Error saving choice:", error);
    alert("Error saving choice");
  }
}

async function deleteDefenderChoice(id) {
  if (!confirm("Delete this defender choice?")) return;

  try {
    const response = await fetch(`${API_BASE}/defender-choices/${id}`, {
      method: "DELETE",
    });
    if (!response.ok) {
      const errorText = await response.text();
      console.error("Delete failed:", errorText);
      alert(`Failed to delete defender choice: ${errorText}`);
      return;
    }
    await selectLevel(currentLevelId);
  } catch (error) {
    console.error("Error deleting choice:", error);
    alert(`Error deleting defender choice: ${error.message}`);
  }
}

// ========== MODAL HELPERS ==========

function openModal(modalId) {
  document.getElementById(modalId).classList.remove("hidden");
}

function closeModal(modalId) {
  document.getElementById(modalId).classList.add("hidden");
}

// Close modal on outside click
document.addEventListener("click", (e) => {
  if (e.target.classList.contains("modal")) {
    e.target.classList.add("hidden");
  }
});

// Close modal on Escape key
document.addEventListener("keydown", (e) => {
  if (e.key === "Escape") {
    document
      .querySelectorAll(".modal")
      .forEach((modal) => modal.classList.add("hidden"));
  }
});
