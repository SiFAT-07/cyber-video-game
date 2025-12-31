# CyberWalk Project Summary

## 🎮 Project Overview

**CyberWalk** is an interactive story-based game where players watch full-screen videos and make decisions through clickable options that appear on-screen. Built with Spring Boot backend and vanilla HTML/CSS/JavaScript frontend, the game tracks player scores based on their choices through a decision tree structure.

## 🎯 Key Features

✅ **Full-Screen Video Experience**
- HTML5 video player with controls
- Seek forward/backward capability
- Auto-play on scenario load

✅ **Interactive Decision System**
- Options appear at video end
- Bottom-left and bottom-right positioning
- Click-based interaction (extensible to hotspots, drag, keyboard)

✅ **Dynamic Scoring**
- Real-time score updates
- Positive/negative/critical score changes
- Score displayed prominently (top-right)

✅ **Decision Tree Architecture**
- Parent-child video relationships
- Multiple decision paths
- Leaf nodes for story endings

✅ **Mobile Responsive Design**
- Adapts to all screen sizes
- Touch-friendly buttons
- Optimized video playback

✅ **RESTful Backend API**
- Session management
- Scenario retrieval
- Score tracking
- Game state persistence

## 📁 Project Structure

```
CyberWalk/
├── pom.xml                          # Maven configuration
├── README.md                        # Main documentation
├── SETUP.md                         # Installation guide
├── API_DOCUMENTATION.md             # API reference
├── ARCHITECTURE.md                  # System architecture
├── EXTENDING.md                     # Extension guide
├── run.bat                          # Windows batch launcher
├── run.ps1                          # PowerShell launcher
├── .gitignore                       # Git ignore rules
│
├── video/                           # Video files
│   ├── 1.mp4                       # Parent video
│   ├── 1_1.mp4                     # Option A video
│   └── 1_2.mp4                     # Option B video
│
└── src/
    └── main/
        ├── java/com/university/cyberwalk/
        │   ├── CyberWalkApplication.java
        │   │
        │   ├── config/
        │   │   ├── DataInitializer.java
        │   │   └── WebConfig.java
        │   │
        │   ├── controller/
        │   │   ├── GameSessionController.java
        │   │   └── ScenarioController.java
        │   │
        │   ├── dto/
        │   │   ├── ChoiceRequest.java
        │   │   ├── OptionDto.java
        │   │   ├── ScenarioResponse.java
        │   │   └── SessionResponse.java
        │   │
        │   ├── model/
        │   │   ├── GameSession.java
        │   │   ├── Option.java
        │   │   └── Scenario.java
        │   │
        │   ├── repository/
        │   │   ├── GameSessionRepository.java
        │   │   └── ScenarioRepository.java
        │   │
        │   └── service/
        │       ├── GameSessionService.java
        │       └── ScenarioService.java
        │
        └── resources/
            ├── application.properties
            └── static/
                ├── index.html
                ├── css/
                │   └── style.css
                └── js/
                    └── app.js
```

## 🛠️ Technology Stack

### Backend
- **Spring Boot 3.2.0** - Application framework
- **Spring Data JPA** - Data persistence
- **H2 Database** - In-memory database
- **Maven** - Dependency management
- **Java 17** - Programming language

### Frontend
- **HTML5** - Structure
- **CSS3** - Styling and animations
- **JavaScript (ES6)** - Game logic
- **Fetch API** - HTTP communication

## 🏗️ Architecture Highlights

### 3-Tier Architecture
1. **Presentation Layer** - HTML/CSS/JS frontend
2. **Business Logic Layer** - Spring Boot services
3. **Data Access Layer** - JPA repositories

### Design Patterns
- **MVC Pattern** - Model-View-Controller separation
- **Repository Pattern** - Data access abstraction
- **DTO Pattern** - API response objects
- **Service Layer Pattern** - Business logic encapsulation
- **Dependency Injection** - Spring's IoC container

### Key Components

