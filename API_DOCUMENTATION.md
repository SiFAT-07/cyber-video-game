# CyberWalk API Documentation

## Base URL
```
http://localhost:8080/api
```

## Endpoints

### 1. Scenario Management

#### Get Scenario by Video ID
Retrieves a scenario with its video information and available options.

**Endpoint:** `GET /scenarios/{videoId}`

**Path Parameters:**
- `videoId` (String): The unique identifier for the video (e.g., "1", "1_1", "1_2")

**Response Example:**
```json
{
  "videoId": "1",
  "videoPath": "/video/1.mp4",
  "description": "Initial scenario - Choose your path wisely!",
  "isLeafNode": false,
  "options": [
    {
      "id": 1,
      "label": "Choice A",
      "targetVideoId": "1_1",
      "scoreChange": 10,
      "position": "bottom-left",
      "interactionType": "click"
    },
    {
      "id": 2,
      "label": "Choice B",
      "targetVideoId": "1_2",
      "scoreChange": -5,
      "position": "bottom-right",
      "interactionType": "click"
    }
  ]
}
```

#### Get All Scenarios
Retrieves all scenarios in the system.

**Endpoint:** `GET /scenarios`

**Response Example:**
```json
[
  {
    "videoId": "1",
    "videoPath": "/video/1.mp4",
    "description": "Initial scenario - Choose your path wisely!",
    "isLeafNode": false,
    "options": [...]
  },
  {
    "videoId": "1_1",
    "videoPath": "/video/1_1.mp4",
    "description": "You chose path A - Good choice!",
    "isLeafNode": true,
    "options": []
  }
]
```

### 2. Game Session Management

#### Start New Session
Creates a new game session.

**Endpoint:** `POST /session/start`

**Response Example:**
```json
{
  "sessionId": "550e8400-e29b-41d4-a716-446655440000",
  "currentScore": 0,
  "currentVideoId": "1",
  "isCompleted": false
}
```

#### Get Session
Retrieves the current state of a game session.

**Endpoint:** `GET /session/{sessionId}`

**Path Parameters:**
- `sessionId` (String): UUID of the game session

**Response Example:**
```json
{
  "sessionId": "550e8400-e29b-41d4-a716-446655440000",
  "currentScore": 10,
  "currentVideoId": "1_1",
  "isCompleted": false
}
```

#### Record Player Choice
Records a player's choice and updates the session.

**Endpoint:** `POST /session/choice`

**Request Body:**
```json
{
  "sessionId": "550e8400-e29b-41d4-a716-446655440000",
  "optionId": 1,
  "targetVideoId": "1_1",
  "scoreChange": 10
}
```

**Response Example:**
```json
{
  "sessionId": "550e8400-e29b-41d4-a716-446655440000",
  "currentScore": 10,
  "currentVideoId": "1_1",
  "isCompleted": false
}
```

#### Complete Session
Marks a game session as completed.

**Endpoint:** `POST /session/complete/{sessionId}`

**Path Parameters:**
- `sessionId` (String): UUID of the game session

**Response Example:**
```json
{
  "sessionId": "550e8400-e29b-41d4-a716-446655440000",
  "currentScore": 10,
  "currentVideoId": "1_1",
  "isCompleted": true
}
```

## Data Models

### Scenario
```typescript
{
  videoId: string;           // Unique identifier (e.g., "1", "1_1")
  videoPath: string;         // Path to video file (e.g., "/video/1.mp4")
  description: string;       // Scenario description
  isLeafNode: boolean;       // True if no further options available
  options: Option[];         // Array of available options
}
```

### Option
```typescript
{
  id: number;                // Unique identifier
  label: string;             // Display text for button
  targetVideoId: string;     // Video ID to play next
  scoreChange: number;       // Points gained/lost (-100 to +100)
  position: string;          // "bottom-left" | "bottom-right"
  interactionType: string;   // "click" | "hotspot" | "drag" | "keyboard"
}
```

### GameSession
```typescript
{
  sessionId: string;         // UUID
  currentScore: number;      // Player's current score
  currentVideoId: string;    // Current video being played
  isCompleted: boolean;      // Whether game is finished
}
```

## Error Responses

All endpoints return standard HTTP status codes:

- **200 OK**: Successful operation
- **404 Not Found**: Scenario or session not found
- **500 Internal Server Error**: Server error

**Error Response Format:**
```json
{
  "timestamp": "2024-12-12T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Scenario not found for videoId: 999",
  "path": "/api/scenarios/999"
}
```

## CORS Configuration

The API allows cross-origin requests from all origins (`*`) for development. For production, configure specific origins in `WebConfig.java`.

## Video File Access

Videos are served as static resources:

**Endpoint:** `GET /video/{filename}`

**Example:**
```
http://localhost:8080/video/1.mp4
```

## Frontend Integration Example

### Initialize Game
```javascript
// Start new session
const response = await fetch('http://localhost:8080/api/session/start', {
  method: 'POST'
});
const session = await response.json();
console.log(session.sessionId); // Save this!

// Load first scenario
const scenarioResponse = await fetch(`http://localhost:8080/api/scenarios/1`);
const scenario = await scenarioResponse.json();
```

### Record Choice
```javascript
const choice = {
  sessionId: "550e8400-e29b-41d4-a716-446655440000",
  optionId: 1,
  targetVideoId: "1_1",
  scoreChange: 10
};

const response = await fetch('http://localhost:8080/api/session/choice', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(choice)
});

const updatedSession = await response.json();
console.log('New score:', updatedSession.currentScore);
```

## Testing with cURL

### Get Scenario
```bash
curl http://localhost:8080/api/scenarios/1
```

### Start Session
```bash
curl -X POST http://localhost:8080/api/session/start
```

### Record Choice
```bash
curl -X POST http://localhost:8080/api/session/choice \
  -H "Content-Type: application/json" \
  -d '{
    "sessionId": "550e8400-e29b-41d4-a716-446655440000",
    "optionId": 1,
    "targetVideoId": "1_1",
    "scoreChange": 10
  }'
```

## Database Schema

### SCENARIOS Table
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary key |
| video_id | VARCHAR | Unique video identifier |
| video_path | VARCHAR | Path to video file |
| description | VARCHAR | Scenario description |
| is_leaf_node | BOOLEAN | No further options flag |

### OPTIONS Table
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary key |
| scenario_id | BIGINT | Foreign key to SCENARIOS |
| label | VARCHAR | Button text |
| target_video_id | VARCHAR | Next video ID |
| score_change | INT | Points change |
| position | VARCHAR | UI position |
| interaction_type | VARCHAR | Interaction method |

### GAME_SESSIONS Table
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary key |
| session_id | VARCHAR | UUID |
| current_score | INT | Player score |
| current_video_id | VARCHAR | Current video |
| start_time | TIMESTAMP | Session start |
| last_updated | TIMESTAMP | Last update |
| is_completed | BOOLEAN | Completion flag |

## Rate Limiting

Currently no rate limiting is implemented. For production, consider adding rate limiting middleware.

## Authentication

Currently no authentication is required. For production with user accounts, implement JWT or OAuth2.
