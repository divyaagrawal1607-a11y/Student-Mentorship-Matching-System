@echo off
setlocal enabledelayedexpansion
title MentorBridge - Launch Application

echo =====================================================================
echo    MentorBridge - Student Mentorship Matching System
echo    NSP Java Mini Project - Divya Agarwal
echo =====================================================================
echo.

cd /d "%~dp0"

:: 1. Detect Java JDK 17+
set "JAVA_CMD="
if exist "%~dp0tools\jdk\jdk-17.0.13+11\bin\javaw.exe" (
    set "JAVA_HOME=%~dp0tools\jdk\jdk-17.0.13+11"
    set "JAVA_CMD=%~dp0tools\jdk\jdk-17.0.13+11\bin\java.exe"
    set "PATH=%~dp0tools\jdk\jdk-17.0.13+11\bin;%PATH%"
) else (
    where java >nul 2>nul
    if %ERRORLEVEL% equ 0 (
        set "JAVA_CMD=java"
    ) else (
        echo [ERROR] Java JDK 17 was not found!
        echo Please run setup.bat first to configure the environment.
        pause
        exit /b 1
    )
)

:: 2. Check for Compiled JAR
set "JAR_PATH=%~dp0target\student-mentorship-system-1.0.0.jar"
if not exist "%JAR_PATH%" (
    echo [INFO] Application JAR not found. Running initial build...
    call "%~dp0setup.bat"
    if not exist "%JAR_PATH%" (
        echo [ERROR] Build failed to generate %JAR_PATH%.
        pause
        exit /b 1
    )
)

:: 3. Test MySQL Connection Note
powershell -NoProfile -Command "$t = Test-NetConnection -ComputerName localhost -Port 3306 -WarningAction SilentlyContinue; if ($t.TcpTestSucceeded) { Write-Host '[OK] MySQL Server active on port 3306.' } else { Write-Host '[INFO] MySQL Server offline. Running in resilient persistent storage mode.' }"

:: 4. Launch Application
echo [INFO] Starting MentorBridge GUI...
start "" "%JAVA_CMD%" -jar "%JAR_PATH%"
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Failed to start Java process.
    pause
    exit /b %ERRORLEVEL%
)

echo [OK] MentorBridge is running!
exit /b 0
