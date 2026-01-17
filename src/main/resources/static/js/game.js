// New Game Logic for CyberWalk
const GAME_API = "/api/game";
const LEVELS_API = "/api/levels";

// Game State
let gameState = {
  roomId: null,
  role: null, // 'ATTACKER' or 'DEFENDER'
  currentLevelId: null,
  currentProfileId: null,
  currentScenarioId: null,
  currentOptionId: null,
  pollInterval: null,
  levels: [],
  profiles: [],
  scenarios: [],
  options: [],
  choices: [],
};

// ========== INITIALIZATION ==========

function initGame(roomId, role) {
  gameState.roomId = roomId;
  gameState.role = role;

  if (role === "ATTACKER") {
    showAttackerGame();
    loadLevels();
  } else {
    showDefenderGame();
  }

  // Start polling for game state updates
  startPolling();
}

function startPolling() {
  if (gameState.pollInterval) {
    clearInterval(gameState.pollInterval);
  }
  gameState.pollInterval = setInterval(pollGameState, 1500);
  pollGameState(); // Initial poll
}

function stopPolling() {
  if (gameState.pollInterval) {
    clearInterval(gameState.pollInterval);
    gameState.pollInterval = null;
  }
}

// ========== GAME STATE POLLING ==========

async function pollGameState() {
  if (!gameState.roomId) return;

  try {
    const response = await fetch(`${GAME_API}/${gameState.roomId}/state`);
    if (!response.ok) return;

    const state = await response.json();
    updateGameUI(state);
  } catch (error) {
    console.error("Poll error:", error);
  }
}

function updateGameUI(state) {
  // Update scores
  updateScores(state);

  // Update round info
  updateRoundInfo(state);

  // Handle phase-specific UI based on role
  if (gameState.role === "ATTACKER") {
    updateAttackerUI(state);
  } else {
    updateDefenderUI(state);
  }

  // Check for game over
  if (state.gamePhase === "GAME_OVER" || state.status === "ROUND_OVER") {
    showGameOver(state);
  }
}

function updateScores(state) {
  // Attacker view
  const atkScore = document.getElementById("atkScore");
  const atkDefScore = document.getElementById("atkDefScore");
  if (atkScore) atkScore.textContent = state.attackerScore;
  if (atkDefScore) atkDefScore.textContent = state.defenderScore;

  // Defender view
  const defScore = document.getElementById("defScore");
  const defAtkScore = document.getElementById("defAtkScore");
  if (defScore) defScore.textContent = state.defenderScore;
  if (defAtkScore) defAtkScore.textContent = state.attackerScore;
}

function updateRoundInfo(state) {
  const elements = ["atkRoundNum", "defRoundNum"];
  elements.forEach((id) => {
    const el = document.getElementById(id);
    if (el) el.textContent = state.currentRound;
  });

  const maxElements = ["atkMaxRounds", "defMaxRounds"];
  maxElements.forEach((id) => {
    const el = document.getElementById(id);
    if (el) el.textContent = state.maxRounds;
  });
}

// ========== ATTACKER UI ==========

function showAttackerGame() {
  document.getElementById("attackerGameArea").classList.remove("hidden");
  document.getElementById("defenderGameArea").classList.add("hidden");
  document.getElementById("atkRoomId").textContent = gameState.roomId;
}

function updateAttackerUI(state) {
  hideAllAttackerPhases();

  switch (state.gamePhase) {
    case "LEVEL_SELECT":
      document.getElementById("atkLevelSelect").classList.remove("hidden");
      break;
    case "PROFILE_SELECT":
      document.getElementById("atkProfileInfo").classList.remove("hidden");
      displayTargetProfile(state);
      break;
    case "ATTACK_TYPE_SELECT":
      document.getElementById("atkTypeSelect").classList.remove("hidden");
      renderAttackTypeCards(); // Render the attack type cards
      break;
    case "ATTACK_OPTION_SELECT":
      document.getElementById("atkOptionSelect").classList.remove("hidden");
      renderAttackOptionCards(); // Render the attack option cards
      break;
    case "DEFENDER_RESPONSE":
      document.getElementById("atkWaiting").classList.remove("hidden");
      document.getElementById("attackPreviewContent").textContent =
        state.lastActionMessage || "Attack in progress...";
      break;
    case "OUTCOME_DISPLAY":
      document.getElementById("atkOutcome").classList.remove("hidden");
      document.getElementById("outcomeMessage").textContent =
        state.lastOutcome || "Round completed";
      break;
  }
}

