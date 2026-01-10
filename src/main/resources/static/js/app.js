// API Configuration
const API_BASE_URL = "http://localhost:8080/api";

// State
let myRoomId = null;
let myRole = null;
let pollInterval = null;
let lastKnownState = null;
let selectedScenarioId = null;
let selectedAttacks = [];

// DOM Elements - New Design
const screens = document.querySelectorAll(".screen");
const volumeVal = document.getElementById("volume-val");
const brightnessVal = document.getElementById("brightness-val");
const app = document.getElementById("app");

// DOM Elements - Game
const roomSelectionScreen = document.getElementById("roomSelectionScreen"); // Renamed from lobbyScreen
const attackerDashboard = document.getElementById("attackerDashboard");
const defenderGameArea = document.getElementById("defenderGameArea");
const lobbyStatus = document.getElementById("lobbyStatus");
const videoElement = document.getElementById("mainVideo");
const optionButtons = document.getElementById("optionButtons");
const optionsOverlay = document.getElementById("optionsOverlay");
const scenarioSelectionArea = document.getElementById("scenarioSelectionArea");
const attackSelectionArea = document.getElementById("attackSelectionArea");

// --- Initialization & New Design Logic ---

document.addEventListener("DOMContentLoaded", () => {
  // Simulate loading for the new cinematic intro
  setTimeout(() => {
    showScreen("lobby-screen");
  }, 2500);
});

// Navigation Logic
function showScreen(screenId) {
  // Hide all cinematic screens
  screens.forEach((s) => {
    s.classList.remove("active");
  });

  // Hide game components just in case
  if (screenId === "lobby-screen") {
    hideGameComponents();
    if (app) app.style.display = "block"; // Ensure app is visible when returning to lobby
  }

  const target = document.getElementById(screenId);
  if (target) {
    target.classList.add("active");
  }
}

// Game Controls (New Lobby)
function startGame() {
  // Hide the main menu lobby container completely so it doesn't take up space
  if (app) app.style.display = "none";

  // Show the Room Selection Screen (Old Lobby)
  roomSelectionScreen.classList.remove("hidden");
}

function exitGame() {
  if (confirm("Are you sure you want to exit?")) {
    window.close();
    // In case window.close() is blocked (common in browsers)
    alert("Systems powered down.");
  }
}

// Settings Logic
function updateSetting(type, value) {
  if (type === "volume") {
    if (volumeVal) volumeVal.textContent = `${value}%`;
    // In a real game, you'd set audio gain here
  } else if (type === "brightness") {
    if (brightnessVal) brightnessVal.textContent = `${value}%`;
    // Apply brightness filter to the app container
    const filterVal = value / 100;
    if (app) app.style.filter = `brightness(${filterVal})`;
  }
}

function hideGameComponents() {
  roomSelectionScreen.classList.add("hidden");
  attackerDashboard.classList.add("hidden");
  defenderGameArea.classList.add("hidden");
  document.getElementById("gameOverScreen").classList.add("hidden");
  if (pollInterval) clearInterval(pollInterval);
}

// --- Lobby Functions (Room Selection) ---

async function createRoom() {
  try {
    const response = await fetch(`${API_BASE_URL}/room/create`, {
      method: "POST",
    });
    const room = await response.json();
    myRoomId = room.roomId;
    document.getElementById("roomIdInput").value = myRoomId;
    lobbyStatus.textContent = `Room Created! ID: ${myRoomId}. Join as a role below.`;
  } catch (e) {
    console.error(e);
    lobbyStatus.textContent = "Error creating room.";
  }
}

async function joinRoom() {
  const roomIdInput = document.getElementById("roomIdInput").value.trim();
  const roleSelect = document.getElementById("roleSelect").value;

  if (!roomIdInput) {
    lobbyStatus.textContent = "Please enter a Room ID.";
    return;
  }

  try {
    const response = await fetch(`${API_BASE_URL}/room/join`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ roomId: roomIdInput, role: roleSelect }),
    });

    if (!response.ok) throw new Error("Join failed");

    const room = await response.json();
    myRoomId = room.roomId;
    myRole = roleSelect;

    enterGameMode();
  } catch (e) {
    console.error(e);
    lobbyStatus.textContent = "Failed to join. Room might be full or invalid.";
  }
}

