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
  lastRenderedPhase: null, // Track last rendered phase to avoid re-rendering
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

function updateScoreDeltas(state) {
  const atkScoreDelta = document.getElementById("atkScoreDelta");
  const defScoreDelta = document.getElementById("defScoreDelta");

  if (atkScoreDelta && state.lastAttackerScoreDelta !== undefined) {
    const atkDelta = state.lastAttackerScoreDelta;
    atkScoreDelta.textContent = atkDelta >= 0 ? `+${atkDelta}` : `${atkDelta}`;
    atkScoreDelta.parentElement.className = `score-delta attacker ${atkDelta >= 0 ? "positive" : "negative"}`;
  }

  if (defScoreDelta && state.lastDefenderScoreDelta !== undefined) {
    const defDelta = state.lastDefenderScoreDelta;
    defScoreDelta.textContent = defDelta >= 0 ? `+${defDelta}` : `${defDelta}`;
    defScoreDelta.parentElement.className = `score-delta defender ${defDelta >= 0 ? "positive" : "negative"}`;
  }
}

function updateDefenderScoreDeltas(state) {
  const defDefenderScoreDelta = document.getElementById(
    "defDefenderScoreDelta",
  );
  const defAttackerScoreDelta = document.getElementById(
    "defAttackerScoreDelta",
  );

  if (defDefenderScoreDelta && state.lastDefenderScoreDelta !== undefined) {
    const defDelta = state.lastDefenderScoreDelta;
    defDefenderScoreDelta.textContent =
      defDelta >= 0 ? `+${defDelta}` : `${defDelta}`;
    defDefenderScoreDelta.parentElement.className = `score-delta defender ${defDelta >= 0 ? "positive" : "negative"}`;
  }

  if (defAttackerScoreDelta && state.lastAttackerScoreDelta !== undefined) {
    const atkDelta = state.lastAttackerScoreDelta;
    defAttackerScoreDelta.textContent =
      atkDelta >= 0 ? `+${atkDelta}` : `${atkDelta}`;
    defAttackerScoreDelta.parentElement.className = `score-delta attacker ${atkDelta >= 0 ? "positive" : "negative"}`;
  }
}