function hideAllAttackerPhases() {
  const phases = [
    "atkLevelSelect",
    "atkProfileInfo",
    "atkTypeSelect",
    "atkOptionSelect",
    "atkWaiting",
    "atkOutcome",
  ];
  phases.forEach((id) => {
    const el = document.getElementById(id);
    if (el) el.classList.add("hidden");
  });
}

async function loadLevels() {
  try {
    const response = await fetch(`${GAME_API}/levels`);
    gameState.levels = await response.json();
    renderLevelCards();
  } catch (error) {
    console.error("Error loading levels:", error);
  }
}

function renderLevelCards() {
  const container = document.getElementById("levelCards");

  if (gameState.levels.length === 0) {
    container.innerHTML = `
            <div class="empty-state" style="grid-column: 1 / -1; text-align: center; padding: 3rem;">
                <h3 style="color: var(--text-secondary);">No Levels Available</h3>
                <p style="color: var(--text-secondary);">Go to Level Editor to create scenarios</p>
                <a href="editor.html" class="btn btn-primary" style="margin-top: 1rem;">Open Level Editor</a>
            </div>
        `;
    return;
  }

  container.innerHTML = gameState.levels
    .map(
      (level) => `
        <div class="selection-card" onclick="selectLevel(${level.id})">
            <h3>🏢 ${level.name}</h3>
            <p>${level.description || "No description"}</p>
            <div class="card-meta">
                <span class="badge">${level.difficulty}</span>
            </div>
        </div>
    `
    )
    .join("");
}

async function selectLevel(levelId) {
  try {
    gameState.currentLevelId = levelId;

    // Load profiles for this level
    const profilesResponse = await fetch(
      `${GAME_API}/levels/${levelId}/profiles`
    );
    gameState.profiles = await profilesResponse.json();
    console.log("Loaded profiles:", gameState.profiles);

    // Load attack scenarios for this level
    const scenariosResponse = await fetch(
      `${GAME_API}/levels/${levelId}/attack-scenarios`
    );
    gameState.scenarios = await scenariosResponse.json();
    console.log("Loaded attack scenarios:", gameState.scenarios);

    // Notify server
    await fetch(`${GAME_API}/${gameState.roomId}/select-level`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ levelId: levelId }),
    });

    // If there's at least one profile, auto-select it and show info
    if (gameState.profiles.length > 0) {
      gameState.currentProfileId = gameState.profiles[0].id;
      await fetch(`${GAME_API}/${gameState.roomId}/select-profile`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ profileId: gameState.currentProfileId }),
      });
    }
  } catch (error) {
    console.error("Error selecting level:", error);
  }
}

function displayTargetProfile(state) {
  const profile =
    gameState.profiles.find((p) => p.id === gameState.currentProfileId) ||
    gameState.profiles[0];
  if (!profile) return;

  document.getElementById("targetAvatar").textContent =
    profile.avatarIcon || "👤";
  document.getElementById("targetName").textContent = profile.name;
  document.getElementById("targetDescription").textContent =
    profile.description || "";
  document.getElementById("targetAge").textContent = profile.age;
  document.getElementById("targetOccupation").textContent =
    profile.occupation || "Unknown";
  document.getElementById("targetTechLevel").textContent =
    profile.techSavviness || "Unknown";
  document.getElementById("targetMentalState").textContent =
    profile.mentalState || "Unknown";

  // Relationships
  const relContainer = document.getElementById("targetRelationships");
  relContainer.innerHTML = (profile.relationships || [])
    .map((r) => `<span class="tag">${r}</span>`)
    .join("");

  // Vulnerabilities
  const vulnContainer = document.getElementById("targetVulnerabilities");
  vulnContainer.innerHTML = (profile.vulnerabilities || [])
    .map((v) => `<span class="tag">${v}</span>`)
    .join("");
}

