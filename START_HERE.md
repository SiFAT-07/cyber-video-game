# 🎮 CyberWalk - Start Here!

Welcome to **CyberWalk**, your interactive story-based game project!

## � Important: Data Persistence Enabled!

Your application now uses a **persistent file-based database**:
- ✅ **User accounts** are saved permanently
- ✅ **Level editor data** persists across restarts
- ✅ Data survives browser refresh, app restart, and works across different browsers
- 📂 Database stored in: `./data/cyberwalk.*`

**📖 For full details**, see: [`DATABASE_SETUP.md`](DATABASE_SETUP.md)

---

## �🚀 Quickest Way to Get Started

### Option 1: Express Start (3 Commands)
```powershell
cd c:\Users\Akib\Desktop\CyberWalk
mvn spring-boot:run
# Open browser: http://localhost:8080
```

### Option 2: Use Launcher
```powershell
.\run.ps1  # or run.bat
```

---

## 📚 Documentation Index

Choose your path based on what you need:

### 🆕 First Time Here?
→ **Start with**: [`PROJECT_COMPLETE.md`](PROJECT_COMPLETE.md)
- Complete overview of everything
- What was built
- How it works
- Success criteria

### 🔧 Want to Install & Run?
→ **Go to**: [`SETUP.md`](SETUP.md)
- Detailed installation instructions
- Prerequisites (Java, Maven)
- Troubleshooting common issues

### 📖 Need Project Overview?
→ **Read**: [`README.md`](README.md)
- Main documentation
- Feature list
- Quick start guide
- Project structure

### 🌐 Building API Integrations?
→ **See**: [`API_DOCUMENTATION.md`](API_DOCUMENTATION.md)
- All API endpoints
- Request/response examples
- Error handling
- cURL examples

### 🏗️ Understanding the Architecture?
→ **Study**: [`ARCHITECTURE.md`](ARCHITECTURE.md)
- System design diagrams
- Component interactions
- Technology choices
- Design patterns used

### ➕ Adding New Scenarios?
→ **Follow**: [`EXTENDING.md`](EXTENDING.md)
- Step-by-step extension guide
- Adding scenarios
- Custom interactions
- Advanced features

### ⚡ Need Quick Commands?
→ **Use**: [`QUICK_REFERENCE.md`](QUICK_REFERENCE.md)
- Common commands
- File locations
- Code snippets
- Debug checklist

### 🗂️ Looking for Specific Files?
→ **Check**: [`FILE_STRUCTURE.md`](FILE_STRUCTURE.md)
- Complete project tree
- File descriptions
- Component breakdown
- Dependency map

### 🧪 Ready to Test?
→ **Follow**: [`TESTING_CHECKLIST.md`](TESTING_CHECKLIST.md)
- Comprehensive test checklist
- Verification steps
- Browser testing
- Performance checks

### 📊 Want a Summary?
→ **See**: [`PROJECT_SUMMARY.md`](PROJECT_SUMMARY.md)
- High-level overview
- Key features
- Technical specs
- Statistics

---

## 🎯 Choose Your Journey

### I want to...

#### 🎮 Play the Game
1. Ensure Java 17+ and Maven installed
2. Run: `mvn spring-boot:run`
3. Open: http://localhost:8080
4. Watch video, make choices, track score!

#### 🔍 Explore the Code
```
src/main/java/com/university/cyberwalk/
├── model/          → Database entities
├── repository/     → Data access
├── service/        → Business logic
├── controller/     → REST APIs
└── config/         → Configuration

src/main/resources/static/
├── index.html      → Game UI
├── css/style.css   → Styling
└── js/app.js       → Game logic
```

#### 📝 Add My Own Videos
1. Add videos to `video/` folder (e.g., `2.mp4`, `2_1.mp4`)
2. Edit `src/main/java/com/university/cyberwalk/config/DataInitializer.java`
3. Add scenario code (see `EXTENDING.md`)
4. Restart server

#### 🐛 Debug Issues
1. Open browser DevTools (F12)
2. Check Console for JavaScript errors
3. Check Network tab for API calls
4. Review Spring Boot logs in terminal
5. Access H2 Console: http://localhost:8080/h2-console

#### 🚀 Deploy to Production
1. Build JAR: `mvn clean package`
2. Run JAR: `java -jar target/cyberwalk-1.0.0.jar`
3. Or deploy to cloud platform (Heroku, AWS, Azure)

---

## 📁 Project Contents

```
CyberWalk/
│
├── 📚 Documentation (10 files)
│   ├── START_HERE.md                 ← You are here!
│   ├── PROJECT_COMPLETE.md           ← Complete project report
│   ├── README.md                     ← Main documentation
│   ├── SETUP.md                      ← Installation guide
│   ├── API_DOCUMENTATION.md          ← API reference
│   ├── ARCHITECTURE.md               ← System design
│   ├── EXTENDING.md                  ← Extension guide
│   ├── QUICK_REFERENCE.md            ← Quick commands
│   ├── FILE_STRUCTURE.md             ← Project layout
│   ├── PROJECT_SUMMARY.md            ← Overview
│   └── TESTING_CHECKLIST.md          ← Testing guide
│
├── 🚀 Quick Launch
│   ├── run.ps1                       ← PowerShell launcher
│   └── run.bat                       ← Batch launcher
│
├── ⚙️ Configuration
│   ├── pom.xml                       ← Maven dependencies
│   └── .gitignore                    ← Git ignore rules
│
├── 🎬 Game Assets
│   └── video/
│       ├── 1.mp4                     ← Main scenario
│       ├── 1_1.mp4                   ← Choice A
│       └── 1_2.mp4                   ← Choice B
│
└── 💻 Source Code
    └── src/
        ├── Backend (14 Java files)
        └── Frontend (3 web files)
```

