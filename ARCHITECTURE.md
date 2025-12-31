# CyberWalk Architecture

## System Overview

CyberWalk is a full-stack interactive story game built with a clear separation between frontend and backend concerns. The architecture follows a 3-tier pattern with presentation layer (frontend), business logic layer (Spring Boot services), and data access layer (JPA repositories).

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                          FRONTEND LAYER                          │
│                      (HTML5 + CSS3 + JavaScript)                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌──────────────┐  ┌──────────────┐  ┌────────────────────┐   │
│  │  index.html  │  │  style.css   │  │     app.js         │   │
│  │              │  │              │  │  - Game Logic      │   │
│  │ - Video UI   │  │ - Responsive │  │  - API Calls       │   │
│  │ - Overlays   │  │ - Animations │  │  - State Mgmt      │   │
│  └──────────────┘  └──────────────┘  └────────────────────┘   │
│                                                                   │
└────────────────────────────┬──────────────────────────────────┘
                             │
                             │ HTTP REST API
                             │ (JSON over HTTP)
                             │
┌────────────────────────────▼──────────────────────────────────┐
│                       BACKEND LAYER                             │
│                    (Spring Boot 3.2.0)                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌───────────────── CONTROLLER LAYER ─────────────────────┐    │
│  │                                                          │    │
│  │  ┌─────────────────────┐  ┌──────────────────────┐    │    │
│  │  │ ScenarioController  │  │ GameSessionController │    │    │
│  │  │                     │  │                       │    │    │
│  │  │ GET /scenarios/{id} │  │ POST /session/start   │    │    │
│  │  │ GET /scenarios      │  │ POST /session/choice  │    │    │
│  │  └─────────────────────┘  └──────────────────────┘    │    │
│  │                                                          │    │
│  └──────────────────────────────────────────────────────────┘    │
│                             │                                     │
│  ┌───────────────── SERVICE LAYER ────────────────────────┐    │
│  │                                                          │    │
│  │  ┌─────────────────────┐  ┌──────────────────────┐    │    │
│  │  │  ScenarioService    │  │  GameSessionService  │    │    │
│  │  │                     │  │                       │    │    │
│  │  │ - Business Logic    │  │ - Session Management │    │    │
│  │  │ - Data Transform    │  │ - Score Calculation  │    │    │
│  │  └─────────────────────┘  └──────────────────────┘    │    │
│  │                                                          │    │
│  └──────────────────────────────────────────────────────────┘    │
│                             │                                     │
│  ┌──────────────── REPOSITORY LAYER ──────────────────────┐    │
│  │                                                          │    │
│  │  ┌─────────────────────┐  ┌──────────────────────┐    │    │
│  │  │ ScenarioRepository  │  │ GameSessionRepository│    │    │
│  │  │                     │  │                       │    │    │
│  │  │ extends JPA Repo    │  │ extends JPA Repo     │    │    │
│  │  └─────────────────────┘  └──────────────────────┘    │    │
│  │                                                          │    │
│  └──────────────────────────────────────────────────────────┘    │
│                             │                                     │
└────────────────────────────┬──────────────────────────────────┘
                             │
                             │ JPA/Hibernate
                             │
┌────────────────────────────▼──────────────────────────────────┐
│                       DATA LAYER                                │
│                     (H2 In-Memory DB)                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐     │
│  │  SCENARIOS   │  │   OPTIONS    │  │  GAME_SESSIONS   │     │
│  │              │  │              │  │                   │     │
│  │ - id         │  │ - id         │  │ - id              │     │
│  │ - video_id   │  │ - scenario_id│  │ - session_id      │     │
│  │ - video_path │  │ - label      │  │ - current_score   │     │
│  │ - desc       │  │ - target_id  │  │ - current_video   │     │
│  │ - is_leaf    │  │ - score      │  │ - start_time      │     │
│  └──────────────┘  └──────────────┘  └──────────────────┘     │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘

                             │
                             │ File System Access
                             │
