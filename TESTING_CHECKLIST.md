# CyberWalk - Testing & Verification Checklist

Use this checklist to verify that everything is working correctly.

## ✅ Pre-Flight Checklist

### 1. Prerequisites Installed
- [ ] Java 17 or higher installed
  - Test: `java -version`
- [ ] Maven 3.6+ installed
  - Test: `mvn -version`
- [ ] Video files present in `video/` folder
  - [ ] `1.mp4` exists
  - [ ] `1_1.mp4` exists
  - [ ] `1_2.mp4` exists

### 2. Project Structure
- [ ] All Java files present in `src/main/java/`
- [ ] All frontend files in `src/main/resources/static/`
- [ ] `pom.xml` present in root directory
- [ ] `application.properties` present

---

## 🚀 Build & Run Checklist

### 3. Building the Project
- [ ] Navigate to project directory: `cd c:\Users\Akib\Desktop\CyberWalk`
- [ ] Run build command: `mvn clean install`
- [ ] Build completes successfully (BUILD SUCCESS message)
- [ ] No compilation errors in output
- [ ] `target/` directory created
- [ ] JAR file created: `target/cyberwalk-1.0.0.jar`

### 4. Starting the Application
- [ ] Run command: `mvn spring-boot:run`
- [ ] Application starts without errors
- [ ] See message: "Started CyberWalkApplication in X seconds"
- [ ] No stack traces in console
- [ ] Port 8080 is bound successfully

---

## 🗄️ Database Checklist

### 5. Database Initialization
- [ ] Console shows: "Database initialized with scenarios!"
- [ ] No database errors in logs

### 6. H2 Console Access
- [ ] Open: http://localhost:8080/h2-console
- [ ] H2 Console page loads
- [ ] Login with:
  - JDBC URL: `jdbc:h2:mem:cyberwalk`
  - Username: `sa`
  - Password: (empty)
- [ ] Successfully connected

### 7. Database Content Verification
Run these queries in H2 Console:

**Check Scenarios:**
```sql
SELECT * FROM SCENARIOS;
```
- [ ] Should return 3 rows (videoId: 1, 1_1, 1_2)
- [ ] Video paths are correct
- [ ] Scenario 1 has `is_leaf_node = false`
- [ ] Scenarios 1_1 and 1_2 have `is_leaf_node = true`

**Check Options:**
```sql
SELECT * FROM OPTIONS;
```
- [ ] Should return 2 rows
- [ ] Labels are "Choice A" and "Choice B"
- [ ] Target video IDs are "1_1" and "1_2"
- [ ] Score changes are 10 and -5
- [ ] Positions are "bottom-left" and "bottom-right"

**Check Sessions (initially empty):**
```sql
SELECT * FROM GAME_SESSIONS;
```
- [ ] Should return 0 rows initially

---

## 🌐 Backend API Checklist

### 8. API Endpoint Testing

**Test with Browser or PowerShell:**

**Get All Scenarios:**
- [ ] Open: http://localhost:8080/api/scenarios
- [ ] Returns JSON array with 3 scenarios
- [ ] Each scenario has videoId, videoPath, options array

**Get Specific Scenario:**
- [ ] Open: http://localhost:8080/api/scenarios/1
- [ ] Returns scenario with videoId "1"
- [ ] Has 2 options in the array
- [ ] Options have correct labels and score changes

**Start New Session:**
```powershell
Invoke-RestMethod -Method Post http://localhost:8080/api/session/start
```
- [ ] Returns sessionId (UUID format)
- [ ] currentScore is 0
- [ ] currentVideoId is "1"
- [ ] isCompleted is false

---

## 💻 Frontend Checklist

### 9. Loading the Game
- [ ] Open: http://localhost:8080
- [ ] Page loads without errors
- [ ] No 404 errors in browser console (F12)
- [ ] CSS loads correctly (styled page, not plain HTML)
- [ ] JavaScript loads (check Sources tab in DevTools)

### 10. Initial Game State
- [ ] Score display visible in top-right corner
- [ ] Score shows "0"
- [ ] Video player visible and centered
- [ ] Video controls visible (play, pause, seek bar)
- [ ] No options visible initially

