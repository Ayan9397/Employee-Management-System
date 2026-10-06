@echo off
setlocal
cd /d "%~dp0"

echo ======================================================================
echo               Packaging Employee Management System (EMS)
echo ======================================================================
echo.

echo [1/4] Compiling Java classes...
call compile.bat
if errorlevel 1 (
    echo [ERROR] Compilation failed!
    pause
    exit /b 1
)

echo.
echo [2/4] Preparing dist directory...
if not exist dist mkdir dist
if not exist dist\lib mkdir dist\lib

echo [3/4] Creating manifest and building Executable JAR...
(
    echo Manifest-Version: 1.0
    echo Main-Class: com.ems.Main
    echo Class-Path: lib/mysql-connector-java.jar
    echo Created-By: Mohd Ayan
) > manifest.txt

jar cfm dist\EmployeeManagementSystem.jar manifest.txt -C bin .
set JAR_STATUS=%errorlevel%
del manifest.txt

if %JAR_STATUS% neq 0 (
    echo [ERROR] JAR creation failed!
    pause
    exit /b %JAR_STATUS%
)

echo.
echo [4/4] Copying runtime dependencies and config...
copy /y lib\mysql-connector-java.jar dist\lib\ >nul
copy /y db.properties dist\ >nul

(
    echo @echo off
    echo title Employee Management System
    echo java -jar EmployeeManagementSystem.jar
    echo pause
) > dist\run.bat

(
    echo #!/usr/bin/env bash
    echo java -jar EmployeeManagementSystem.jar
) > dist\run.sh

echo.
echo ======================================================================
echo [SUCCESS] Package created successfully in dist/
echo  - JAR File : dist\EmployeeManagementSystem.jar
echo  - Launcher : dist\run.bat
echo ======================================================================
echo.

