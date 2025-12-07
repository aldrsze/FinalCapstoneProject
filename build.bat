@echo off
echo Building SmartStock...
echo.

REM clean old files
if exist bin\com rmdir /s /q bin\com
if exist bin\config.properties del /q bin\config.properties

REM compile java files
echo Compiling...
javac --release 21 -d bin -cp "lib\*" -encoding UTF-8 src\App.java src\com\inventorysystem\data\*.java src\com\inventorysystem\gui\*.java src\com\inventorysystem\model\*.java src\com\inventorysystem\util\*.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Build failed!
    pause
    exit /b 1
)

REM copy config file
copy src\config.properties bin\config.properties > nul

REM create jar
echo Creating JAR...
jar cfm SmartStock.jar MANIFEST.MF -C bin .

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo JAR creation failed!
    pause
    exit /b 1
)

echo.
echo Build successful!
echo Run: java -jar SmartStock.jar
echo.
timeout /t 2 /nobreak > nul
