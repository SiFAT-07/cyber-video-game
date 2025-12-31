# CyberWalk Quick Reference

## 🚀 Quick Start Commands

### Start the Application
```powershell
cd c:\Users\Akib\Desktop\CyberWalk
mvn spring-boot:run
```

### Access Points
- **Game**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console
- **API Base**: http://localhost:8080/api

---

## 📁 File Locations

### Backend Code
```
src/main/java/com/university/cyberwalk/
├── model/          → Database entities
├── repository/     → Database access
├── service/        → Business logic
├── controller/     → REST APIs
├── dto/            → API objects
└── config/         → Configuration
```

### Frontend Code
```
src/main/resources/static/
├── index.html      → Main page
├── css/style.css   → Styling
└── js/app.js       → Game logic
```

### Videos
```
video/
├── 1.mp4           → Parent video
├── 1_1.mp4         → Option 1
└── 1_2.mp4         → Option 2
```

---

## 🔧 Common Tasks

### Add New Scenario
1. Add video files to `video/` folder
2. Edit `DataInitializer.java`
3. Add scenario and options
4. Restart application

### Change Port
Edit `application.properties`:
```properties
server.port=8081
```

### View Database
1. Go to http://localhost:8080/h2-console
2. JDBC URL: `jdbc:h2:mem:cyberwalk`
3. Username: `sa`
4. Password: (empty)
5. Click Connect

### Check Scenarios
```sql
SELECT * FROM SCENARIOS;
SELECT * FROM OPTIONS;
SELECT * FROM GAME_SESSIONS;
```

---

## 🌐 API Quick Reference

### Start New Game
```http
POST /api/session/start
```

### Get Scenario
```http
GET /api/scenarios/{videoId}
```

### Record Choice
```http
POST /api/session/choice
Content-Type: application/json

{
  "sessionId": "uuid-here",
  "optionId": 1,
  "targetVideoId": "1_1",
  "scoreChange": 10
}
```

---

## 🎮 Game Flow

```
1. Load → Create Session
2. Play Video → Watch
3. Video Ends → Show Options
4. Click Option → Update Score
5. Load Next Video → Repeat
6. Leaf Node → Game Over
```

---

## 📹 Video Naming Rules

| Type | Example | Description |
|------|---------|-------------|
| Parent | `1.mp4` | Main scenario |
| Child | `1_1.mp4` | Option 1 |
| Child | `1_2.mp4` | Option 2 |
| Nested | `1_1_1.mp4` | Sub-option |

---

## 💻 Code Snippets

### Add Scenario (Java)
```java
Scenario scenario = new Scenario();
scenario.setVideoId("2");
scenario.setVideoPath("/video/2.mp4");
scenario.setDescription("Description");
scenario.setLeafNode(false);

Option option = new Option();
option.setLabel("Choice");
option.setTargetVideoId("2_1");
option.setScoreChange(10);
option.setPosition("bottom-left");
option.setInteractionType("click");
option.setScenario(scenario);

scenario.setOptions(Arrays.asList(option));
scenarioRepository.save(scenario);
```

### API Call (JavaScript)
```javascript
const response = await fetch('http://localhost:8080/api/scenarios/1');
const scenario = await response.json();
console.log(scenario);
```

---

## 🎨 Button Positions

| Position | CSS Class | Location |
|----------|-----------|----------|
| `bottom-left` | `.left` | Bottom left corner |
| `bottom-right` | `.right` | Bottom right corner |

---

## 📊 Score Guidelines

| Value | Meaning |
|-------|---------|
| +50 | Excellent choice |
| +10 to +25 | Good choice |
| 0 | Neutral |
| -5 to -15 | Bad choice |
| -30+ | Critical mistake |

---

## 🐛 Debug Checklist

### Video Won't Play
- [ ] File exists in `video/` folder?
- [ ] File name matches database?
- [ ] MP4 format with H.264 codec?
- [ ] Check browser console (F12)?

### Options Not Showing
- [ ] Is scenario a leaf node?
- [ ] Are options in database?
- [ ] Did video end?
- [ ] Check JavaScript console?

### Score Not Updating
- [ ] Is session ID valid?
- [ ] Check API response?
- [ ] Verify scoreChange value?
- [ ] Backend logs show update?

### API Not Working
- [ ] Is Spring Boot running?
- [ ] Port 8080 available?
- [ ] CORS enabled?
- [ ] Check H2 console?

---

## 🔍 Testing Commands

### Test API with cURL
```bash
# Get scenario
curl http://localhost:8080/api/scenarios/1

# Start session
curl -X POST http://localhost:8080/api/session/start
```

### Test API with PowerShell
```powershell
# Get scenario
Invoke-RestMethod http://localhost:8080/api/scenarios/1

# Start session
Invoke-RestMethod -Method Post http://localhost:8080/api/session/start
```

---

## 📱 Responsive Breakpoints

| Device | Max Width | Changes |
|--------|-----------|---------|
| Desktop | - | Full layout |
| Tablet | 768px | Adjusted sizes |
| Mobile | 480px | Stacked buttons |

---

## 🛠️ Dependencies

### Backend
- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database
- Lombok

### Frontend
- HTML5
- CSS3
- Vanilla JavaScript
- Fetch API

---

## 📞 Help & Resources

| Resource | Location |
|----------|----------|
| Full Docs | `README.md` |
| Setup Guide | `SETUP.md` |
| API Docs | `API_DOCUMENTATION.md` |
| Architecture | `ARCHITECTURE.md` |
| Extensions | `EXTENDING.md` |
| Summary | `PROJECT_SUMMARY.md` |

---

## ⚡ Keyboard Shortcuts (Browser)

| Key | Action |
|-----|--------|
| F12 | Open DevTools |
| F5 | Refresh page |
| Ctrl+Shift+I | Inspect element |
| Ctrl+Shift+J | Open console |

---

## 🎯 Next Steps

1. ✅ Install Java 17 and Maven
2. ✅ Run `mvn spring-boot:run`
3. ✅ Open http://localhost:8080
4. ✅ Play through video 1
5. ✅ Choose an option
6. ✅ See score update
7. ✅ Add your own scenarios!

---

## 💡 Tips

- **Development**: Use `mvn spring-boot:run` for auto-reload
- **Production**: Build JAR with `mvn package`
- **Debugging**: Check both browser console and Spring Boot logs
- **Database**: Use H2 console to verify data
- **Videos**: Keep file sizes reasonable (<50MB per video)
- **Testing**: Test on multiple browsers and devices

---

## 🔑 Key Files to Modify

| File | Purpose |
|------|---------|
| `DataInitializer.java` | Add scenarios |
| `app.js` | Frontend logic |
| `style.css` | UI styling |
| `application.properties` | Configuration |

---

**Keep this file handy for quick reference!** 📌