function enterGameMode() {
  roomSelectionScreen.classList.add("hidden");

  if (myRole === "ATTACKER") {
    attackerDashboard.classList.remove("hidden");
    document.getElementById("attackerRoomId").textContent = myRoomId;
  } else {
    defenderGameArea.classList.remove("hidden");
  }

  // Start Polling
  pollInterval = setInterval(pollGameState, 1500);
}

// --- Game Logic ---

async function pollGameState() {
  try {
    const response = await fetch(`${API_BASE_URL}/room/${myRoomId}/status`);
    const room = await response.json();

    updateUI(room);
    lastKnownState = room;
  } catch (e) {
    console.error("Polling error", e);
  }
}

function triggerTransition(callback) {
  const overlay = document.getElementById("transitionOverlay");
  overlay.classList.add("active");
  setTimeout(() => {
    if (callback) callback();
    setTimeout(() => {
      overlay.classList.remove("active");
    }, 500);
  }, 500);
}

function updateUI(room) {
  // Score Updates
  if (myRole === "ATTACKER") {
    document.getElementById("attackerScoreValue").textContent =
      room.attackerScore;
    document.getElementById("defenderScoreValueAtk").textContent =
      room.defenderScore;
    // Always update dashboard for attacker
    updateAttackerDashboard(room);
  } else {
    document.getElementById("defenderScoreValue").textContent =
      room.defenderScore;

    // Defender Specific State Logic
    if (room.status === "ATTACK_SELECTION") {
      // Defender Waiting
      document.getElementById("loadingIndicator").classList.remove("hidden");
      document.querySelector("#loadingIndicator p").textContent =
        "Waiting for Attacker to select a scenario...";

      // Ensure other areas hidden
      document.getElementById("attackSelectionArea").classList.add("hidden");
      document.getElementById("scenarioSelectionArea").classList.add("hidden");
    } else if (room.status === "DEFENDER_TURN") {
      document.getElementById("loadingIndicator").classList.add("hidden");

      // Defender Playing
      if (
        !lastKnownState ||
        lastKnownState.currentVideoId !== room.currentVideoId
      ) {
        if (lastKnownState && lastKnownState.currentVideoId) {
          triggerTransition(() => loadScenarioForDefender(room.currentVideoId));
        } else {
          loadScenarioForDefender(room.currentVideoId);
        }
      }
    }
  }

  if (room.status === "ROUND_OVER") {
    clearInterval(pollInterval);
    showGameOver(room);
  }
}

// --- Attacker Dashboard Logic ---

function updateAttackerDashboard(room) {
  document.getElementById("attackerRoomId").innerText = room.roomId;
  document.getElementById("attackerScoreValue").innerText = room.attackerScore;
  document.getElementById("defenderScoreValueAtk").innerText =
    room.defenderScore;

  // Logic to show appropriate selection screen
  if (room.status === "ATTACK_SELECTION") {
    // Show both scenario and attack selection areas
    scenarioSelectionArea.classList.remove("hidden");
    attackSelectionArea.classList.remove("hidden");

    // Update Initiate Button State based on Defender Presence
    const confirmBtn = document.getElementById("initiateAttackBtn");
    if (!room.defenderSessionId) {
      confirmBtn.disabled = true;
      confirmBtn.innerText = "Waiting for Defender...";
    } else {
      // Re-evaluate button state based on selection
      if (selectedAttacks.length === 2) {
        confirmBtn.disabled = false;
        confirmBtn.innerText = "INITIATE ATTACK";
      } else {
        confirmBtn.disabled = true;
        confirmBtn.innerText = `Select ${2 - selectedAttacks.length} more`;
      }
    }
  } else {
    scenarioSelectionArea.classList.add("hidden");
    attackSelectionArea.classList.add("hidden");
  }

  updateMissionLog(room);
}