### 11. Video Playback
- [ ] Video "1.mp4" loads automatically
- [ ] Video source shows: `/video/1.mp4`
- [ ] Video can be played (click play if needed)
- [ ] Video plays without errors
- [ ] Can seek forward and backward
- [ ] Volume control works
- [ ] Fullscreen button works (optional)

### 12. Decision Point
- [ ] Let video play until the end
- [ ] When video ends, two buttons appear
- [ ] "Choice A" button on bottom-left
- [ ] "Choice B" button on bottom-right
- [ ] Buttons are styled (gradient backgrounds)
- [ ] Buttons are clickable

### 13. Making a Choice (Option A - Good)
- [ ] Click "Choice A" button
- [ ] Buttons disappear
- [ ] Loading indicator briefly shows
- [ ] Score updates to "10" (was 0, +10)
- [ ] Score animates (scales up/down)
- [ ] Video changes to "1_1.mp4"
- [ ] New video plays automatically

### 14. Game Over (Path A)
- [ ] Let video "1_1" play to end
- [ ] No options appear (leaf node)
- [ ] Game Over screen appears
- [ ] Shows "Game Complete!"
- [ ] Shows "Your Final Score: 10"
- [ ] "Play Again" button visible
- [ ] Click "Play Again"
- [ ] Game restarts with video "1"
- [ ] Score resets to "0"

### 15. Alternative Path (Option B - Bad)
- [ ] Restart game if needed
- [ ] Let video "1" play to end
- [ ] Click "Choice B" button
- [ ] Score updates to "-5" (was 0, -5)
- [ ] Video changes to "1_2.mp4"
- [ ] Game Over screen appears after video ends
- [ ] Shows "Your Final Score: -5"

---

## 📱 Responsive Design Checklist

### 16. Desktop View (Full Screen)
- [ ] Open game in full-screen browser window
- [ ] Video fills most of viewport
- [ ] Score in top-right corner
- [ ] Buttons well-spaced at bottom
- [ ] All text readable

### 17. Tablet View (768px)
- [ ] Open DevTools (F12)
- [ ] Toggle device toolbar
- [ ] Select iPad or similar (768px width)
- [ ] Page adjusts to tablet size
- [ ] Buttons still visible and clickable
- [ ] Score display readable

### 18. Mobile View (480px)
- [ ] Select iPhone or similar (480px width)
- [ ] Page adjusts to mobile size
- [ ] Video maintains aspect ratio
- [ ] Buttons stack or adjust properly
- [ ] Touch targets are large enough
- [ ] Score display visible

---

## 🔧 Browser Compatibility Checklist

### 19. Test in Different Browsers
- [ ] Chrome/Edge (Chromium)
  - Video plays
  - All features work
- [ ] Firefox
  - Video plays
  - All features work
- [ ] Safari (if available)
  - Video plays
  - All features work

---

## 🐛 Error Handling Checklist

### 20. Network Errors
- [ ] Stop Spring Boot server
- [ ] Try to play game
- [ ] Appropriate error messages appear
- [ ] No silent failures

### 21. Missing Video
- [ ] Rename a video file temporarily
- [ ] Try to reach that scenario
- [ ] Check for error message
- [ ] Video error event fires
- [ ] Restore video file

### 22. Invalid Session
- [ ] Open browser console
- [ ] Manually call API with invalid session ID
- [ ] Server returns appropriate error
- [ ] Error is handled gracefully

---

## 📊 Performance Checklist

### 23. Video Loading
- [ ] Video starts playing within 2-3 seconds
- [ ] No excessive buffering
- [ ] Smooth playback
- [ ] Transitions are smooth

### 24. API Response Times
- [ ] Session start: < 200ms
- [ ] Get scenario: < 100ms
- [ ] Record choice: < 200ms
- [ ] Check browser Network tab for timing

### 25. Memory Usage
- [ ] Open Task Manager / Activity Monitor
- [ ] Check browser memory usage
- [ ] Play through game 3-4 times
- [ ] Memory doesn't grow excessively
- [ ] No memory leaks detected

---

## 🔒 Security Checklist (Basic)