┌────────────────────────────▼──────────────────────────────────┐
│                       FILE SYSTEM                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  video/                                                          │
│    ├── 1.mp4                                                    │
│    ├── 1_1.mp4                                                  │
│    └── 1_2.mp4                                                  │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘
```

## Component Details

### Frontend Layer

#### 1. **index.html**
- Video player with HTML5 `<video>` element
- Overlay container for interactive options
- Score display (top-right)
- Loading indicator
- Game over screen

#### 2. **style.css**
- Full-screen video layout
- Responsive design (mobile-first)
- Animated button styles
- Position-based overlays (bottom-left, bottom-right)
- Media queries for tablets and phones

#### 3. **app.js**
- Game state management
- Video playback control
- API communication
- Event handling (video end, button clicks)
- Score animation

### Backend Layer

#### 1. **Controller Layer** (REST API)

**ScenarioController**
- Maps HTTP requests to service methods
- Handles `/api/scenarios/*` endpoints
- Returns JSON responses

**GameSessionController**
- Manages game session lifecycle
- Handles `/api/session/*` endpoints
- Records player choices

#### 2. **Service Layer** (Business Logic)

**ScenarioService**
- Retrieves scenario data
- Converts entities to DTOs
- Validates video IDs

**GameSessionService**
- Creates/manages sessions
- Updates scores
- Tracks game progress
- Generates UUIDs for sessions

#### 3. **Repository Layer** (Data Access)

**ScenarioRepository**
- JPA repository for Scenario entity
- Custom query: `findByVideoId()`

**GameSessionRepository**
- JPA repository for GameSession entity
- Custom query: `findBySessionId()`

#### 4. **Model Layer** (Domain Entities)

**Scenario**
- Represents a video scenario
- One-to-many relationship with Options
- Contains video metadata

**Option**
- Represents a decision choice
- Many-to-one relationship with Scenario
- Contains score change and position

**GameSession**
- Tracks player progress
- Stores current score and video
- Timestamps for analytics

#### 5. **DTO Layer** (Data Transfer Objects)

- `ScenarioResponse`: Scenario data for frontend
- `OptionDto`: Option data without circular refs
- `SessionResponse`: Session state for frontend
- `ChoiceRequest`: Player choice from frontend

#### 6. **Configuration Layer**

**WebConfig**
- CORS configuration
- Static resource mapping (videos, frontend)
- Resource handlers

**DataInitializer**
- Populates database on startup
- Creates initial scenarios (1, 1_1, 1_2)
- Runs as CommandLineRunner

### Data Layer

**H2 Database**
- In-memory SQL database
- Auto-creates tables from JPA entities
- Accessible via H2 Console

**Tables:**
- `SCENARIOS`: Video scenarios
- `OPTIONS`: Decision options
- `GAME_SESSIONS`: Player sessions

## Data Flow

### 1. Game Initialization
```
User → Browser → GET / → Spring Boot → index.html
                          ↓
JavaScript → POST /api/session/start → GameSessionController
                                      → GameSessionService
                                      → GameSessionRepository
                                      → Database
                          ← Session JSON ←
```

### 2. Loading Scenario
```
JavaScript → GET /api/scenarios/1 → ScenarioController
                                  → ScenarioService
                                  → ScenarioRepository
                                  → Database
           ← Scenario JSON (with options) ←
```

### 3. Video Playback
```
JavaScript → Set video.src = "/video/1.mp4"
           → Spring Boot → Static Resource Handler
                         → File System (video/1.mp4)
           ← Video Stream ←
```

### 4. Player Choice
```
User clicks option → JavaScript prepares ChoiceRequest
                   → POST /api/session/choice → GameSessionController
                                               → GameSessionService
                                               → Update score
                                               → Update current video
                                               → Save to DB
                   ← Updated Session JSON ←
                   → Load next scenario (repeat step 2)
```

## Design Patterns

### 1. **MVC (Model-View-Controller)**
- Model: JPA entities (Scenario, Option, GameSession)
- View: HTML/CSS frontend
- Controller: REST controllers

### 2. **Repository Pattern**
- Abstracts data access
- JPA repositories with custom queries

### 3. **DTO Pattern**
- Separates internal models from API responses
- Prevents circular references in JSON

### 4. **Service Layer Pattern**
- Business logic separate from controllers
- Reusable service methods

### 5. **Dependency Injection**
- Spring's `@Autowired` for loose coupling
- Easy testing and modularity

## Technology Choices

### Why Spring Boot?
- **Required**: University OOP course requirement
- **Benefits**: 
  - Auto-configuration
  - Embedded server (Tomcat)
  - Easy dependency management
  - Production-ready features

### Why H2 Database?
- In-memory for quick setup
- No external database needed
- Perfect for development/demo
- Can switch to MySQL/PostgreSQL for production

### Why Vanilla JavaScript?
- No build tools required
- Lightweight
- Easy to understand
- Direct DOM manipulation
- Native fetch API for HTTP

### Why REST API?
- Stateless communication
- Standard HTTP methods
- JSON for easy parsing
- Scalable architecture
- Can add mobile app later

## Security Considerations

### Current Implementation
- No authentication (for simplicity)
- Open CORS (development only)
- No input validation (basic)

### Production Recommendations
1. Add Spring Security
2. Implement JWT authentication
3. Validate all inputs
4. Rate limiting on API
5. HTTPS only
6. Restrict CORS to specific origins
7. Sanitize file paths
8. Add CSRF protection

## Performance Optimizations

### Current
- In-memory database (fast reads)
- Static video serving
- Minimal API calls

### Future Enhancements
1. Video preloading
2. CDN for video files
3. Response caching
4. Database connection pooling
5. Lazy loading for large decision trees
6. Video compression

## Scalability

### Horizontal Scaling
- Stateless backend (session ID in request)
- Can add load balancer
- Multiple Spring Boot instances

### Database Scaling
- Switch to PostgreSQL
- Connection pooling
- Read replicas for analytics

### Video Storage
- Move to AWS S3 or Azure Blob
- CDN distribution
- Adaptive bitrate streaming

## Testing Strategy

### Unit Tests
- Service layer logic
- Repository queries
- DTO conversions

### Integration Tests
- Controller endpoints
- Database operations
- Full API flow

### Frontend Tests
- User interactions
- Video playback
- Score updates

### End-to-End Tests
- Complete game flow
- Multiple decision paths
- Score calculation accuracy

## Deployment Options

### 1. **Local Development**
```
mvn spring-boot:run
```

### 2. **JAR Deployment**
```
mvn clean package
java -jar target/cyberwalk-1.0.0.jar
```

### 3. **Docker**
```dockerfile
FROM openjdk:17
COPY target/cyberwalk-1.0.0.jar app.jar
COPY video /video
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 4. **Cloud Platforms**
- Heroku
- AWS Elastic Beanstalk
- Azure App Service
- Google Cloud Run

## Extensibility

### Adding New Interaction Types
1. Add to `interactionType` enum
2. Update frontend JavaScript
3. Add event handlers
4. Test new interaction

### Adding Complex Decision Trees
1. Create more scenarios in DataInitializer
2. Link options to new scenarios
3. Videos follow naming convention
4. Database auto-updates

### Adding Multiplayer
1. Add User entity
2. WebSocket for real-time
3. Shared sessions
4. Leaderboard table

## Conclusion

This architecture provides:
- ✅ Clear separation of concerns
- ✅ Scalable design
- ✅ Easy to extend
- ✅ Modern web standards
- ✅ Production-ready foundation
- ✅ Educational value (OOP principles)