function selectScenario(id) {
  if (id !== 1) return; // Only Scenario 1 is active

  selectedScenarioId = id;
  selectedAttacks = []; // Reset attacks

  // Reset attack buttons
  document
    .querySelectorAll(".attack-card")
    .forEach((btn) => btn.classList.remove("selected"));

  // Update visual feedback for scenario selection
  document
    .querySelectorAll(".scenario-card")
    .forEach((card) => card.classList.remove("selected"));
  event.currentTarget.classList.add("selected");

  // Check state immediately (though poll will overwrite, good for instant feedback)
  const confirmBtn = document.getElementById("initiateAttackBtn");
  // We rely on polling to know about defender, but we can set default disable
  confirmBtn.disabled = true;
  confirmBtn.innerText = "Select 2 more";
}

function toggleAttack(attackType, btnElement) {
  // Don't allow attack selection until a scenario is selected
  if (!selectedScenarioId) {
    alert("Please select a scenario first!");
    return;
  }

  const index = selectedAttacks.indexOf(attackType);

  if (index > -1) {
    // Deselect
    selectedAttacks.splice(index, 1);
    btnElement.classList.remove("selected");
  } else {
    // Select
    if (selectedAttacks.length < 2) {
      selectedAttacks.push(attackType);
      btnElement.classList.add("selected");
    } else {
      // Already 2 selected, maybe hint user?
      alert("You can only select 2 attacks.");
    }
  }

  // Update Confirm Button - Logic now relies on polling for Defender check,
  // but we can do a local check if we had the latest room state.
  // Since we don't have 'room' here, we'll let the next poll (1.5s) strictly enforce the text,
  // but we can do a provisional update assuming defender is there for responsiveness,
  // or just wait for poll.
  // BETTER: Use lastKnownState.

  const confirmBtn = document.getElementById("initiateAttackBtn");

  if (lastKnownState && !lastKnownState.defenderSessionId) {
    confirmBtn.disabled = true;
    confirmBtn.innerText = "Waiting for Defender...";
  } else {
    confirmBtn.disabled = selectedAttacks.length !== 2;
    confirmBtn.innerText =
      selectedAttacks.length === 2
        ? "INITIATE ATTACK"
        : `Select ${2 - selectedAttacks.length} more`;
  }
}

async function confirmAttack() {
  if (selectedAttacks.length < 2) return;

  const primaryAttack = selectedAttacks[0];
  const secondaryAttack = selectedAttacks[1];

  try {
    const response = await fetch(`${API_BASE_URL}/room/${myRoomId}/attack`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ roomId: myRoomId, attackType: primaryAttack }),
    });
    const room = await response.json(); // Assuming it returns the updated room state
    updateUI(room); // Update UI based on new room state
    addToLog(`MISSION INITIATED: ${selectedAttacks.join(" & ")}`);
    // Reset selections after initiating attack
    selectedScenarioId = null;
    selectedAttacks = [];
  } catch (err) {
    console.error("Error sending attack:", err);
  }
}

function updateMissionLog(room) {
  // This function can be expanded to update the log based on room state changes
  // For now, it's a placeholder. The addToLog function handles new entries.
}

function addToLog(message) {
  const log = document.getElementById("missionLog");
  const item = document.createElement("li");
  item.innerText = `> ${message}`;
  log.appendChild(item);
  log.scrollTop = log.scrollHeight; // Scroll to bottom
}

// --- Attacker Actions ---

// The old selectAttack is replaced by toggleAttack and confirmAttack
// document.querySelectorAll(".attack-card").forEach(btn => {
//     btn.addEventListener("click", () => {
//         selectAttack(btn.dataset.attack);
//     });
// });