### 26. CORS Configuration
- [ ] API accessible from `localhost:8080`
- [ ] CORS headers present in responses
- [ ] Check Network tab → Response Headers

### 27. Input Validation
- [ ] Try invalid videoId in API: `/api/scenarios/999`
- [ ] Should return 404 or error
- [ ] No stack trace exposed in response

---

## 📝 Code Quality Checklist

### 28. Console Logs
- [ ] No errors in browser console (F12)
- [ ] No errors in Spring Boot console
- [ ] Only informational logs appear
- [ ] No stack traces (unless expected)

### 29. Code Standards
- [ ] Java code compiles without warnings
- [ ] JavaScript has no syntax errors
- [ ] CSS validates (optional)
- [ ] Proper indentation throughout

---

## 🎯 Feature Completeness Checklist

### 30. Core Features
- [ ] ✅ Video playback
- [ ] ✅ Decision making
- [ ] ✅ Score tracking
- [ ] ✅ Game restart
- [ ] ✅ Multiple paths
- [ ] ✅ Game over screen
- [ ] ✅ Responsive design

### 31. Backend Features
- [ ] ✅ Session management
- [ ] ✅ Scenario retrieval
- [ ] ✅ Score calculation
- [ ] ✅ Database persistence
- [ ] ✅ REST API
- [ ] ✅ Static file serving

### 32. Frontend Features
- [ ] ✅ Video controls
- [ ] ✅ Dynamic button placement
- [ ] ✅ Score animation
- [ ] ✅ Loading states
- [ ] ✅ Game over screen
- [ ] ✅ Restart functionality

---

## 📚 Documentation Checklist

### 33. Documentation Complete
- [ ] README.md exists and is comprehensive
- [ ] SETUP.md has installation instructions
- [ ] API_DOCUMENTATION.md covers all endpoints
- [ ] ARCHITECTURE.md explains system design
- [ ] EXTENDING.md shows how to add features
- [ ] QUICK_REFERENCE.md provides shortcuts
- [ ] FILE_STRUCTURE.md maps project layout

### 34. Code Comments
- [ ] Complex logic is commented
- [ ] All classes have descriptions
- [ ] API endpoints are documented
- [ ] Configuration is explained

---

## 🚀 Deployment Readiness Checklist

### 35. Production Readiness (Future)
- [ ] Consider adding authentication
- [ ] Add input validation
- [ ] Implement rate limiting
- [ ] Configure CORS for specific origins
- [ ] Use production database (PostgreSQL)
- [ ] Add monitoring/logging
- [ ] Implement caching
- [ ] Add CDN for videos

---

## ✅ Final Verification

### All Systems Go!
- [ ] All critical tests pass
- [ ] No blocking issues
- [ ] Game is playable end-to-end
- [ ] Documentation is complete
- [ ] Ready for demo/submission

---

## 📋 Issue Log

If you encounter issues, document them here:

| Issue | Status | Notes |
|-------|--------|-------|
|       |        |       |
|       |        |       |
|       |        |       |

---

## 🎉 Success Criteria

Your project is ready when:
1. ✅ All checkboxes above are marked
2. ✅ You can play through the entire game
3. ✅ Score updates correctly
4. ✅ Both paths (1_1 and 1_2) work
5. ✅ Game can be restarted
6. ✅ No console errors appear
7. ✅ Mobile view works
8. ✅ Documentation is clear

**Congratulations! Your CyberWalk game is fully functional!** 🎊

---

## 🔄 Quick Re-Test Command

To quickly re-test everything:

```powershell
# Stop server (Ctrl+C)
# Clear browser cache
# Restart server
cd c:\Users\Akib\Desktop\CyberWalk
mvn spring-boot:run

# In browser:
# 1. Open http://localhost:8080
# 2. Open DevTools (F12)
# 3. Clear cache and hard reload (Ctrl+Shift+R)
# 4. Watch video 1
# 5. Click Choice A
# 6. Verify score = 10
# 7. Watch video 1_1
# 8. See Game Over
# 9. Click Play Again
# 10. Verify game restarts
```

---

**Last Updated**: December 12, 2025
**Version**: 1.0.0
**Status**: ✅ Ready for Testing