function updateRoundInfo(state) {
  // Update attack counter for both attacker and defender
  const attackElements = ["atkAttacksPerformed", "defAttacksPerformed"];
  attackElements.forEach((id) => {
    const el = document.getElementById(id);
    if (el) el.textContent = state.attacksPerformed || 0;
  });

  const maxAttackElements = ["atkMaxAttacks", "defMaxAttacks"];
  maxAttackElements.forEach((id) => {
    const el = document.getElementById(id);
    if (el) el.textContent = state.maxAttacks || 5;
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
      // Only show level selection if defender has joined
      if (state.defenderJoined) {
        document.getElementById("atkLevelSelect").classList.remove("hidden");
        // Trigger staggered entrance animation only once
        if (gameState.lastRenderedPhase !== "LEVEL_SELECT") {
          initLevelSlider();
          gameState.lastRenderedPhase = "LEVEL_SELECT";
        }
      } else {
        // Show waiting message if no defender yet
        document.getElementById("atkWaiting").classList.remove("hidden");
        const waitingTitle = document.getElementById("attackLaunchedTitle");
        const waitingMsg = document.getElementById("attackLaunchedMsg");
        if (waitingTitle) {
          waitingTitle.textContent = "Waiting for Opponent...";
        }
        if (waitingMsg) {
          waitingMsg.textContent = "Waiting for defender to join the game...";
        }
        const attackPreviewSection = document.getElementById("attackPreview");
        if (attackPreviewSection) {
          attackPreviewSection.style.display = "none"; // Hide entire attack preview section
        }
      }
      break;
    case "PROFILE_SELECT":
      // Profile is auto-selected, show profile info
      if (state.defenderProfileName) {
        document.getElementById("atkProfileInfo").classList.remove("hidden");
        displayTargetProfile(state);
      }
      break;
    case "ATTACK_TYPE_SELECT":
      document.getElementById("atkTypeSelect").classList.remove("hidden");
      if (gameState.lastRenderedPhase !== "ATTACK_TYPE_SELECT") {
        renderAttackTypeCards(); // Render the attack type cards
        gameState.lastRenderedPhase = "ATTACK_TYPE_SELECT";
      }
      // Display target profile info if available
      if (state.defenderProfileName) {
        document.getElementById("atkProfileInfo").classList.remove("hidden");
        displayTargetProfile(state);
      }
      break;
    case "ATTACK_OPTION_SELECT":
      document.getElementById("atkOptionSelect").classList.remove("hidden");
      if (gameState.lastRenderedPhase !== "ATTACK_OPTION_SELECT") {
        renderAttackOptionCards(); // Render the attack option cards
        gameState.lastRenderedPhase = "ATTACK_OPTION_SELECT";
      }
      break;
    case "DEFENDER_RESPONSE":
      document.getElementById("atkWaiting").classList.remove("hidden");
      const attackTitle = document.getElementById("attackLaunchedTitle");
      const attackPreviewSection = document.getElementById("attackPreview");
      const attackPreview = document.getElementById("attackPreviewContent");
      const attackMsg = document.getElementById("attackLaunchedMsg");
      if (attackTitle) {
        attackTitle.textContent = "Attack Launched!";
      }
      if (attackPreviewSection) {
        attackPreviewSection.style.display = ""; // Show entire attack preview section
      }
      if (attackPreview) {
        attackPreview.textContent =
          state.lastActionMessage || "Attack in progress...";
      }
      if (attackMsg) {
        attackMsg.textContent = "Waiting for defender to respond...";
      }
      break;
    case "OUTCOME_DISPLAY":
      document.getElementById("atkOutcome").classList.remove("hidden");
      document.getElementById("outcomeMessage").textContent =
        state.lastOutcome || "Round completed";
      // Update score deltas
      updateScoreDeltas(state);
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
  // Don't reset lastRenderedPhase here - it should persist across phase visibility changes
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

  // Reset any leftover animation classes from previous play
  container.classList.remove("grid-exit");

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
      (level, index) => `
        <div class="selection-card card-stagger-in"
             style="animation-delay: ${index * 0.12}s"
             tabindex="0"
             role="button"
             aria-label="Select ${level.name}"
             data-level-id="${level.id}">
            <div class="card-inner">
                <h3>🏢 ${level.name}</h3>
                <p>${level.description || "No description"}</p>
                <div class="card-meta">
                    <span class="badge">${level.difficulty}</span>
                </div>
            </div>
        </div>
    `,
    )
    .join("");

  // Attach click & keyboard handlers with exit animation
  container.querySelectorAll(".selection-card[data-level-id]").forEach((card) => {
    const levelId = parseInt(card.dataset.levelId, 10);

    const handleActivate = () => {
      // Prevent double-click
      if (container.classList.contains("grid-exit")) return;
      // Play grid exit animation then proceed
      container.classList.add("grid-exit");
      const duration = getComputedStyle(document.documentElement)
        .getPropertyValue("--card-exit-duration") || "350ms";
      setTimeout(() => {
        selectLevel(levelId);
      }, parseInt(duration));
    };

    card.addEventListener("click", handleActivate);
    card.addEventListener("keydown", (e) => {
      if (e.key === "Enter" || e.key === " ") {
        e.preventDefault();
        handleActivate();
      }
    });
  });
}

async function selectLevel(levelId) {
  try {
    gameState.currentLevelId = levelId;

    // Load profiles for this level
    const profilesResponse = await fetch(
      `${GAME_API}/levels/${levelId}/profiles`,
    );
    gameState.profiles = await profilesResponse.json();
    console.log("Loaded profiles:", gameState.profiles);

    // Load attack scenarios for this level
    const scenariosResponse = await fetch(
      `${GAME_API}/levels/${levelId}/attack-scenarios`,
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
  if (!state.defenderProfileName) return;

  document.getElementById("targetAvatar").textContent =
    state.defenderAvatarIcon || "👤";
  document.getElementById("targetName").textContent = state.defenderProfileName;
  document.getElementById("targetDescription").textContent =
    state.defenderProfileDescription || "";
  document.getElementById("targetAge").textContent =
    state.defenderAge || "Unknown";
  document.getElementById("targetAgeGroup").textContent =
    state.defenderAgeGroup || "Unknown";
  document.getElementById("targetOccupation").textContent =
    state.defenderOccupation || "Unknown";
  document.getElementById("targetTechLevel").textContent =
    state.defenderTechSavviness || "Unknown";
  document.getElementById("targetMentalState").textContent =
    state.defenderMentalState || "Unknown";
  document.getElementById("targetFinancialStatus").textContent =
    state.defenderFinancialStatus || "Unknown";
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
    gameState.scenarios.length,
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
    `,
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
      `${GAME_API}/attack-scenarios/${scenarioId}/options`,
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
    `,
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

    // Reset phase tracking so next phase will render fresh
    gameState.lastRenderedPhase = null;
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
        `${GAME_API}/levels/${gameState.currentLevelId}/attack-scenarios`,
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

  // Show defender profile if available
  if (state.defenderProfileName) {
    const profileDisplay = document.getElementById("defenderProfileDisplay");
    if (profileDisplay) {
      profileDisplay.style.display = "block";
      document.getElementById("defProfileName").textContent =
        state.defenderProfileName || "-";
      document.getElementById("defProfileAge").textContent =
        state.defenderAge || "-";
      document.getElementById("defProfileAgeGroup").textContent =
        state.defenderAgeGroup || "-";
      document.getElementById("defProfileOccupation").textContent =
        state.defenderOccupation || "-";
      document.getElementById("defProfileTechLevel").textContent =
        state.defenderTechSavviness || "-";
      document.getElementById("defProfileMentalState").textContent =
        state.defenderMentalState || "-";
      document.getElementById("defProfileFinancialStatus").textContent =
        state.defenderFinancialStatus || "-";
      document.getElementById("defProfileDescription").textContent =
        state.defenderProfileDescription || "";
    }
  }

  if (state.gamePhase === "DEFENDER_RESPONSE" && !state.isAttackerTurn) {
    document.getElementById("defUnderAttack").classList.remove("hidden");
    displayAttackToDefender(state);
    if (gameState.lastRenderedPhase !== "DEFENDER_RESPONSE") {
      loadDefenderChoices();
      gameState.lastRenderedPhase = "DEFENDER_RESPONSE";
    }
  } else if (state.gamePhase === "OUTCOME_DISPLAY") {
    document.getElementById("defOutcome").classList.remove("hidden");
    document.getElementById("defOutcomeMessage").textContent =
      state.lastOutcome || "Round completed";
    // Update score deltas for defender view
    updateDefenderScoreDeltas(state);
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
          `${GAME_API}/attack-options/${room.currentAttackOptionId}/choices`,
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
        `${GAME_API}/attack-options/${gameState.currentOptionId}/choices`,
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
    (c) => c.treeDepth && c.treeDepth > 0,
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
            <h4>${choice.label}</h4>
            <p>${choice.description || ""}</p>
        </div>
    `,
      )
      .join("");
}

function getChoiceIcon(type) {
  // This function is no longer used - kept for backward compatibility
  return "";
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
    // Find the choice to show educational note
    const choice = gameState.choices.find((c) => c.id === choiceId);

    await fetch(`${GAME_API}/${gameState.roomId}/defender-choice`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ choiceId: choiceId }),
    });

    // Reset phase tracking so next phase will render fresh
    gameState.lastRenderedPhase = null;

    // Show educational note if available
    if (choice && choice.educationalNote) {
      const noteEl = document.getElementById("educationalNote");
      noteEl.innerHTML = `
                <h4>💡 Learning Point</h4>
                <p>${choice.educationalNote}</p>
            `;
    }

    // Reset for next round - attacker chooses next attack
    gameState.currentOptionId = null;
    gameState.choices = [];
  } catch (error) {
    console.error("Error making choice:", error);
  }
}

function showFollowUpTransition(choice) {
  // This function is no longer used - kept for backward compatibility
}

async function defenderContinue() {
  showGamePopup("Kindly wait for attacker to launch attack");
}

function showGamePopup(message) {
  // Remove existing popup if any
  const existing = document.getElementById("gamePopupOverlay");
  if (existing) existing.remove();

  const overlay = document.createElement("div");
  overlay.id = "gamePopupOverlay";
  overlay.style.cssText = `
    position: fixed; top: 0; left: 0; width: 100%; height: 100%;
    background: rgba(0, 0, 0, 0.7); display: flex; align-items: center;
    justify-content: center; z-index: 9999; backdrop-filter: blur(4px);
  `;

  const popup = document.createElement("div");
  popup.style.cssText = `
    background: linear-gradient(135deg, #12121f, #1a1a2e);
    border: 1px solid #00ffff44; border-radius: 12px; padding: 2rem 2.5rem;
    text-align: center; max-width: 420px; width: 90%;
    box-shadow: 0 0 30px rgba(0, 255, 255, 0.15), 0 0 60px rgba(0, 255, 255, 0.05);
    animation: popupFadeIn 0.3s ease;
  `;

  popup.innerHTML = `
    <div style="font-size: 2rem; margin-bottom: 0.8rem;">⏳</div>
    <p style="color: #e8e8ff; font-size: 1.1rem; margin: 0 0 1.5rem 0; line-height: 1.5;
       font-family: 'Courier New', monospace;">${message}</p>
    <button onclick="document.getElementById('gamePopupOverlay').remove()" style="
      background: linear-gradient(135deg, #00ffff, #00d4ff); color: #0a0a14;
      border: none; padding: 0.6rem 2rem; border-radius: 6px; font-size: 1rem;
      font-weight: bold; cursor: pointer; text-transform: uppercase;
      letter-spacing: 1px; transition: all 0.2s;
    " onmouseover="this.style.boxShadow='0 0 20px rgba(0,255,255,0.5)'"
       onmouseout="this.style.boxShadow='none'">OK</button>
  `;

  overlay.appendChild(popup);
  document.body.appendChild(overlay);

  // Close on overlay click (outside popup)
  overlay.addEventListener("click", (e) => {
    if (e.target === overlay) overlay.remove();
  });
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
    lastRenderedPhase: null,
  };

  document.getElementById("attackerGameArea").classList.add("hidden");
  document.getElementById("defenderGameArea").classList.add("hidden");
  document.getElementById("gameOverScreen").classList.add("hidden");
  document.getElementById("roomSelectionScreen").classList.add("hidden");

  showScreen("lobby-screen");
  if (app) app.style.display = "block";
}

// ========== LEVEL-SELECT HORIZONTAL SLIDER ==========

let sliderIndex = 0;

function initLevelSlider() {
  const container = document.getElementById("levelCards");
  if (!container || container.querySelector(".works-slider")) return; // already wrapped

  const cards = [...container.querySelectorAll(".selection-card")];
  if (cards.length === 0) return;

  // Build slider DOM around existing cards (cards keep all data-attrs & handlers)
  const slider = document.createElement("div");
  slider.className = "works-slider";

  const viewport = document.createElement("div");
  viewport.className = "works-viewport";

  const track = document.createElement("div");
  track.className = "works-track";
  cards.forEach((c) => { c.classList.remove("card-stagger-in"); track.appendChild(c); });
  viewport.appendChild(track);

  // Nav buttons
  const prev = document.createElement("button");
  prev.className = "works-prev"; prev.innerHTML = "&#8249;"; prev.setAttribute("aria-label","Previous");
  const next = document.createElement("button");
  next.className = "works-next"; next.innerHTML = "&#8250;"; next.setAttribute("aria-label","Next");
  slider.appendChild(prev); slider.appendChild(viewport); slider.appendChild(next);

  // Dots
  const dots = document.createElement("div"); dots.className = "works-dots";
  cards.forEach((_, i) => { const d = document.createElement("span"); d.className = "works-dot"; d.addEventListener("click", () => { sliderIndex = i; updateSlider(); }); dots.appendChild(d); });

  container.innerHTML = "";
  container.appendChild(slider);
  slider.appendChild(dots);

  sliderIndex = 0;
  updateSlider();

  // --- Nav buttons ---
  prev.addEventListener("click", () => { if (sliderIndex > 0) { sliderIndex--; updateSlider(); } });
  next.addEventListener("click", () => { if (sliderIndex < cards.length - 1) { sliderIndex++; updateSlider(); } });

  // --- Wheel (vertical → horizontal) ---
  let wheelCooldown = false;
  slider.addEventListener("wheel", (e) => {
    e.preventDefault();
    if (wheelCooldown) return;
    if (Math.abs(e.deltaY) > 20) {
      sliderIndex = Math.max(0, Math.min(cards.length - 1, sliderIndex + (e.deltaY > 0 ? 1 : -1)));
      updateSlider(); wheelCooldown = true; setTimeout(() => wheelCooldown = false, 350);
    }
  }, { passive: false });

  // --- Drag / swipe (pointer events) ---
  let startX = 0, dragging = false, startTx = 0, pointerDown = false, pendingPointerId = null;
  const DRAG_THRESHOLD = 5; // px – movements smaller than this count as a click
  track.addEventListener("pointerdown", (e) => {
    if (e.button && e.button !== 0) return;
    pointerDown = true; dragging = false; startX = e.clientX;
    startTx = parseFloat(track.style.transform.replace(/[^-\d.]/g, "") || 0);
    pendingPointerId = e.pointerId;
  });
  track.addEventListener("pointermove", (e) => {
    if (!pointerDown) return;
    const dx = e.clientX - startX;
    if (!dragging && Math.abs(dx) >= DRAG_THRESHOLD) {
      dragging = true;
      track.classList.add("is-dragging");
      track.setPointerCapture(pendingPointerId);
    }
    if (dragging) {
      track.style.transform = `translateX(${startTx + dx}px)`;
    }
  });
  const endDrag = (e) => {
    if (!pointerDown) return;
    pointerDown = false;
    if (dragging) {
      dragging = false; track.classList.remove("is-dragging");
      const dx = e.clientX - startX;
      if (Math.abs(dx) > 60) sliderIndex = Math.max(0, Math.min(cards.length - 1, sliderIndex + (dx < 0 ? 1 : -1)));
      updateSlider();
    }
    // If not dragging, the click event fires normally on the card
  };
  track.addEventListener("pointerup", endDrag);
  track.addEventListener("pointercancel", endDrag);

  // --- Keyboard ---
  slider.setAttribute("tabindex", "0");
  slider.addEventListener("keydown", (e) => {
    if (e.key === "ArrowLeft") { sliderIndex = Math.max(0, sliderIndex - 1); updateSlider(); e.preventDefault(); }
    if (e.key === "ArrowRight") { sliderIndex = Math.min(cards.length - 1, sliderIndex + 1); updateSlider(); e.preventDefault(); }
  });
}

function updateSlider() {
  const slider = document.querySelector("#levelCards .works-slider");
  if (!slider) return;
  const viewport = slider.querySelector(".works-viewport");
  const track = slider.querySelector(".works-track");
  const cards = [...track.children];
  if (!cards.length) return;

  const vpStyle = getComputedStyle(viewport);
  const padL = parseFloat(vpStyle.paddingLeft) || 0;
  const padR = parseFloat(vpStyle.paddingRight) || 0;
  const contentW = viewport.clientWidth - padL - padR;
  const cardW = cards[0].offsetWidth;
  const gap = parseFloat(getComputedStyle(track).gap) || 28;
  const step = cardW + gap;

  // Center active card within the content area of the viewport
  let offset = (contentW / 2) - (cardW / 2) - sliderIndex * step;

  // Clamp so adjacent cards peek symmetrically at both edges
  if (cards.length > 1) {
    const maxOff = (contentW / 2) - (cardW / 2);
    const minOff = (contentW / 2) - (cardW / 2) - (cards.length - 1) * step;
    if (maxOff > minOff) {
      offset = Math.max(minOff, Math.min(maxOff, offset));
    }
  }

  track.style.transform = `translateX(${offset}px)`;
  cards.forEach((c, i) => c.classList.toggle("works-active", i === sliderIndex));
  // Dots
  const dots = document.querySelectorAll("#levelCards .works-dot");
  dots.forEach((d, i) => d.classList.toggle("active", i === sliderIndex));
  // Buttons
  const prev = slider.querySelector(".works-prev");
  const next = slider.querySelector(".works-next");
  if (prev) prev.disabled = sliderIndex === 0;
  if (next) next.disabled = sliderIndex === cards.length - 1;
}

// Export for use in app.js
window.initGame = initGame;
window.exitToLobby = exitToLobby;