// async function selectAttack(attackType) {
//     try {
//         await fetch(`${API_BASE_URL}/room/${myRoomId}/attack`, {
//             method: "POST",
//             headers: { "Content-Type": "application/json" },
//             body: JSON.stringify({ roomId: myRoomId, attackType: attackType })
//         });
//         logMessage(`Selected attack: ${attackType}`);
//     } catch (e) {
//         console.error(e);
//     }
// }

function logMessage(msg) {
  const log = document.getElementById("missionLog");
  const li = document.createElement("li");
  li.textContent = msg;
  log.appendChild(li);
}

// --- Defender Actions ---

// --- Defender Actions ---
let currentScenarioOptions = [];

async function loadScenarioForDefender(videoId) {
  if (!videoId) return;
  try {
    const response = await fetch(`${API_BASE_URL}/scenarios/${videoId}`);
    const scenario = await response.json();

    console.log("Loading scenario:", videoId, scenario);

    videoElement.src = scenario.videoPath;
    videoElement.load();
    optionsOverlay.classList.add("hidden");
    // Ensure "Next Scene" container is hidden when loading a new video
    document.getElementById("nextSceneContainer").classList.add("hidden");

    currentScenarioOptions = scenario.options || [];

    videoElement.play().catch((e) => console.log("Auto-play blocked", e));

    // Handle Timed Options
    videoElement.ontimeupdate = () => {
      const currentTime = videoElement.currentTime;
      // Show options if ANY option in this scenario has reached its appearTime
      const shouldShow = currentScenarioOptions.some(
        (opt) => opt.appearTime && currentTime >= opt.appearTime
      );

      if (
        shouldShow &&
        optionsOverlay.classList.contains("hidden") &&
        currentScenarioOptions.length > 0
      ) {
        showOptions(currentScenarioOptions);
      }
    };

    videoElement.onended = () => {
      console.log("Video playback finished.");

      const isLeaf = (scenario.leafNode === true || scenario.isLeafNode === true);
      const nextSceneId = scenario.nextScenarioId;

      console.log("Scenario Data:", {
        videoId: videoId,
        isLeaf: isLeaf,
        nextSceneId: nextSceneId
      });

      if (isLeaf) {
        if (nextSceneId) {
          console.log("Leaf node detected with next scene. Showing 'Next Scene' button.");
          showNextSceneButton(nextSceneId);
        } else {
          console.log("Final leaf node reached. Transitioning to game over soon.");
        }
      } else {
        // Not a leaf node (this was a main scene video)
        // If choices weren't shown (e.g. video was too short), show them now
        if (optionsOverlay.classList.contains("hidden") && currentScenarioOptions.length > 0) {
          console.log("Main video ended without choices showing. Triggering fallback.");
          showOptions(currentScenarioOptions);
        } else {
          console.log("Main video ended. Choices should already be visible.");
        }
      }
    };
  } catch (e) {
    console.error(e);
  }
}

function showOptions(options) {
  optionButtons.innerHTML = "";
  optionsOverlay.classList.remove("hidden");

  options.forEach((opt) => {
    const btn = document.createElement("button");
    btn.className = "option-btn";
    btn.textContent = opt.label;
    btn.onclick = () => sendDefenderAction(opt.id);

    // Positioning
    if (opt.position === "bottom-left") btn.classList.add("left");
    if (opt.position === "bottom-right") btn.classList.add("right");

    optionButtons.appendChild(btn);
  });
}

async function sendDefenderAction(optionId) {
  optionsOverlay.classList.add("hidden");
  try {
    const response = await fetch(`${API_BASE_URL}/room/${myRoomId}/action`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ roomId: myRoomId, optionId: optionId }),
    });
    const room = await response.json();

    // Find the option that was selected
    const opt = currentScenarioOptions.find(o => o.id === optionId);
    if (opt) {
      const scoreTag = opt.defenderScoreDelta >= 0 ? `+${opt.defenderScoreDelta}` : opt.defenderScoreDelta;
      addToLog(`Defender Choice: ${opt.label} (${scoreTag})`);

      // Load the branch video directly with transition
      console.log("Loading branch video:", opt.targetVideoId);
      triggerTransition(() => loadScenarioForDefender(opt.targetVideoId));
    }

    // Update lastKnownState to the new state to prevent polling conflict
    lastKnownState = room;
  } catch (e) {
    console.error(e);
  }
}

