// API Configuration
const API_BASE_URL = "http://localhost:8080/api";

// State
let myRoomId = null;
let myRole = null;
let pollInterval = null;
let lastKnownState = null;

// DOM Elements
const lobbyScreen = document.getElementById("lobbyScreen");
const attackerDashboard = document.getElementById("attackerDashboard");
const defenderGameArea = document.getElementById("defenderGameArea");
const lobbyStatus = document.getElementById("lobbyStatus");
const videoElement = document.getElementById("mainVideo");
const optionButtons = document.getElementById("optionButtons");
const optionsOverlay = document.getElementById("optionsOverlay");

// --- Lobby Functions ---

async function createRoom() {
    try {
        const response = await fetch(`${API_BASE_URL}/room/create`, { method: "POST" });
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
            body: JSON.stringify({ roomId: roomIdInput, role: roleSelect })
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
    lobbyScreen.classList.add("hidden");
    
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

function updateUI(room) {
    // Score Updates
    if (myRole === "ATTACKER") {
        document.getElementById("attackerScoreValue").textContent = room.attackerScore;
        document.getElementById("defenderScoreValueAtk").textContent = room.defenderScore;
    } else {
        document.getElementById("defenderScoreValue").textContent = room.defenderScore;
    }

    // State Machine
    if (room.status === "ATTACK_SELECTION") {
        if (myRole === "ATTACKER") {
            document.getElementById("attackSelectionArea").classList.remove("hidden");
        } else {
            // Defender Waiting
            document.getElementById("loadingIndicator").classList.remove("hidden");
            document.querySelector("#loadingIndicator p").textContent = "Waiting for Attacker to select a scenario...";
        }
    } else if (room.status === "DEFENDER_TURN") {
        document.getElementById("loadingIndicator").classList.add("hidden");
        
        if (myRole === "ATTACKER") {
            document.getElementById("attackSelectionArea").classList.add("hidden");
            // Show log?
        } else {
            // Defender Playing
            if (!lastKnownState || lastKnownState.currentVideoId !== room.currentVideoId) {
                loadScenarioForDefender(room.currentVideoId);
            }
        }
    } else if (room.status === "ROUND_OVER") {
        clearInterval(pollInterval);
        showGameOver(room);
    }
}

// --- Attacker Actions ---

document.querySelectorAll(".attack-card").forEach(btn => {
    btn.addEventListener("click", () => {
        selectAttack(btn.dataset.attack);
    });
});

async function selectAttack(attackType) {
    try {
        await fetch(`${API_BASE_URL}/room/${myRoomId}/attack`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ roomId: myRoomId, attackType: attackType })
        });
        logMessage(`Selected attack: ${attackType}`);
    } catch (e) {
        console.error(e);
    }
}

function logMessage(msg) {
    const log = document.getElementById("missionLog");
    const li = document.createElement("li");
    li.textContent = msg;
    log.appendChild(li);
}

// --- Defender Actions ---

async function loadScenarioForDefender(videoId) {
    try {
         const response = await fetch(`${API_BASE_URL}/scenarios/${videoId}`);
         const scenario = await response.json();
         
         videoElement.src = scenario.videoPath;
         videoElement.load();
         optionsOverlay.classList.add("hidden");
         
         videoElement.play().catch(e => console.log("Auto-play blocked", e));
         
         videoElement.onended = () => {
             if (scenario.leafNode) {
                 // Wait for round over signal or score
             } else {
                 showOptions(scenario.options);
             }
         };
    } catch (e) {
        console.error(e);
    }
}

function showOptions(options) {
    optionButtons.innerHTML = "";
    optionsOverlay.classList.remove("hidden");
    
    options.forEach(opt => {
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
        await fetch(`${API_BASE_URL}/room/${myRoomId}/action`, {
             method: "POST",
             headers: { "Content-Type": "application/json" },
             body: JSON.stringify({ roomId: myRoomId, optionId: optionId })
        });
    } catch (e) {
        console.error(e);
    }
}

function showGameOver(room) {
    document.getElementById("gameOverScreen").classList.remove("hidden");
    document.getElementById("finalDefenderScore").textContent = room.defenderScore;
    document.getElementById("finalAttackerScore").textContent = room.attackerScore;
}

// Initial Bindings
document.getElementById("createRoomBtn").addEventListener("click", createRoom);
document.getElementById("joinRoomBtn").addEventListener("click", joinRoom);
document.getElementById("restartButton").addEventListener("click", () => location.reload());