async function proceedToAttackTypes() {
  console.log("Proceeding to attack types. Scenarios:", gameState.scenarios);
  renderAttackTypeCards();
  // Manually transition (polling will catch up)
  hideAllAttackerPhases();
  document.getElementById("atkTypeSelect").classList.remove("hidden");
}

function renderAttackTypeCards() {
  const container = document.getElementById("attackTypeCards");
  console.log(
    "Rendering attack type cards. Scenarios count:",
    gameState.scenarios.length
  );
  console.log("Container element:", container);

  if (!container) {
    console.error("attackTypeCards container not found!");
    return;
  }

  if (gameState.scenarios.length === 0) {
    container.innerHTML = `
            <div class="empty-state" style="grid-column: 1 / -1; text-align: center; padding: 2rem;">
                <p style="color: var(--text-secondary);">No attack scenarios defined for this level</p>
                <p style="color: var(--text-secondary); font-size: 0.9rem; margin-top: 1rem;">Go to Level Editor and add attack scenarios to this level</p>
            </div>
        `;
    return;
  }

  const cardsHTML = gameState.scenarios
    .map(
      (scenario) => `
        <div class="selection-card attack-card" onclick="selectAttackScenario(${
          scenario.id
        })">
            <h3>🎯 ${scenario.name}</h3>
            <p>${scenario.description || "No description"}</p>
            <div class="card-meta">
                <span class="badge" style="background: rgba(255,100,100,0.2); color: #ff6464;">${
                  scenario.attackType
                }</span>
            </div>
        </div>
    `
    )
    .join("");

  console.log("Generated HTML:", cardsHTML);
  container.innerHTML = cardsHTML;
  console.log("Container after setting innerHTML:", container.innerHTML);
}

async function selectAttackScenario(scenarioId) {
  try {
    gameState.currentScenarioId = scenarioId;

    // Load attack options
    const response = await fetch(
      `${GAME_API}/attack-scenarios/${scenarioId}/options`
    );
    gameState.options = await response.json();

    // Notify server
    await fetch(`${GAME_API}/${gameState.roomId}/select-attack-scenario`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ scenarioId: scenarioId }),
    });

    renderAttackOptionCards();
  } catch (error) {
    console.error("Error selecting scenario:", error);
  }
}

function renderAttackOptionCards() {
  const container = document.getElementById("attackOptionCards");

  if (gameState.options.length === 0) {
    container.innerHTML = `
            <div class="empty-state" style="grid-column: 1 / -1; text-align: center; padding: 2rem;">
                <p style="color: var(--text-secondary);">No attack options defined</p>
            </div>
        `;
    return;
  }

  container.innerHTML = gameState.options
    .map(
      (option) => `
        <div class="option-card" onclick="selectAttackOption(${option.id})">
            <h4>${option.isFollowUp ? "🔗" : "🎭"} ${option.label}</h4>
            ${
              option.isFollowUp
                ? '<span class="badge" style="background: rgba(100,200,255,0.2); color: #64c8ff; margin-bottom: 0.5rem; display: inline-block;">Follow-up Option</span>'
                : ""
            }
            <p>${option.description || ""}</p>
            ${
              option.impersonatedEntity
                ? `<p><strong>Impersonating:</strong> ${option.impersonatedEntity}</p>`
                : ""
            }
            <div class="message-preview">"${
              option.attackerMessage || "No message set"
            }"</div>
            <div class="risk-indicator">
                <span>Risk Level:</span>
                <div class="risk-dots">
                    ${renderRiskDots(option.riskLevel)}
                </div>
                ${
                  option.criticalRisk
                    ? '<span style="color: #ff6464; font-weight: bold;">⚠ CRITICAL</span>'
                    : ""
                }
            </div>
            <div style="margin-top: 0.5rem; padding-top: 0.5rem; border-top: 1px solid rgba(255,255,255,0.1);">
                <strong>Points if successful:</strong> ${
                  option.baseAttackerPoints
                }
            </div>
        </div>
    `
    )
    .join("");

  hideAllAttackerPhases();
  document.getElementById("atkOptionSelect").classList.remove("hidden");
}

