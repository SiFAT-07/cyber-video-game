# Quick Setup Guide

## Prerequisites Installation

### 1. Install Java 17
Download and install from: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html

Verify installation:
```powershell
java -version
```

### 2. Install Maven
Download from: https://maven.apache.org/download.cgi

**Windows Setup:**
1. Download `apache-maven-3.9.x-bin.zip`
2. Extract to `C:\Program Files\Apache\maven`
3. Add to PATH:
   - System Properties > Environment Variables
   - Add to Path: `C:\Program Files\Apache\maven\bin`
4. Verify:
   ```powershell
   mvn -version
   ```

## Running the Application

### Option 1: Using Maven (Recommended)
```powershell
cd c:\Users\Akib\Desktop\CyberWalk
mvn clean install
mvn spring-boot:run
```

### Option 2: Using IDE (IntelliJ IDEA / Eclipse)
1. Import project as Maven project
2. Right-click on `CyberWalkApplication.java`
3. Select "Run" or "Debug"

### Option 3: Using JAR file
```powershell
cd c:\Users\Akib\Desktop\CyberWalk
mvn clean package
java -jar target/cyberwalk-1.0.0.jar
```

## Access the Application

Once running, open browser:
- **Game**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console

## Initial Test

The application comes pre-configured with:
- Video 1: Main scenario with 2 options
- Video 1_1: Option A (Good choice, +10 points)
- Video 1_2: Option B (Bad choice, -5 points)

Both 1_1 and 1_2 are leaf nodes (end of story).

## Quick Testing Steps

1. Start the application
2. Open http://localhost:8080
3. Video "1.mp4" should start playing automatically
4. Let the video play until the end
5. Two buttons should appear:
   - "Choice A" (bottom-left, green)
   - "Choice B" (bottom-right, red)
6. Click either button
7. Score updates (top-right corner)
8. Next video plays based on your choice
9. Game Over screen shows final score

## Troubleshooting

### Maven not found
- Install Maven and add to PATH
- Restart PowerShell/Terminal after installation

### Java not found
- Install JDK 17 or higher
- Set JAVA_HOME environment variable

### Port 8080 already in use
Edit `src/main/resources/application.properties`:
```properties
server.port=8081
```
Then access: http://localhost:8081

### Videos not loading
- Ensure video files are in the `video/` folder
- Check file names match: `1.mp4`, `1_1.mp4`, `1_2.mp4`
- Verify video format is MP4 (H.264 codec recommended)

## Project Structure Verification

Check if all files are in place:
```
CyberWalk/
├── pom.xml ✓
├── video/
│   ├── 1.mp4 ✓
│   ├── 1_1.mp4 ✓
│   └── 1_2.mp4 ✓
└── src/
    └── main/
        ├── java/ ✓
        └── resources/
            ├── static/
            │   ├── index.html ✓
            │   ├── css/style.css ✓
            │   └── js/app.js ✓
            └── application.properties ✓
```

## Alternative: Using VS Code

If you have VS Code with Java extensions:

1. Install extensions:
   - Extension Pack for Java
   - Spring Boot Extension Pack

2. Open project folder in VS Code

3. Press F5 to run the application

## Database Check

To verify scenarios are loaded:

1. Access H2 Console: http://localhost:8080/h2-console
2. Use these credentials:
   - JDBC URL: `jdbc:h2:mem:cyberwalk`
   - User Name: `sa`
   - Password: (leave empty)
3. Run query:
   ```sql
   SELECT * FROM SCENARIOS;
   SELECT * FROM OPTIONS;
   ```

You should see 3 scenarios (1, 1_1, 1_2) and 2 options.