function showGameOver(room) {
  document.getElementById("gameOverScreen").classList.remove("hidden");
  document.getElementById("finalDefenderScore").textContent = room.defenderScore;
  document.getElementById("finalAttackerScore").textContent = room.attackerScore;

  // Populate Analysis
  const actionList = document.getElementById("actionList");
  actionList.innerHTML = "";

  const missionLog = document.getElementById("missionLog");
  const entries = Array.from(missionLog.querySelectorAll("li")).map(li => li.innerText);

  entries.forEach(entry => {
    if (entry.includes("Defender Choice")) {
      const li = document.createElement("li");
      li.style.marginBottom = "8px";
      li.style.paddingLeft = "10px";
      li.style.borderLeft = "2px solid var(--accent-blue)";
      li.innerText = entry;
      actionList.appendChild(li);
    }
  });

  if (actionList.innerHTML === "") {
    actionList.innerHTML = "<li style='color: var(--text-secondary);'>No activity recorded.</li>";
  }
}

// Manual Scene Progression
function showNextSceneButton(nextScenarioId) {
  console.log("showNextSceneButton called with:", nextScenarioId);

  // Hide options overlay to ensure button is visible
  const optionsOverlay = document.getElementById("optionsOverlay");
  if (optionsOverlay) {
    optionsOverlay.classList.add("hidden");
  }

  const nextSceneContainer = document.getElementById("nextSceneContainer");
  const nextSceneBtn = document.getElementById("nextSceneBtn");

  if (!nextSceneContainer || !nextSceneBtn) {
    console.error("Next Scene button elements not found!");
    return;
  }

  // Change text for final scene
  if (nextScenarioId === "GAMEOVER") {
    nextSceneBtn.textContent = "SHOW RESULTS";
  } else {
    nextSceneBtn.textContent = "NEXT SCENE";
  }

  console.log("Showing Next Scene button...");
  nextSceneContainer.classList.remove("hidden");

  // Remove old listeners by cloning the button
  const newBtn = nextSceneBtn.cloneNode(true);
  nextSceneBtn.parentNode.replaceChild(newBtn, nextSceneBtn);

  // Add click handler
  newBtn.addEventListener("click", () => {
    nextSceneContainer.classList.add("hidden");
    if (nextScenarioId === "GAMEOVER") {
      // Trigger game over screen by fetching final status
      pollGameState();
    } else {
      // Sync with backend that we are moving to next scene
      fetch(`${API_BASE_URL}/room/${myRoomId}/video/${nextScenarioId}`, {
        method: "PUT",
      })
        .then(res => res.json())
        .then(room => {
          lastKnownState = room; // Sync state to prevent polling conflict
          triggerTransition(() => loadScenarioForDefender(nextScenarioId));
        })
        .catch(e => {
          console.error("Sync failed, proceeding anyway", e);
          triggerTransition(() => loadScenarioForDefender(nextScenarioId));
        });
    }
  });

  console.log("Next Scene button is now visible and clickable");
}

// Initial Bindings
document.getElementById("createRoomBtn").addEventListener("click", createRoom);
document.getElementById("joinRoomBtn").addEventListener("click", joinRoom);
document
  .getElementById("restartButton")
  .addEventListener("click", () => location.reload()); // Reloads page, restarting from loading screen

// Expose functions to window for HTML onclick attributes
window.startGame = startGame;
window.showScreen = showScreen;
window.exitGame = exitGame;
window.updateSetting = updateSetting;
window.hideGameComponents = hideGameComponents;
window.selectScenario = selectScenario;
window.toggleAttack = toggleAttack;
window.confirmAttack = confirmAttack;
window.sendDefenderAction = sendDefenderAction;