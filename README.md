# CyberWalk - Interactive Story-Based Game

An interactive video-based decision game built with Spring Boot backend and vanilla HTML/CSS/JavaScript frontend. Players watch full-screen videos and make choices that affect their score and story progression.

## Features

- **Full-screen Video Playback**: Videos play in full-screen with interactive overlays
- **Decision Tree Structure**: Each scenario presents options that lead to different paths
- **Dynamic Scoring System**: Choices affect the player's score (positive/negative/critical)
- **Persistent Data Storage**: All user accounts and level editor data are saved permanently
- **Mobile Responsive**: Works on desktop, tablet, and mobile devices
- **REST API Backend**: Spring Boot handles game logic, scoring, and session management
- **Video Controls**: Seek forward/backward through videos

## Technology Stack

### Backend
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (file-based for data persistence)
- **Maven**

### Frontend
- **HTML5**
- **CSS3** (with responsive design)
- **Vanilla JavaScript**
- **HTML5 Video API**

## Project Structure

```
CyberWalk/
├── src/
│   └── main/
│       ├── java/com/university/cyberwalk/
│       │   ├── CyberWalkApplication.java
│       │   ├── config/
│       │   │   ├── DataInitializer.java
│       │   │   └── WebConfig.java
│       │   ├── controller/
│       │   │   ├── GameSessionController.java
│       │   │   └── ScenarioController.java
│       │   ├── dto/
│       │   │   ├── ChoiceRequest.java
│       │   │   ├── OptionDto.java
│       │   │   ├── ScenarioResponse.java
│       │   │   └── SessionResponse.java
│       │   ├── model/
│       │   │   ├── GameSession.java
│       │   │   ├── Option.java
│       │   │   └── Scenario.java
│       │   ├── repository/
│       │   │   ├── GameSessionRepository.java
│       │   │   └── ScenarioRepository.java
│       │   └── service/
│       │       ├── GameSessionService.java
│       │       └── ScenarioService.java
│       └── resources/
│           ├── static/
│           │   ├── index.html
│           │   ├── css/style.css
│           │   └── js/app.js
│           └── application.properties
├── video/
│   ├── 1.mp4
│   ├── 1_1.mp4
│   └── 1_2.mp4
└── pom.xml
```

## Video Naming Convention

- **Parent videos**: Single integer (e.g., `1.mp4`)
- **Option videos**: Parent integer + underscore + option number (e.g., `1_1.mp4`, `1_2.mp4`)

## API Endpoints

### Scenario Endpoints
- `GET /api/scenarios/{videoId}` - Get scenario details by video ID
- `GET /api/scenarios` - Get all scenarios

### Session Endpoints
- `POST /api/session/start` - Start a new game session
- `GET /api/session/{sessionId}` - Get session details
- `POST /api/session/choice` - Record a player's choice
- `POST /api/session/complete/{sessionId}` - Mark session as complete

## How to Run

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Steps

1. **Clone or navigate to the project directory**
   ```powershell
   cd c:\Users\Akib\Desktop\CyberWalk
   ```

2. **Build the project**
   ```powershell
   mvn clean install
   ```

3. **Run the application**
   ```powershell
   mvn spring-boot:run
   ```

4. **Access the game**
   - Open browser: `http://localhost:8080`
   - H2 Console (for debugging): `http://localhost:8080/h2-console`

## Game Flow

1. **Start**: Game begins with video "1"
2. **Watch**: Player watches the video with controls
3. **Choose**: When video ends, options appear (bottom-left and bottom-right)
4. **Score**: Choice updates the score (displayed top-right)
5. **Continue**: Selected option's video plays next
6. **End**: Leaf node videos show "Game Over" screen with final score

## Adding New Scenarios

### Method 1: Database Initialization (DataInitializer.java)
Add scenarios in `DataInitializer.java`:

```java
Scenario newScenario = new Scenario();
newScenario.setVideoId("2");
newScenario.setVideoPath("/video/2.mp4");
newScenario.setDescription("Your description");
newScenario.setLeafNode(false);

Option option1 = new Option();
option1.setLabel("Option A");
option1.setTargetVideoId("2_1");
option1.setScoreChange(15);
option1.setPosition("bottom-left");
option1.setInteractionType("click");
option1.setScenario(newScenario);

newScenario.setOptions(Arrays.asList(option1));
scenarioRepository.save(newScenario);
```

### Method 2: REST API (Future Enhancement)
Create POST endpoints to add scenarios dynamically.

## Configuration

### Database
- **Type**: H2 (in-memory)
- **Console**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:cyberwalk`
- **Username**: `sa`
- **Password**: (empty)

### Server
- **Port**: 8080 (configurable in `application.properties`)
- **CORS**: Enabled for all origins (adjust for production)

## Mobile Optimization

- Responsive design with breakpoints at 768px and 480px
- Touch-friendly button sizes
- Optimized video controls for mobile
- Fullscreen video experience

## Future Enhancements

- Keyboard interaction support
- Hotspot interactions (clickable areas on video)
- Drag-and-drop interactions
- Multiple decision paths with complex trees
- Leaderboard system
- Save/Load game functionality
- Video preloading for smoother transitions
- Admin panel for scenario management

## Troubleshooting

### Video not playing
- Check video files exist in `video/` folder
- Ensure video paths are correct in database
- Check browser console for errors
- Try different video format (MP4 recommended)

### API errors
- Verify Spring Boot is running on port 8080
- Check H2 database console for data
- Review application logs for errors

### Score not updating
- Check browser console for API errors
- Verify session ID is being passed correctly
- Check network tab for failed requests

## License

This project is for educational purposes (University OOP Course).

## Contributors

Built for Advanced Object-Oriented Programming course project.
