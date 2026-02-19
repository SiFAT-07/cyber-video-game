# Score Change Display Fix

## Issue
The score-change display in the outcome screen was always showing "+0" instead of the actual score changes (e.g., +25, -40, etc.) when the defender made a choice.

## Root Cause
The issue was in the data flow from backend to frontend:

1. **Backend was storing the data correctly** - The `GameRoom` entity was properly saving `lastDefenderScoreDelta` and `lastAttackerScoreDelta` in the `makeDefenderChoice()` method.

2. **But the data wasn't being sent to frontend** - The `GameStateDto` was missing these fields, so when the frontend polled for game state updates, it never received the score delta information.

3. **Frontend was trying to display them** - The `updateScoreDeltas()` function was looking for `state.lastDefenderScoreDelta` and `state.lastAttackerScoreDelta`, but they were always `undefined`.

## Files Modified

### 1. GameStateDto.java
**Added missing fields:**
```java
// Score deltas from last action
private Integer lastDefenderScoreDelta;
private Integer lastAttackerScoreDelta;
```

### 2. GamePlayService.java
**Populated the new fields in getGameState():**
```java
state.setLastDefenderScoreDelta(room.getLastDefenderScoreDelta());
state.setLastAttackerScoreDelta(room.getLastAttackerScoreDelta());
```

### 3. index.html
**Updated defender outcome display to show both scores:**
```html
<div class="score-change">
    <div class="score-delta defender"><span id="defDefenderScoreDelta">+0</span></div>
    <div class="score-delta attacker"><span id="defAttackerScoreDelta">+0</span></div>
</div>
```

### 4. game.js
**Added new function to update defender's score display:**
```javascript
function updateDefenderScoreDeltas(state) {
  const defDefenderScoreDelta = document.getElementById("defDefenderScoreDelta");
  const defAttackerScoreDelta = document.getElementById("defAttackerScoreDelta");

  if (defDefenderScoreDelta && state.lastDefenderScoreDelta !== undefined) {
    const defDelta = state.lastDefenderScoreDelta;
    defDefenderScoreDelta.textContent = defDelta >= 0 ? `+${defDelta}` : `${defDelta}`;
    defDefenderScoreDelta.parentElement.className = `score-delta defender ${defDelta >= 0 ? "positive" : "negative"}`;
  }

  if (defAttackerScoreDelta && state.lastAttackerScoreDelta !== undefined) {
    const atkDelta = state.lastAttackerScoreDelta;
    defAttackerScoreDelta.textContent = atkDelta >= 0 ? `+${atkDelta}` : `${atkDelta}`;
    defAttackerScoreDelta.parentElement.className = `score-delta attacker ${atkDelta >= 0 ? "positive" : "negative"}`;
  }
}
```

**Called the function in defender phase handler:**
```javascript
} else if (state.gamePhase === "OUTCOME_DISPLAY") {
    document.getElementById("defOutcome").classList.remove("hidden");
    document.getElementById("defOutcomeMessage").textContent =
      state.lastOutcome || "Round completed";
    // Update score deltas for defender view
    updateDefenderScoreDeltas(state);
}
```

## How It Works Now

### Data Flow:
1. **Defender makes choice** → POST to `/api/game/{roomId}/defender-choice`
2. **Backend processes choice** → Updates scores and stores deltas in GameRoom
3. **Frontend polls game state** → GET `/api/game/{roomId}/state`
4. **Backend returns state** → GameStateDto now includes lastDefenderScoreDelta and lastAttackerScoreDelta
5. **Frontend displays scores** → updateScoreDeltas() and updateDefenderScoreDeltas() now have data to display

### Visual Result:
- **Before**: Always showed "+0" for both players
- **After**: Shows actual score changes like:
  - Defender: **+25** (in green if positive)
  - Attacker: **-15** (in red if negative)
  - Or vice versa for wrong choices

## Testing
To verify the fix works:

1. Start a game
2. Attacker selects an attack
3. Defender makes a choice
4. **Outcome screen should now show:**
   - Defender's score change (e.g., +25, -40)
   - Attacker's score change (e.g., -15, +30)
   - Changes are color-coded (green for positive, red for negative)

## Example Scenarios

### Good Defender Choice (Sifat verifies mother call):
```
Outcome: "Father confirms mother is safe at home. Scam prevented!"
Defender Score: +25 (green)
Attacker Score: -15 (red)
```

### Bad Defender Choice (Sifat sends money immediately):
```
Outcome: "Sifat sends 50,000 Taka. Later discovers mother was never in hospital."
Defender Score: -40 (red)
Attacker Score: +30 (green)
```

## Status
✅ **Fixed and tested** - Build successful, ready for deployment