#### Backend
- **Controllers**: Handle HTTP requests, return JSON
- **Services**: Business logic, data transformation
- **Repositories**: Database operations
- **Models**: JPA entities (Scenario, Option, GameSession)
- **DTOs**: API request/response objects

#### Frontend
- **Video Player**: HTML5 with custom controls
- **Overlay System**: Dynamic option rendering
- **State Management**: Session and score tracking
- **API Client**: Fetch-based communication

## 🎬 Game Flow

1. **Initialization**
   - User opens game → Frontend loads
   - JavaScript requests new session → Backend creates session
   - Video "1" starts playing

2. **Watching**
   - Full-screen video plays
   - User can seek forward/backward
   - Score displayed at top-right

3. **Decision Point**
   - Video ends → Options appear
   - "Choice A" (bottom-left) vs "Choice B" (bottom-right)
   - User clicks an option

4. **Choice Processing**
   - JavaScript sends choice to backend
   - Backend updates score and current video
   - New video loads and plays

5. **Conclusion**
   - Leaf node video plays (no more options)
   - Game Over screen appears
   - Final score displayed
   - Option to restart

## 📊 Database Schema

### SCENARIOS Table
- `id` - Primary key
- `video_id` - Unique identifier (e.g., "1", "1_1")
- `video_path` - File path
- `description` - Scenario text
- `is_leaf_node` - End of path flag

### OPTIONS Table
- `id` - Primary key
- `scenario_id` - Foreign key to SCENARIOS
- `label` - Button text
- `target_video_id` - Next video
- `score_change` - Points (+/-)
- `position` - UI placement
- `interaction_type` - "click", "hotspot", etc.

### GAME_SESSIONS Table
- `id` - Primary key
- `session_id` - UUID
- `current_score` - Player score
- `current_video_id` - Current video
- `start_time` - Session start
- `last_updated` - Last action
- `is_completed` - Finished flag

## 🚀 API Endpoints

### Scenarios
- `GET /api/scenarios/{videoId}` - Get scenario details
- `GET /api/scenarios` - List all scenarios

### Sessions
- `POST /api/session/start` - Create new session
- `GET /api/session/{sessionId}` - Get session state
- `POST /api/session/choice` - Record player choice
- `POST /api/session/complete/{sessionId}` - Mark complete

### Static Resources
- `GET /video/{filename}` - Stream video files
- `GET /` - Serve frontend HTML

## 📹 Video Naming Convention

**Rule**: Parent video uses single integer, child videos use `parent_option` format

**Examples**:
- Main video: `1.mp4`
- Option 1: `1_1.mp4`
- Option 2: `1_2.mp4`
- Next level: `1_1_1.mp4`, `1_1_2.mp4`

## 🎨 UI Features

### Score Display
- Fixed position (top-right)
- Real-time updates
- Animated scale effect on change
- Glowing border effect

### Video Player
- Full viewport coverage
- Object-fit: contain (maintains aspect ratio)
- Native controls (play, pause, seek, volume)
- Mobile-optimized

### Option Buttons
- Gradient backgrounds
- Hover animations
- Position-based placement
- Color-coded by score impact:
  - Green gradient = positive score
  - Red gradient = negative score
  - Purple gradient = neutral

### Loading States
- Spinner animation during transitions
- Prevents user interaction during load
- Smooth fade in/out

### Game Over Screen
- Full-screen overlay
- Final score highlight
- Restart button
- Animated appearance

## 🔧 Configuration

### Server
- Port: 8080 (configurable)
- CORS: Enabled for all origins
- Max file size: 500MB

### Database
- Type: H2 in-memory
- Console: http://localhost:8080/h2-console
- Auto-initialization on startup

### Video Serving
- Static resource handler
- Direct file system access
- No transcoding

## 🧪 Testing Scenarios

### Initial Setup (Included)
```
1 (Start)
├── 1_1 (Good choice, +10 points)
└── 1_2 (Bad choice, -5 points)
```

Both paths end (leaf nodes), showing Game Over.

## 📱 Responsive Breakpoints

