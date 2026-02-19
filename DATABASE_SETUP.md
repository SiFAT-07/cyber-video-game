# Database Configuration - Persistent Storage

## Overview
The application now uses a **file-based H2 database** that persists all data to disk. This means:
- ✅ All registered user accounts are saved permanently
- ✅ All level editor data persists across restarts
- ✅ Game sessions, rooms, and scenarios are preserved
- ✅ Data remains available even after browser refresh
- ✅ Data persists across different browsers on the same device
- ✅ Data survives application restarts

## Database Location
The database files are stored in: `./data/cyberwalk.*`

This directory is created automatically in your project root when you first run the application.

## Database Files
After the first run, you'll see these files in the `data` folder:
- `cyberwalk.mv.db` - Main database file
- `cyberwalk.trace.db` - Trace/log file (optional)

**⚠️ Important**: Do NOT delete these files unless you want to reset all data!

## Configuration Changes Made

### 1. Application Properties (`application.properties`)
Changed from in-memory database to file-based:
```properties
# OLD (in-memory - data lost on restart):
spring.datasource.url=jdbc:h2:mem:cyberwalk

# NEW (file-based - data persists):
spring.datasource.url=jdbc:h2:file:./data/cyberwalk
```

**Connection string explanation:**
- `jdbc:h2:file:./data/cyberwalk` - Stores database in `./data` folder
- Database files created: `cyberwalk.mv.db` (main data) and `cyberwalk.trace.db` (logs)

### 2. Data Initializer (`DataInitializer.java`)
Modified to only initialize default scenarios on first run:
- Checks if database already has data
- If data exists, skips initialization (preserves your changes)
- If empty, loads default game scenarios

### 3. Git Ignore
Added database files to `.gitignore` to prevent committing user data:
```
/data/
*.mv.db
*.trace.db
```

## How to Use

### First Time Setup
1. Start the application (run `run.bat` or `run.ps1`)
2. Database will be created automatically in `./data` folder
3. Default scenarios will be loaded
4. Register user accounts - they will persist!

### Accessing H2 Console (For Debugging)
The H2 web console is still available at: `http://localhost:8080/h2-console`

**Connection Settings:**
- JDBC URL: `jdbc:h2:file:./data/cyberwalk`
- Username: `sa`
- Password: (leave empty)

## Data Persistence Examples

### User Registration
```
✅ Register a new account → Saved to database
✅ Restart application → Account still exists
✅ Open in different browser → Same account available
✅ Stop server and start later → Account persists
```

### Level Editor
```
✅ Create a new level → Saved to database
✅ Close browser → Level data preserved
✅ Restart application → Level still exists
✅ Edit level → Changes are saved automatically
```

### Game Sessions & Rooms
```
✅ All game sessions persist
✅ Room configurations are saved
✅ Player progress is maintained
```

## Resetting the Database

If you want to start fresh with a clean database:

1. **Stop the application**
2. **Delete the data folder**: `rm -r data` (PowerShell) or manually delete
3. **Restart the application** - fresh database will be created with default scenarios

## Backup Your Data

To backup your database:
1. Stop the application
2. Copy the entire `data` folder to a safe location
3. To restore: replace the `data` folder with your backup

## Troubleshooting

### Problem: Data not saving
- Check if `data` folder exists and has write permissions
- Check application logs for database errors
- Verify `spring.jpa.hibernate.ddl-auto=update` is set

### Problem: Database locked
- Make sure only one instance of the application is running
- If needed, delete `cyberwalk.trace.db` file

### Problem: Want to reset to default scenarios
- Delete the `data` folder
- Restart the application

## Technical Details

### Entity Persistence
All entities are properly configured with JPA annotations:
- `User` - Registered accounts
- `Level` - Level editor levels
- `Scenario` - Game scenarios
- `Option` - Choice options
- `GameSession` - Active game sessions
- `GameRoom` - Multiplayer rooms
- `DefenderProfile`, `AttackScenario`, etc.

### Database Schema
Schema is managed automatically by Hibernate:
- `spring.jpa.hibernate.ddl-auto=update` - Updates schema without losing data
- Tables are created/updated automatically on startup
- No manual SQL migration needed

## Migration from In-Memory Database

If you had data in the old in-memory database, it's unfortunately lost (that's the nature of in-memory databases). However, going forward:
- All new registrations will persist
- All level editor work will be saved
- You'll never lose data on restart again!

---

**You're all set!** Your application now has full data persistence. Enjoy creating levels and registering accounts without worrying about data loss! 🎮
