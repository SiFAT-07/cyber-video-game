# 🚀 Quick Start Guide - New Game Levels

## To Initialize the New Levels

### Option 1: Fresh Start (Recommended)
```bash
# Delete the existing database to trigger reinitialization
# The database file is typically in the project root or data folder
# Look for: cyber-video-game.mv.db or similar H2 database files

# Then run the application
./run.bat
# or
./run.ps1
# or
mvn spring-boot:run
```

### Option 2: Manual Database Reset
If the application is already running:
1. Stop the application
2. Delete database files (usually `*.mv.db` in project root)
3. Restart the application
4. DataInitializer will automatically create all 5 levels

---

## 📊 What You'll Get

### Levels Created Automatically
1. **University Hostel Life** (EASY) - 5 attacks max
2. **Campus Wide Cyber Attack** (MEDIUM) - 6 attacks max
3. **Exam Season Exploitation** (MEDIUM) - 5 attacks max
4. **Remote Internship Dangers** (HARD) - 6 attacks max
5. **E-Commerce Fraud Season** (HARD) - 7 attacks max

### Defender Profiles
- **Sifat** - CS student with your requested vulnerabilities
- **Riya** - Business student
- **Dr. Rahman** - Senior professor
- **Nadia** - Lab assistant (expert)
- **Tina** - Stressed engineering student
- **Arif** - Job hunting graduate
- **Maya** - Shopping enthusiast

---

## 🎮 Testing the Levels

### Using Level Editor
1. Navigate to: `http://localhost:8080/editor.html`
2. See all 5 levels in the sidebar
3. Click any level to view:
   - Defender profiles
   - Attack scenarios
   - Attack options
   - Defender choices

### Playing the Game
1. Navigate to: `http://localhost:8080`
2. Create account / Login
3. Select defender profile (e.g., Sifat)
4. Experience realistic attack scenarios
5. Make choices and see educational outcomes

---

## 🔍 Verify Initialization

Check console output when starting app:
```
Initializing database with comprehensive cyber attack simulation levels...
✓ Level 1 created: University Hostel Life
✓ Level 2 created: Campus Wide Attacks
✓ Level 3 created: Exam Season Exploitation
✓ Level 4 created: Remote Internship Dangers
✓ Level 5 created: E-Commerce Fraud Season
Database initialized successfully with 5 comprehensive levels!
```

If you see:
```
Database already contains data. Skipping initialization.
```
Then delete the database file and restart.

---

## 📝 Example Scenarios to Test

### Test Sifat's Vulnerability to Mother Call
1. Select Level 1: University Hostel Life
2. Choose Sifat as defender
3. Attacker selects: "Family Emergency Call" → "Call as Mother"
4. See Sifat's 3 choices:
   - ❌ "Send money immediately" (Very vulnerable - lives far!)
   - ✅ "Call father to verify" (Smart!)
   - ✅ "Demand video call first" (Best!)

### Test Sifat's Strength Against Tech Attacks
1. Same level
2. Attacker selects: "Evil Twin WiFi" → "Hostel_WiFi_Free"
3. See Sifat's choices:
   - ❌ "Connect immediately" (Tech student should know better)
   - ✅ "Check with hostel admin" (High tech savviness!)
   - ✅ "Use mobile data" (Safe choice)

---

## 🎯 Key Features to Showcase

### Dynamic Vulnerabilities
- **Sifat + Mother Call** = -40 points (VERY vulnerable - distance factor)
- **Sifat + Friend Call** = +35 points (LESS vulnerable - knows friend)
- **Sifat + WiFi Attack** = +30 points (HIGH tech savvy advantage)

### Educational Notes
Every choice includes explanation:
- Why it's right/wrong
- What vulnerability/strength was involved
- Real-world cybersecurity lesson

### Realistic Scoring
- Wrong choices: -25 to -70 points
- Correct choices: +20 to +60 points
- Reflects real-world consequences

---

## 🛠️ Customization

### Adding More Levels
Use the level editor or follow the pattern in DataInitializer.java:

```java
private void createYourLevel() {
    // 1. Create level
    Level level = new Level();
    level.setName("Your Level Name");
    level.setDifficulty("EASY/MEDIUM/HARD/EXPERT");
    // ... set other properties
    level = levelRepository.save(level);
    
    // 2. Create defender profiles
    DefenderProfile profile = new DefenderProfile();
    // ... set properties
    profile.setLevel(level);
    profile = defenderProfileRepository.save(profile);
    
    // 3. Create attack scenarios
    AttackScenario scenario = new AttackScenario();
    // ... set properties
    scenario.setLevel(level);
    scenario = attackScenarioRepository.save(scenario);
    
    // 4. Create attack options
    AttackOption option = new AttackOption();
    // ... set properties
    option.setAttackScenario(scenario);
    option = attackOptionRepository.save(option);
    
    // 5. Create defender choices
    DefenderChoice choice = new DefenderChoice();
    // ... set properties
    choice.setAttackOption(option);
    defenderChoiceRepository.save(choice);
}
```

---

## 📚 Documentation Files

- **LEVEL_CREATION_SUMMARY.md** - Quick overview and statistics
- **GAME_LEVELS_DOCUMENTATION.md** - Detailed level descriptions
- **This file** - Quick start guide

---

## ⚠️ Troubleshooting

### Levels not showing?
- Check database was deleted before restart
- Look for initialization message in console
- Verify no errors during startup

### Want to reset everything?
1. Stop application
2. Delete `*.mv.db` files
3. Restart application
4. Fresh initialization will occur

### Want to keep some data?
Don't delete database - use level editor to:
- Add new levels manually
- Modify existing content
- Create additional profiles/scenarios

---

## 🎉 Ready to Play!

Your game now has:
- ✅ 5 comprehensive levels
- ✅ 7 unique characters with Sifat as featured profile
- ✅ 100+ realistic scenarios
- ✅ Smart vulnerability system
- ✅ Educational cybersecurity training
- ✅ Dynamic, context-aware gameplay

**Enjoy the enhanced cyber attack simulation game! 🎮🔒**