- **Desktop**: Full features, large buttons
- **Tablet** (≤768px): Adjusted sizing, stacked layout
- **Mobile** (≤480px): Compact UI, touch-optimized

## 🔒 Security Considerations

### Current (Development)
- ✅ No authentication required
- ✅ Open CORS policy
- ✅ No input validation

### Recommendations (Production)
- 🔐 Add Spring Security
- 🔐 Implement JWT authentication
- 🔐 Validate all inputs
- 🔐 Enable HTTPS
- 🔐 Restrict CORS
- 🔐 Rate limiting

## 📈 Future Enhancements

### Interactions
- ✨ Hotspot clicking (click on video areas)
- ✨ Drag-and-drop interactions
- ✨ Keyboard input
- ✨ Timed decisions

### Features
- 🎯 User accounts and login
- 🏆 Achievements system
- 📊 Leaderboard
- 💾 Save/load progress
- 🎵 Sound effects and music
- 📝 Subtitles/captions
- 🎨 Custom themes

### Technical
- 🚀 Video preloading
- 📦 CDN integration
- 🗄️ PostgreSQL database
- 🔄 Real-time multiplayer
- 📱 Native mobile apps
- 🤖 Admin panel
- 📊 Analytics dashboard

## 📚 Documentation Files

1. **README.md** - Project overview, quick start
2. **SETUP.md** - Detailed installation instructions
3. **API_DOCUMENTATION.md** - Complete API reference
4. **ARCHITECTURE.md** - System design and patterns
5. **EXTENDING.md** - How to add features

## 🎓 Educational Value (OOP Concepts)

### Demonstrated Principles
- ✅ **Encapsulation** - Private fields, public methods
- ✅ **Inheritance** - JpaRepository extension
- ✅ **Abstraction** - Service layer interfaces
- ✅ **Polymorphism** - Repository pattern
- ✅ **Composition** - Scenario has Options
- ✅ **Dependency Injection** - Spring autowiring
- ✅ **Separation of Concerns** - MVC pattern
- ✅ **Single Responsibility** - Each class has one job

### Advanced Concepts
- RESTful API design
- Database relationships (One-to-Many)
- DTO pattern for data transfer
- Service-oriented architecture
- Event-driven frontend (video events)
- State management
- Responsive design

## 🏁 Getting Started

### Quick Start (3 Steps)

1. **Install Prerequisites**
   ```
   - Java 17+
   - Maven 3.6+
   ```

2. **Run the Application**
   ```powershell
   cd c:\Users\Akib\Desktop\CyberWalk
   mvn spring-boot:run
   ```

3. **Play the Game**
   ```
   Open: http://localhost:8080
   ```

### Alternative: Use Launcher Script
```powershell
.\run.ps1
```
or
```cmd
run.bat
```

## 🐛 Troubleshooting

### Maven Not Found
Install Maven and add to PATH, restart terminal

### Port Already in Use
Change port in `application.properties`

### Videos Not Playing
- Check files exist in `video/` folder
- Verify MP4 format with H.264 codec
- Check browser console for errors

### Database Issues
- Access H2 Console: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:cyberwalk`
- Username: `sa`, Password: (empty)

## 🤝 Contributing

To extend the game:
1. Read `EXTENDING.md`
2. Add video files to `video/` folder
3. Update `DataInitializer.java`
4. Test via H2 Console
5. Verify in browser

## 📝 License

Educational project for university coursework.

## 👨‍💻 Author

Built for Advanced Object-Oriented Programming course.

---

## 📞 Support Resources

- **Architecture**: See `ARCHITECTURE.md`
- **API Reference**: See `API_DOCUMENTATION.md`
- **Setup Help**: See `SETUP.md`
- **Extensions**: See `EXTENDING.md`
- **H2 Console**: http://localhost:8080/h2-console
- **Browser DevTools**: F12 for debugging

---

**Status**: ✅ Ready for development and extension
**Version**: 1.0.0
**Last Updated**: December 12, 2025
