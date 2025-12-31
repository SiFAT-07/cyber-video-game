# How to Extend CyberWalk

This guide shows you how to add new scenarios, customize interactions, and extend the game.

## Adding New Scenarios

### Step 1: Add Video Files
Place your video files in the `video/` folder following the naming convention:
- Parent video: `2.mp4`
- Child videos: `2_1.mp4`, `2_2.mp4`, `2_3.mp4`, etc.

### Step 2: Update DataInitializer.java

Open `src/main/java/com/university/cyberwalk/config/DataInitializer.java` and add:

```java
// Create parent scenario (video 2)
Scenario scenario2 = new Scenario();
scenario2.setVideoId("2");
scenario2.setVideoPath("/video/2.mp4");
scenario2.setDescription("Second scenario - Critical decision ahead!");
scenario2.setLeafNode(false);

// Create options for scenario 2
Option option2_1 = new Option();
option2_1.setLabel("Safe Route");
option2_1.setTargetVideoId("2_1");
option2_1.setScoreChange(5);
option2_1.setPosition("bottom-left");
option2_1.setInteractionType("click");
option2_1.setScenario(scenario2);

Option option2_2 = new Option();
option2_2.setLabel("Risky Route");
option2_2.setTargetVideoId("2_2");
option2_2.setScoreChange(20);
option2_2.setPosition("bottom-right");
option2_2.setInteractionType("click");
option2_2.setScenario(scenario2);

scenario2.setOptions(Arrays.asList(option2_1, option2_2));
scenarioRepository.save(scenario2);

// Create child scenarios
Scenario scenario2_1 = new Scenario();
scenario2_1.setVideoId("2_1");
scenario2_1.setVideoPath("/video/2_1.mp4");
scenario2_1.setDescription("You took the safe route");
scenario2_1.setLeafNode(true);
scenario2_1.setOptions(new ArrayList<>());
scenarioRepository.save(scenario2_1);

Scenario scenario2_2 = new Scenario();
scenario2_2.setVideoId("2_2");
scenario2_2.setVideoPath("/video/2_2.mp4");
scenario2_2.setDescription("You took the risky route - high reward!");
scenario2_2.setLeafNode(true);
scenario2_2.setOptions(new ArrayList<>());
scenarioRepository.save(scenario2_2);
```

### Step 3: Link Scenarios
To link scenario 1_1 to scenario 2 (instead of being a leaf node):

```java
// Modify scenario1_1 to not be a leaf node
scenario1_1.setLeafNode(false);

// Add option to continue to scenario 2
Option option1_1_continue = new Option();
option1_1_continue.setLabel("Continue Adventure");
option1_1_continue.setTargetVideoId("2");
option1_1_continue.setScoreChange(0);
option1_1_continue.setPosition("bottom-left");
option1_1_continue.setInteractionType("click");
option1_1_continue.setScenario(scenario1_1);

scenario1_1.setOptions(Arrays.asList(option1_1_continue));
scenarioRepository.save(scenario1_1);
```

## Custom Score Values

### Positive Scores (Good Choices)
```java
option.setScoreChange(10);  // Small reward
option.setScoreChange(25);  // Medium reward
option.setScoreChange(50);  // Large reward
```

### Negative Scores (Bad Choices)
```java
option.setScoreChange(-5);   // Minor penalty
option.setScoreChange(-15);  // Medium penalty
option.setScoreChange(-30);  // Critical mistake
```

### Neutral Choices
```java
option.setScoreChange(0);    // No impact
```

## Button Positions

### Two Options (Current)
```java
option1.setPosition("bottom-left");
option2.setPosition("bottom-right");
```

### Three Options (Horizontal)
Modify CSS in `style.css`:
```css
#optionButtons {
    justify-content: space-around; /* Instead of space-between */
}

.option-btn.center {
    align-self: center;
}
```

Then in Java:
```java
option1.setPosition("bottom-left");
option2.setPosition("center");
option3.setPosition("bottom-right");
```

### Custom Positions
For specific coordinates, add to `Option.java`:
```java
private int positionX; // Percentage from left
private int positionY; // Percentage from top
```

