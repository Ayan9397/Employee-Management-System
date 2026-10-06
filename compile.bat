@echo off
setlocal
cd /d "%~dp0"

echo [INFO] Compiling Employee Management System Java Sources...

if not exist bin mkdir bin

javac -encoding UTF-8 -cp "lib\mysql-connector-java.jar" -d bin ^
    src\com\ems\*.java ^
    src\com\ems\model\*.java ^
    src\com\ems\dao\*.java ^
    src\com\ems\dao\jdbc\*.java ^
    src\com\ems\dao\memory\*.java ^
    src\com\ems\service\*.java ^
    src\com\ems\util\*.java ^
    src\com\ems\ui\*.java

set COMPILE_STATUS=%errorlevel%

if %COMPILE_STATUS% equ 0 (
    echo [SUCCESS] Compilation finished successfully! Class files written to bin/
) else (
    echo [ERROR] Compilation failed with error code %COMPILE_STATUS%
)

exit /b %COMPILE_STATUS%

