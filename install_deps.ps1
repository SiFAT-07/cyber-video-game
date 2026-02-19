# Check and Install Java 17
Write-Host "Checking for Java..."
if (Get-Command java -ErrorAction SilentlyContinue) {
    Write-Host "Java is already installed." -ForegroundColor Green
    java -version
} else {
    Write-Host "Java not found. Installing Microsoft OpenJDK 17 via winget..." -ForegroundColor Yellow
    winget install -e --id Microsoft.OpenJDK.17 --accept-package-agreements --accept-source-agreements
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Failed to install Microsoft OpenJDK 17. Trying ojdkbuild..." -ForegroundColor Yellow
        winget install -e --id ojdkbuild.openjdk.17.jdk --accept-package-agreements --accept-source-agreements
    }
}

# Check and Install Maven
Write-Host "`nChecking for Maven..."
if (Get-Command mvn -ErrorAction SilentlyContinue) {
    Write-Host "Maven is already installed." -ForegroundColor Green
    mvn -version
} else {
    Write-Host "Maven not found. Trying to install via winget..." -ForegroundColor Yellow
    winget install -e --id Apache.Maven --accept-package-agreements --accept-source-agreements
    
    if (-not (Get-Command mvn -ErrorAction SilentlyContinue)) {
        Write-Host "Maven failed via winget or not found. Downloading manually..." -ForegroundColor Yellow
        $mavenVer = "3.9.6"
        $mavenUrl = "https://archive.apache.org/dist/maven/maven-3/$mavenVer/binaries/apache-maven-$mavenVer-bin.zip"
        $installDir = "$env:USERPROFILE\.antigravity\tools"
        $mavenDir = "$installDir\apache-maven-$mavenVer"
        
        New-Item -ItemType Directory -Force -Path $installDir | Out-Null
        
        $zipFile = "$installDir\maven.zip"
        Write-Host "Downloading Maven from $mavenUrl..."
        Invoke-WebRequest -Uri $mavenUrl -OutFile $zipFile
        
        Write-Host "Extracting..."
        Expand-Archive -Path $zipFile -DestinationPath $installDir -Force
        
        # Add to User Path
        $currentPath = [Environment]::GetEnvironmentVariable("Path", [EnvironmentVariableTarget]::User)
        if ($currentPath -notlike "*$mavenDir\bin*") {
            $newPath = "$currentPath;$mavenDir\bin"
            [Environment]::SetEnvironmentVariable("Path", $newPath, [EnvironmentVariableTarget]::User)
            $env:Path += ";$mavenDir\bin"
            Write-Host "Added Maven to User Path." -ForegroundColor Green
        }
        
        Write-Host "Maven installed to $mavenDir" -ForegroundColor Green
    }
}

Write-Host "`nInstallation verification:"
Get-Command java -ErrorAction SilentlyContinue
Get-Command mvn -ErrorAction SilentlyContinue