Update CSS dynamically in JavaScript:
```javascript
button.style.position = 'absolute';
button.style.left = option.positionX + '%';
button.style.top = option.positionY + '%';
```

## Adding Interaction Types

### 1. Keyboard Interaction

**Backend (Option.java):**
```java
option.setInteractionType("keyboard");
option.setKeyCode("KeyA"); // Which key to press
```

**Frontend (app.js):**
```javascript
function setupKeyboardInteraction(option) {
    document.addEventListener('keydown', (e) => {
        if (e.code === option.keyCode) {
            handleOptionClick(option);
        }
    }, { once: true });
}
```

### 2. Hotspot Interaction

**Backend:**
```java
option.setInteractionType("hotspot");
option.setHotspotX(50);  // Percentage from left
option.setHotspotY(50);  // Percentage from top
option.setHotspotRadius(10); // Click radius
```

**Frontend:**
```javascript
function createHotspot(option) {
    const hotspot = document.createElement('div');
    hotspot.className = 'hotspot';
    hotspot.style.left = option.hotspotX + '%';
    hotspot.style.top = option.hotspotY + '%';
    hotspot.onclick = () => handleOptionClick(option);
    optionsOverlay.appendChild(hotspot);
}
```

**CSS:**
```css
.hotspot {
    position: absolute;
    width: 50px;
    height: 50px;
    border: 3px solid yellow;
    border-radius: 50%;
    cursor: pointer;
    animation: pulse 2s infinite;
}

@keyframes pulse {
    0%, 100% { transform: scale(1); opacity: 0.7; }
    50% { transform: scale(1.2); opacity: 1; }
}
```

### 3. Drag Interaction

**Backend:**
```java
option.setInteractionType("drag");
option.setDragTargetId("dragTarget1");
```

**Frontend:**
```javascript
function setupDragInteraction(option) {
    const draggable = document.getElementById(option.dragSourceId);
    const target = document.getElementById(option.dragTargetId);
    
    draggable.draggable = true;
    draggable.addEventListener('dragstart', (e) => {
        e.dataTransfer.setData('optionId', option.id);
    });
    
    target.addEventListener('drop', (e) => {
        e.preventDefault();
        const optionId = e.dataTransfer.getData('optionId');
        if (optionId === option.id.toString()) {
            handleOptionClick(option);
        }
    });
}
```

## Adding Timed Choices

Show options only for a limited time:

**Backend (Option.java):**
```java
private int timeLimit; // Seconds before option disappears
```

**Frontend:**
```javascript
function displayTimedOption(option) {
    const button = createOptionButton(option);
    optionButtons.appendChild(button);
    
    // Auto-hide after time limit
    setTimeout(() => {
        if (button.parentElement) {
            button.remove();
        }
    }, option.timeLimit * 1000);
}
```

## Complex Decision Trees

### Branching Paths
```
1 (start)
├── 1_1 (good choice +10)
│   ├── 1_1_1 (continue good +15)
│   └── 1_1_2 (mistake -20)
└── 1_2 (bad choice -5)
    ├── 1_2_1 (redemption +25)
    └── 1_2_2 (worse -30)
```

### Convergent Paths
Multiple choices lead to same video:
```
1
├── 1_1 → 2 (same result)
└── 1_2 → 2 (same result)
```

## Adding Statistics

### Track Decision History

**GameSession.java:**
```java
@Column(columnDefinition = "TEXT")
private String decisionHistory; // JSON array of choices
```

**GameSessionService.java:**
```java
public void recordDecision(String sessionId, String videoId, Long optionId) {
    // Append to decision history
    String history = session.getDecisionHistory();
    JSONArray decisions = new JSONArray(history);
    JSONObject decision = new JSONObject();
    decision.put("videoId", videoId);
    decision.put("optionId", optionId);
    decision.put("timestamp", System.currentTimeMillis());
    decisions.put(decision);
    session.setDecisionHistory(decisions.toString());
}
```

## Adding Achievements

### Backend

**Achievement.java:**
```java
@Entity
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    private String condition; // e.g., "score > 100"
    private String icon;
}
```

### Frontend
```javascript
function checkAchievements(score) {
    if (score >= 100) {
        showAchievement("Master Decision Maker");
    }
    if (score < 0) {
        showAchievement("Learning Experience");
    }
}
```

