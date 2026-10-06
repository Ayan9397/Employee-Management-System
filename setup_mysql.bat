@echo off
setlocal
cd /d "%~dp0"

echo ======================================================================
echo              MySQL Database Setup for EMS
echo ======================================================================
echo.

set /p MYSQL_USER="Enter MySQL Username [default: root]: "
if "%MYSQL_USER%"=="" set MYSQL_USER=root

set /p MYSQL_PASS="Enter MySQL Password: "

set MYSQL_BIN=mysql
where mysql >nul 2>nul
if errorlevel 1 (
    if exist "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" (
        set "MYSQL_BIN=C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
    ) else (
        echo [ERROR] mysql.exe not found in PATH or standard installation folder.
        pause
        exit /b 1
    )
)

echo.
echo [INFO] Executing sql\schema.sql using %MYSQL_BIN%...

if "%MYSQL_PASS%"=="" (
    "%MYSQL_BIN%" -u %MYSQL_USER% < sql\schema.sql
) else (
    "%MYSQL_BIN%" -u %MYSQL_USER% -p%MYSQL_PASS% < sql\schema.sql
)

if errorlevel 1 (
    echo.
    echo [ERROR] Failed to import database schema. Please check username and password.
) else (
    echo.
    echo [SUCCESS] Database and sample records initialized successfully!
    echo [INFO] Updating db.properties with provided user credentials...
    (
        echo # Database Connection Configuration for MySQL
        echo db.url=jdbc:mysql://localhost:3306/employee_management_db?useSSL=false^&allowPublicKeyRetrieval=true^&serverTimezone=UTC^&createDatabaseIfNotExist=true
        echo db.user=%MYSQL_USER%
        echo db.password=%MYSQL_PASS%
    ) > db.properties
    echo [SUCCESS] db.properties updated!
)

echo.
pause