function renderRiskDots(level) {
  let dots = "";
  for (let i = 1; i <= 5; i++) {
    dots += `<span class="dot ${i <= level ? "active" : ""}"></span>`;
  }
  return dots;
}

async function selectAttackOption(optionId) {
  try {
    gameState.currentOptionId = optionId;

    await fetch(`${GAME_API}/${gameState.roomId}/select-attack-option`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ optionId: optionId }),
    });

    // Polling will update UI to waiting state
  } catch (error) {
    console.error("Error selecting option:", error);
  }
}

async function nextRound() {
  try {
    await fetch(`${GAME_API}/${gameState.roomId}/next-round`, {
      method: "POST",
    });

    // Reload scenarios for next round
    if (gameState.currentLevelId) {
      const scenariosResponse = await fetch(
        `${GAME_API}/levels/${gameState.currentLevelId}/attack-scenarios`
      );
      gameState.scenarios = await scenariosResponse.json();
    }
  } catch (error) {
    console.error("Error continuing:", error);
  }
}

// ========== DEFENDER UI ==========

function showDefenderGame() {
  document.getElementById("defenderGameArea").classList.remove("hidden");
  document.getElementById("attackerGameArea").classList.add("hidden");
  document.getElementById("defRoomId").textContent = gameState.roomId;
}

function updateDefenderUI(state) {
  hideAllDefenderPhases();

  if (state.gamePhase === "DEFENDER_RESPONSE" && !state.isAttackerTurn) {
    document.getElementById("defUnderAttack").classList.remove("hidden");
    displayAttackToDefender(state);
    loadDefenderChoices();
  } else if (state.gamePhase === "OUTCOME_DISPLAY") {
    document.getElementById("defOutcome").classList.remove("hidden");
    document.getElementById("defOutcomeMessage").textContent =
      state.lastOutcome || "Round completed";
  } else {
    document.getElementById("defWaiting").classList.remove("hidden");
  }
}

function hideAllDefenderPhases() {
  const phases = ["defWaiting", "defUnderAttack", "defOutcome"];
  phases.forEach((id) => {
    const el = document.getElementById(id);
    if (el) el.classList.add("hidden");
  });
}

function displayAttackToDefender(state) {
  document.getElementById("attackSender").textContent =
    state.impersonatedEntity || "Unknown Caller";
  document.getElementById("attackMessage").textContent =
    state.currentAttackMessage ||
    "You have received a suspicious communication...";
}

async function loadDefenderChoices() {
  if (!gameState.currentOptionId) {
    // Get current option ID from room state
    try {
      const response = await fetch(`/api/room/${gameState.roomId}/status`);
      const room = await response.json();

      if (room.currentAttackOptionId) {
        gameState.currentOptionId = room.currentAttackOptionId;

        const choicesResponse = await fetch(
          `${GAME_API}/attack-options/${room.currentAttackOptionId}/choices`
        );
        gameState.choices = await choicesResponse.json();
        renderDefenderChoices();
      } else {
        // No attack option selected yet, show waiting message
        const container = document.getElementById("defenderChoices");
        container.innerHTML = `
          <div class="empty-state" style="text-align: center; padding: 2rem;">
            <h3 style="color: var(--text-secondary);">⏳ Waiting for attacker...</h3>
            <p style="color: var(--text-secondary);">The attacker is preparing their move</p>
          </div>
        `;
      }
    } catch (error) {
      console.error("Error loading choices:", error);
      const container = document.getElementById("defenderChoices");
      container.innerHTML = `
        <div class="choice-card" onclick="makeDefaultChoice()">
          <h4>🤔 I'm not sure...</h4>
          <p>Proceed with caution</p>
        </div>
      `;
    }
  } else {
    try {
      const response = await fetch(
        `${GAME_API}/attack-options/${gameState.currentOptionId}/choices`
      );
      gameState.choices = await response.json();
      renderDefenderChoices();
    } catch (error) {
      console.error("Error loading choices:", error);
      const container = document.getElementById("defenderChoices");
      container.innerHTML = `
        <div class="choice-card" onclick="makeDefaultChoice()">
          <h4>🤔 I'm not sure...</h4>
          <p>Proceed with caution</p>
        </div>
      `;
    }
  }
}