---

## 🎯 Quick Tasks

### ✅ Verify Everything Works
```powershell
# 1. Check prerequisites
java -version    # Should be 17+
mvn -version     # Should be 3.6+

# 2. Build project
mvn clean install

# 3. Run application
mvn spring-boot:run

# 4. Test in browser
# Open: http://localhost:8080
```

### ✅ Test the Game Flow
1. ⏯️ Watch video 1 play
2. ⏸️ Use video controls (seek, pause)
3. ⏭️ Wait for video to end
4. 🖱️ Click "Choice A" or "Choice B"
5. 📊 See score update
6. 🎥 Watch next video
7. 🏁 See "Game Over" screen
8. 🔄 Click "Play Again"

### ✅ Verify API Works
```powershell
# Start session
Invoke-RestMethod -Method Post http://localhost:8080/api/session/start

# Get scenario
Invoke-RestMethod http://localhost:8080/api/scenarios/1
```

### ✅ Check Database
1. Open: http://localhost:8080/h2-console
2. Connect with:
   - JDBC URL: `jdbc:h2:mem:cyberwalk`
   - Username: `sa`
   - Password: (empty)
3. Run: `SELECT * FROM SCENARIOS;`

---

## 🆘 Common Issues & Solutions

### Maven Not Found
**Solution**: Install Maven and add to PATH (see `SETUP.md`)

### Port 8080 Already in Use
**Solution**: Change port in `application.properties`:
```properties
server.port=8081
```

### Videos Not Playing
**Solution**: 
- Verify files exist in `video/` folder
- Check file names: `1.mp4`, `1_1.mp4`, `1_2.mp4`
- Use MP4 format with H.264 codec

### API Returns 404
**Solution**: 
- Ensure Spring Boot is running
- Check correct URL: `http://localhost:8080/api/...`
- Verify endpoint in `API_DOCUMENTATION.md`

---

## 💡 Pro Tips

1. **Development**: Keep `mvn spring-boot:run` running, it auto-reloads
2. **Debugging**: Use browser DevTools (F12) extensively
3. **Database**: Check H2 console to verify data
4. **Testing**: Test on multiple browsers and devices
5. **Videos**: Keep video files under 50MB for better performance

---

## 🎓 Learning Path

### Day 1: Setup & Run
1. Read `PROJECT_COMPLETE.md`
2. Follow `SETUP.md` to install prerequisites
3. Run the application
4. Play through the game

### Day 2: Understand Architecture
1. Study `ARCHITECTURE.md`
2. Review Java code in `src/main/java/`
3. Examine frontend code in `src/main/resources/static/`
4. Check database with H2 console

### Day 3: Explore API
1. Read `API_DOCUMENTATION.md`
2. Test endpoints with PowerShell/cURL
3. Inspect Network tab in browser
4. Try creating your own API calls

### Day 4: Extend the Game
1. Follow `EXTENDING.md`
2. Add new video files
3. Modify `DataInitializer.java`
4. Create new scenarios

### Day 5: Polish & Test
1. Use `TESTING_CHECKLIST.md`
2. Test on mobile devices
3. Optimize performance
4. Prepare for demo

---

## 📞 Help & Support

### Resources by Task

| Task | Resource | Location |
|------|----------|----------|
| Install & Run | Setup Guide | `SETUP.md` |
| Play Game | Just run it! | http://localhost:8080 |
| Understand Code | Architecture | `ARCHITECTURE.md` |
| API Details | API Docs | `API_DOCUMENTATION.md` |
| Add Scenarios | Extension Guide | `EXTENDING.md` |
| Quick Commands | Quick Ref | `QUICK_REFERENCE.md` |
| Find Files | File Structure | `FILE_STRUCTURE.md` |
| Test | Test Checklist | `TESTING_CHECKLIST.md` |
| Overview | Summary | `PROJECT_SUMMARY.md` |
| Everything | Complete Report | `PROJECT_COMPLETE.md` |

---

## 🎊 You're All Set!

Everything you need is here. Pick your starting point above and dive in!

### Recommended First Steps:
1. ✅ Read `PROJECT_COMPLETE.md` (5 min overview)
2. ✅ Follow `SETUP.md` to install and run (10 min)
3. ✅ Play the game (2 min)
4. ✅ Explore the code (as needed)
5. ✅ Extend with your scenarios (when ready)

### Quick Start Right Now:
```powershell
cd c:\Users\Akib\Desktop\CyberWalk
mvn spring-boot:run
# Then open: http://localhost:8080
```

---

## 🌟 Project Status

✅ **Backend**: Complete & Functional  
✅ **Frontend**: Complete & Responsive  
✅ **Database**: Configured & Seeded  
✅ **API**: Documented & Tested  
✅ **Documentation**: Comprehensive  
✅ **Ready**: For Demo & Submission  

---

**Happy Gaming! 🎮**

*For questions, consult the documentation files above or check the code comments.*

---

**Last Updated**: December 12, 2025  
**Version**: 1.0.0  
**Status**: ✅ Production Ready
