// API Configuration
const API_BASE_URL = "http://localhost:8080/api";

// Game State
let currentSession = null;
let currentScore = 0;
let currentVideoId = "1";
let currentScenario = null;

// DOM Elements
const videoElement = document.getElementById("mainVideo");
const scoreElement = document.getElementById("score");
const optionsOverlay = document.getElementById("optionsOverlay");
const optionButtons = document.getElementById("optionButtons");
const loadingIndicator = document.getElementById("loadingIndicator");
const gameOverScreen = document.getElementById("gameOverScreen");
const finalScoreElement = document.getElementById("finalScore");
const restartButton = document.getElementById("restartButton");

// Initialize Game
async function initializeGame() {
  try {
    showLoading();

    // Start a new game session
    const sessionResponse = await fetch(`${API_BASE_URL}/session/start`, {
      method: "POST",
    });

    if (!sessionResponse.ok) {
      throw new Error("Failed to start session");
    }

    currentSession = await sessionResponse.json();
    currentScore = currentSession.currentScore;
    currentVideoId = currentSession.currentVideoId;

    updateScoreDisplay();
    await loadScenario(currentVideoId);

    hideLoading();
  } catch (error) {
    console.error("Error initializing game:", error);
    alert("Failed to start the game. Please refresh the page.");
    hideLoading();
  }
}

// Load Scenario
async function loadScenario(videoId) {
  try {
    showLoading();

    const response = await fetch(`${API_BASE_URL}/scenarios/${videoId}`);

    if (!response.ok) {
      throw new Error(`Failed to load scenario: ${videoId}`);
    }

    currentScenario = await response.json();
    currentVideoId = videoId;

    // Load and play video
    videoElement.src = currentScenario.videoPath;
    videoElement.load();

    // Hide options initially
    hideOptions();

    // Wait for video to be ready
    videoElement.addEventListener(
      "loadeddata",
      () => {
        hideLoading();
        videoElement.play().catch((e) => {
          console.error("Error playing video:", e);
          // On mobile, user interaction is required
          alert("Please tap to start the video");
        });
      },
      { once: true }
    );

    // Show options when video ends
    videoElement.addEventListener("ended", handleVideoEnd, { once: true });
  } catch (error) {
    console.error("Error loading scenario:", error);
    alert(`Failed to load scenario: ${videoId}`);
    hideLoading();
  }
}

// Handle Video End
function handleVideoEnd() {
  if (currentScenario.isLeafNode) {
    // This is the end of a path - show game over
    showGameOver();
  } else {
    // Show options for the next choice
    displayOptions();
  }
}

// Display Options
function displayOptions() {
  // Clear previous options
  optionButtons.innerHTML = "";

  if (!currentScenario.options || currentScenario.options.length === 0) {
    return;
  }

  currentScenario.options.forEach((option) => {
    const button = document.createElement("button");
    button.className = "option-btn";
    button.textContent = option.label;

    // Add position class
    if (option.position === "bottom-left") {
      button.classList.add("left");
    } else if (option.position === "bottom-right") {
      button.classList.add("right");
    }

    // Add score-based styling
    if (option.scoreChange > 0) {
      button.classList.add("positive");
    } else if (option.scoreChange < 0) {
      button.classList.add("negative");
    }

    // Add click handler
    button.addEventListener("click", () => handleOptionClick(option));

    optionButtons.appendChild(button);
  });

  showOptions();
}

// Handle Option Click
async function handleOptionClick(option) {
  try {
    hideOptions();
    showLoading();

    // Send choice to backend
    const response = await fetch(`${API_BASE_URL}/session/choice`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        sessionId: currentSession.sessionId,
        optionId: option.id,
        targetVideoId: option.targetVideoId,
        scoreChange: option.scoreChange,
      }),
    });

    if (!response.ok) {
      throw new Error("Failed to record choice");
    }

    const updatedSession = await response.json();
    currentScore = updatedSession.currentScore;
    updateScoreDisplay();

    // Load next scenario
    await loadScenario(option.targetVideoId);
  } catch (error) {
    console.error("Error handling option click:", error);
    alert("Failed to process your choice. Please try again.");
    hideLoading();
    displayOptions(); // Show options again
  }
}

// Show Game Over
async function showGameOver() {
  try {
    // Complete the session
    await fetch(
      `${API_BASE_URL}/session/complete/${currentSession.sessionId}`,
      {
        method: "POST",
      }
    );
  } catch (error) {
    console.error("Error completing session:", error);
  }

  finalScoreElement.textContent = currentScore;
  gameOverScreen.classList.remove("hidden");
}

// Restart Game
function restartGame() {
  gameOverScreen.classList.add("hidden");
  currentSession = null;
  currentScore = 0;
  currentVideoId = "1";
  currentScenario = null;

  // Reset video
  videoElement.pause();
  videoElement.currentTime = 0;

  // Restart game
  initializeGame();
}

// Update Score Display
function updateScoreDisplay() {
  scoreElement.textContent = currentScore;

  // Add animation effect
  scoreElement.style.transform = "scale(1.3)";
  setTimeout(() => {
    scoreElement.style.transform = "scale(1)";
  }, 300);
}

// UI Helper Functions
function showOptions() {
  optionsOverlay.classList.remove("hidden");
}

function hideOptions() {
  optionsOverlay.classList.add("hidden");
}

function showLoading() {
  loadingIndicator.classList.remove("hidden");
}

function hideLoading() {
  loadingIndicator.classList.add("hidden");
}

// Event Listeners
restartButton.addEventListener("click", restartGame);

// Handle video errors
videoElement.addEventListener("error", (e) => {
  console.error("Video error:", e);
  hideLoading();
  alert("Error loading video. Please check if the video file exists.");
});

// Prevent right-click on video (optional - for production)
videoElement.addEventListener("contextmenu", (e) => {
  e.preventDefault();
});

// Start the game when page loads
window.addEventListener("load", initializeGame);

// Add smooth transition for score changes
scoreElement.style.transition = "transform 0.3s ease";