function renderDefenderChoices() {
  const container = document.getElementById("defenderChoices");

  if (gameState.choices.length === 0) {
    container.innerHTML = `
            <div class="choice-card" onclick="makeDefaultChoice()">
                <h4>🤔 I'm not sure...</h4>
                <p>Proceed with caution</p>
            </div>
        `;
    return;
  }

  // Check if we're in a follow-up situation (tree depth > 0)
  const hasTreeDepth = gameState.choices.some(
    (c) => c.treeDepth && c.treeDepth > 0
  );
  const treeDepth = hasTreeDepth ? gameState.choices[0].treeDepth : 0;

  let header = "";
  if (treeDepth > 0) {
    header = `
      <div style="background: rgba(100, 200, 255, 0.1); padding: 1rem; border-radius: 8px; margin-bottom: 1rem; border-left: 4px solid var(--accent-blue);">
        <h4 style="margin: 0; color: var(--accent-blue);">🔗 Decision Tree - Level ${treeDepth}</h4>
        <p style="margin: 0.5rem 0 0 0; color: var(--text-secondary); font-size: 0.9rem;">
          This is a follow-up scenario. Your previous choice has led to this new situation.
        </p>
      </div>
    `;
  }

  container.innerHTML =
    header +
    gameState.choices
      .map(
        (choice) => `
        <div class="choice-card" onclick="makeDefenderChoice(${choice.id})">
            <h4>${getChoiceIcon(choice.choiceType)} ${choice.label}</h4>
            <p>${choice.description || ""}</p>
            ${
              choice.followUpAttackOptionId
                ? '<span class="badge" style="background: rgba(100,200,255,0.2); color: #64c8ff; margin-top: 0.5rem; display: inline-block;">🔗 Continues scenario</span>'
                : ""
            }
        </div>
    `
      )
      .join("");
}

function getChoiceIcon(type) {
  switch (type) {
    case "CORRECT":
      return "✅";
    case "WRONG":
      return "❌";
    case "RISKY":
      return "⚠️";
    default:
      return "🔵";
  }
}

async function makeDefaultChoice() {
  // Handle the "I'm not sure" default choice when no choices are available
  try {
    await fetch(`${GAME_API}/${gameState.roomId}/defender-choice`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ choiceId: null }),
    });

    // Show a neutral outcome
    const noteEl = document.getElementById("educationalNote");
    noteEl.innerHTML = `
      <h4>💡 Learning Point</h4>
      <p>When unsure, it's better to seek help or verify before taking action. Always consult with IT or security professionals when faced with suspicious situations.</p>
    `;

    // Reset for next round
    gameState.currentOptionId = null;
    gameState.choices = [];
  } catch (error) {
    console.error("Error making default choice:", error);
  }
}

