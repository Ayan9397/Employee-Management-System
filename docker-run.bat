@echo off
setlocal
cd /d "%~dp0"
title Employee Management System - Docker Launcher

echo ======================================================================
echo             Starting EMS Containerized Environment
echo ======================================================================
echo.

docker info >nul 2>nul
if errorlevel 1 (
    echo [ERROR] Docker is not running. Please start Docker Desktop first.
    pause
    exit /b 1
)

echo [INFO] Building and launching MySQL and EMS containers...
docker compose up -d mysql

echo [INFO] Waiting for database readiness...
docker compose run --rm app

endlocal

