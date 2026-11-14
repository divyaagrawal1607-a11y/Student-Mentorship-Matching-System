@echo off
setlocal enabledelayedexpansion
title MentorBridge - Setup & Dependency Configuration

echo =====================================================================
echo    MentorBridge - Student Mentorship Matching System
echo    NSP Java Mini Project - Divya Agarwal
echo    Automated Setup and Build Script
echo =====================================================================
echo.

cd /d "%~dp0"

:: 1. Detect Java JDK 17+
set "JAVA_CMD="
if exist "%~dp0tools\jdk\jdk-17.0.13+11\bin\java.exe" (
    set "JAVA_HOME=%~dp0tools\jdk\jdk-17.0.13+11"
    set "JAVA_CMD=%~dp0tools\jdk\jdk-17.0.13+11\bin\java.exe"
    set "PATH=%~dp0tools\jdk\jdk-17.0.13+11\bin;%PATH%"
    echo [OK] Using portable JDK 17: !JAVA_HOME!
) else (
    where java >nul 2>nul
    if %ERRORLEVEL% equ 0 (
        set "JAVA_CMD=java"
        echo [OK] Found system Java installation.
    ) else (
        echo [ERROR] Java JDK 17 was not found. Please install JDK 17+ or ensure tools folder is present.
        pause
        exit /b 1
    )
)

:: 2. Detect Apache Maven
set "MVN_CMD="
if exist "%~dp0tools\mvn\apache-maven-3.9.6\bin\mvn.cmd" (
    set "MVN_CMD=%~dp0tools\mvn\apache-maven-3.9.6\bin\mvn.cmd"
    echo [OK] Using portable Apache Maven: %~dp0tools\mvn\apache-maven-3.9.6
) else (
    where mvn >nul 2>nul
    if %ERRORLEVEL% equ 0 (
        set "MVN_CMD=mvn"
        echo [OK] Found system Apache Maven installation.
    ) else (
        echo [ERROR] Apache Maven was not found.
        pause
        exit /b 1
    )
)

:: 3. Setup .env file
if not exist "%~dp0.env" (
    if exist "%~dp0.env.example" (
        copy "%~dp0.env.example" "%~dp0.env" >nul
        echo [OK] Generated local .env configuration from template.
    )
) else (
    echo [OK] Existing .env configuration detected.
)

:: 4. Check MySQL Status
echo [INFO] Checking local MySQL status on port 3306...
powershell -NoProfile -Command "$t = Test-NetConnection -ComputerName localhost -Port 3306 -WarningAction SilentlyContinue; if ($t.TcpTestSucceeded) { Write-Host '[OK] MySQL Server is listening on port 3306.' } else { Write-Host '[NOTICE] MySQL Server is not listening on port 3306. Application will use the resilient persistent embedded engine with automatic sync.' }"

:: 5. Compile & Package Application
echo.
echo [INFO] Resolving Maven dependencies and compiling project...
call "%MVN_CMD%" clean package
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Maven build failed. Please review compilation output above.
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo =====================================================================
echo [SUCCESS] Setup and compilation completed successfully!
echo You can now launch MentorBridge anytime by double-clicking 'run.bat'
echo =====================================================================
echo.
pause