async function makeDefenderChoice(choiceId) {
  try {
    // Find the choice to check for follow-up
    const choice = gameState.choices.find((c) => c.id === choiceId);

    await fetch(`${GAME_API}/${gameState.roomId}/defender-choice`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ choiceId: choiceId }),
    });

    // Show educational note if available
    if (choice && choice.educationalNote) {
      const noteEl = document.getElementById("educationalNote");
      noteEl.innerHTML = `
                <h4>💡 Learning Point</h4>
                <p>${choice.educationalNote}</p>
            `;
    }

    // Check if this choice has a follow-up attack option (decision tree continues)
    if (choice && choice.followUpAttackOptionId) {
      // Load the follow-up attack option and its choices
      gameState.currentOptionId = choice.followUpAttackOptionId;

      // Show a brief transition message
      showFollowUpTransition(choice);

      // Wait a moment then load new choices
      setTimeout(async () => {
        await loadDefenderChoices();
      }, 2000);
    } else {
      // Reset for next round (scenario ends)
      gameState.currentOptionId = null;
      gameState.choices = [];
    }
  } catch (error) {
    console.error("Error making choice:", error);
  }
}

function showFollowUpTransition(choice) {
  const container = document.getElementById("defenderChoices");
  container.innerHTML = `
    <div class="transition-message" style="text-align: center; padding: 2rem;">
      <div class="spinner"></div>
      <h3 style="margin-top: 1rem; color: var(--accent-orange);">🔄 Situation Evolving...</h3>
      <p style="color: var(--text-secondary);">The attacker is adapting to your response...</p>
    </div>
  `;
}

async function defenderContinue() {
  // Just let polling handle the state transition
  pollGameState();
}

// ========== GAME OVER ==========

function showGameOver(state) {
  stopPolling();

  document.getElementById("attackerGameArea").classList.add("hidden");
  document.getElementById("defenderGameArea").classList.add("hidden");
  document.getElementById("gameOverScreen").classList.remove("hidden");

  // Determine winner
  const defenderWins = state.defenderScore > state.attackerScore;
  const title = document.getElementById("gameOverTitle");
  const subtitle = document.getElementById("gameOverSubtitle");

  if (defenderWins) {
    title.textContent = "🛡️ DEFENSE SUCCESSFUL!";
    title.style.color = "#64c8ff";
    subtitle.textContent =
      "The defender successfully protected against cyber attacks!";
  } else if (state.defenderScore < state.attackerScore) {
    title.textContent = "⚔️ ATTACK SUCCESSFUL!";
    title.style.color = "#ff6464";
    subtitle.textContent = "The attacker successfully compromised the target!";
  } else {
    title.textContent = "⚖️ DRAW";
    title.style.color = "#ffc800";
    subtitle.textContent = "Both sides fought to a standstill!";
  }

  document.getElementById("finalDefenderScore").textContent =
    state.defenderScore;
  document.getElementById("finalAttackerScore").textContent =
    state.attackerScore;

  // Show lessons
  const lessons = document.getElementById("lessonsLearned");
  lessons.innerHTML = `
        <ul style="list-style: none; padding: 0;">
            <li style="margin-bottom: 0.75rem;">🔒 Always verify the identity of callers before sharing sensitive information</li>
            <li style="margin-bottom: 0.75rem;">🔍 Check URLs carefully - attackers often use similar-looking domains</li>
            <li style="margin-bottom: 0.75rem;">📞 When in doubt, hang up and call back using official numbers</li>
            <li style="margin-bottom: 0.75rem;">🔐 Never share passwords, OTPs, or security codes with anyone</li>
            <li>⚠️ Trust your instincts - if something feels off, it probably is</li>
        </ul>
    `;
}

// ========== UTILITY FUNCTIONS ==========

function exitToLobby() {
  stopPolling();
  gameState = {
    roomId: null,
    role: null,
    currentLevelId: null,
    currentProfileId: null,
    currentScenarioId: null,
    currentOptionId: null,
    pollInterval: null,
    levels: [],
    profiles: [],
    scenarios: [],
    options: [],
    choices: [],
  };

  document.getElementById("attackerGameArea").classList.add("hidden");
  document.getElementById("defenderGameArea").classList.add("hidden");
  document.getElementById("gameOverScreen").classList.add("hidden");
  document.getElementById("roomSelectionScreen").classList.add("hidden");

  showScreen("lobby-screen");
  if (app) app.style.display = "block";
}

// Export for use in app.js
window.initGame = initGame;
window.exitToLobby = exitToLobby;
