# CyberWalk Launcher for PowerShell

Write-Host "========================================"
Write-Host "CyberWalk Interactive Story Game"
Write-Host "========================================"
Write-Host ""

# Check if Maven is installed
$mvnExists = Get-Command mvn -ErrorAction SilentlyContinue
if (-not $mvnExists) {
    Write-Host "ERROR: Maven is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install Maven and add it to your PATH"
    Write-Host "See SETUP.md for instructions"
    Read-Host "Press Enter to exit"
    exit 1
}

# Check if Java is installed
$javaExists = Get-Command java -ErrorAction SilentlyContinue
if (-not $javaExists) {
    Write-Host "ERROR: Java is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install Java 17+ and add it to your PATH"
    Write-Host "See SETUP.md for instructions"
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "Maven and Java found!" -ForegroundColor Green
Write-Host ""
Write-Host "Building project..."
mvn clean install -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "ERROR: Build failed!" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""
Write-Host "========================================"
Write-Host "Starting CyberWalk Server..."
Write-Host "========================================"
Write-Host ""
Write-Host "Access the game at: http://localhost:8080" -ForegroundColor Cyan
Write-Host "Press Ctrl+C to stop the server"
Write-Host ""

mvn spring-boot:run
