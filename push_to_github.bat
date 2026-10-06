@echo off
setlocal
cd /d "%~dp0"
title Push EMS to GitHub

echo ======================================================================
echo              Push Employee Management System to GitHub
echo              (Triggers GitHub Actions CI/CD Pipeline)
echo ======================================================================
echo.

git remote get-url origin >nul 2>nul
if errorlevel 1 (
    echo [STEP 1] Please create a new empty repository on GitHub:
    echo          https://github.com/new
    echo.
    set /p REPO_URL="Enter your GitHub Repository URL (e.g. https://github.com/Ayan9397/EMS.git): "
    if "%REPO_URL%"=="" (
        echo [ERROR] No URL entered. Aborting.
        pause
        exit /b 1
    )
    git remote add origin %REPO_URL%
    echo [SUCCESS] Added remote origin: %REPO_URL%
) else (
    echo Remote repository already configured.
)

echo.
echo [STEP 2] Pushing code to GitHub...
git branch -M main
git push -u origin main

if errorlevel 1 (
    echo.
    echo [ERROR] Git push failed. Please check your GitHub credentials or repo permissions.
) else (
    echo.
    echo ======================================================================
    echo [SUCCESS] Code pushed successfully to GitHub!
    echo.
    echo Your GitHub Actions CI/CD workflow is now running:
    echo Go to your repository on GitHub and click the "Actions" tab to view
    echo the live build, smoke test, and download your release artifact!
    echo ======================================================================
)

echo.
pause
