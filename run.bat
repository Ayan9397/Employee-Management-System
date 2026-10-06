@echo off
setlocal
cd /d "%~dp0"
title Employee Management System (EMS)

echo ======================================================================
echo                  EMPLOYEE MANAGEMENT SYSTEM
echo             Java + OOP + JDBC + MySQL Architecture
echo ======================================================================
echo.

if not exist "bin\com\ems\Main.class" (
    echo [INFO] Compiling Java source files...
    call compile.bat
    if errorlevel 1 (
        echo [ERROR] Compilation failed!
        pause
        exit /b %errorlevel%
    )
)

echo [INFO] Starting Application...
echo.
java -cp "bin;lib\mysql-connector-java.jar" com.ems.Main

endlocal

