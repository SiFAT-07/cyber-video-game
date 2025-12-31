@echo off
echo ========================================
echo CyberWalk Interactive Story Game
echo ========================================
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven and add it to your PATH
    echo See SETUP.md for instructions
    pause
    exit /b 1
)

REM Check if Java is installed
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 17+ and add it to your PATH
    echo See SETUP.md for instructions
    pause
    exit /b 1
)

echo Maven and Java found!
echo.
echo Building project...
call mvn clean install -DskipTests

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Build failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo Starting CyberWalk Server...
echo ========================================
echo.
echo Access the game at: http://localhost:8080
echo Press Ctrl+C to stop the server
echo.

call mvn spring-boot:run