## Adding Sound Effects

### Frontend
```javascript
const correctSound = new Audio('/sounds/correct.mp3');
const wrongSound = new Audio('/sounds/wrong.mp3');

function handleOptionClick(option) {
    if (option.scoreChange > 0) {
        correctSound.play();
    } else if (option.scoreChange < 0) {
        wrongSound.play();
    }
    // ... rest of logic
}
```

## Adding Video Transitions

### Fade Effect
```css
#mainVideo {
    transition: opacity 0.5s ease;
}

#mainVideo.fading {
    opacity: 0;
}
```

```javascript
function loadScenarioWithTransition(videoId) {
    videoElement.classList.add('fading');
    
    setTimeout(() => {
        loadScenario(videoId);
        videoElement.classList.remove('fading');
    }, 500);
}
```

## Adding Subtitles/Captions

### Frontend
```html
<video id="mainVideo" controls>
    <source src="" type="video/mp4">
    <track kind="subtitles" src="/captions/1.vtt" srclang="en" label="English">
</video>
```

### Backend
Serve caption files from static resources:
```
src/main/resources/static/captions/
├── 1.vtt
├── 1_1.vtt
└── 1_2.vtt
```

## Adding Analytics

### Track User Behavior

**Analytics.java:**
```java
@Entity
public class Analytics {
    private String sessionId;
    private String eventType; // "video_start", "video_end", "choice_made"
    private String videoId;
    private LocalDateTime timestamp;
    private String metadata; // JSON
}
```

### Frontend
```javascript
function trackEvent(eventType, data) {
    fetch('/api/analytics', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            eventType: eventType,
            data: data,
            timestamp: new Date().toISOString()
        })
    });
}

// Usage
trackEvent('video_start', { videoId: '1' });
trackEvent('choice_made', { optionId: 2, scoreChange: -5 });
```

## Testing Your Extensions

### 1. Test Video Playback
```javascript
console.log('Video source:', videoElement.src);
console.log('Can play:', videoElement.canPlayType('video/mp4'));
```

### 2. Test API Endpoints
```bash
# Get new scenario
curl http://localhost:8080/api/scenarios/2

# Check database
# Open H2 Console: http://localhost:8080/h2-console
# Run: SELECT * FROM SCENARIOS WHERE video_id = '2';
```

### 3. Test Score Calculation
Add logging in `GameSessionService.java`:
```java
System.out.println("Old score: " + session.getCurrentScore());
System.out.println("Score change: " + request.getScoreChange());
System.out.println("New score: " + (session.getCurrentScore() + request.getScoreChange()));
```

## Common Issues

### Video Not Found
- Check file exists in `video/` folder
- Verify file name matches exactly (case-sensitive)
- Check video path in database matches

### Options Not Showing
- Verify scenario is not a leaf node
- Check options are saved to database
- Verify JavaScript is fetching options correctly

### Score Not Updating
- Check `scoreChange` value in option
- Verify session ID is being passed
- Check browser console for errors

## Performance Tips

### Preload Videos
```javascript
function preloadVideo(videoId) {
    const video = document.createElement('video');
    video.src = `/video/${videoId}.mp4`;
    video.preload = 'auto';
}

// Preload next videos when options appear
function displayOptions() {
    currentScenario.options.forEach(option => {
        preloadVideo(option.targetVideoId);
    });
    // ... rest of code
}
```

### Lazy Load Database
For large decision trees, load scenarios on-demand instead of all at startup.

## Next Steps

1. **Add more scenarios** - Build a complete story
2. **Implement achievements** - Reward players
3. **Add sound effects** - Enhance immersion
4. **Create admin panel** - Manage scenarios via UI
5. **Add user accounts** - Track player progress
6. **Implement leaderboard** - Competitive element
7. **Add video analytics** - Track watch patterns

## Support

For questions or issues:
1. Check `ARCHITECTURE.md` for system design
2. Review `API_DOCUMENTATION.md` for API details
3. Inspect browser console for JavaScript errors
4. Check Spring Boot logs for backend errors
5. Use H2 Console to verify database state

Happy extending! 🎮
