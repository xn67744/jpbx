@echo off
setlocal
title JPBX 8090
chcp 65001 >nul

rem ============================================================
rem  JPBX (FreeSWITCH GUI) launcher  --  auto log to logs\jpbx-console.log
rem
rem   start-jpbx.bat           start  (prompt if 8090 is busy)
rem   start-jpbx.bat force     kill the old instance, then start
rem   start-jpbx.bat stop      stop the instance on 8090
rem   start-jpbx.bat status    show status + log tail
rem   start-jpbx.bat service   foreground run (Task Scheduler / systemd style)
rem ============================================================

for %%I in ("%~dp0.") do set "APP_HOME=%%~fI"
set "APP_HOME=%APP_HOME%\av-admin\target"
for %%L in ("%APP_HOME%\..\..") do set "LOG_DIR=%%~fL\logs"
set "PORT=8090"
set "LOG=%LOG_DIR%\jpbx-console.log"
if not exist "%LOG_DIR%" md "%LOG_DIR%" 2>nul

if /i "%~1"=="stop"    goto :stop
if /i "%~1"=="status"  goto :status
if /i "%~1"=="service" goto :service

rem ---------- 1. locate a JDK ----------
rem Build target is JDK 21 -> prefer JDK 21, then JPBX_JAVA / JAVA_HOME, then JDK 8.
set "JAVA_EXE="
rem optional override:  set JPBX_JAVA=F:\Dev\jdk\jdk-17.0.18
if defined JPBX_JAVA if exist "%JPBX_JAVA%\bin\java.exe" set "JAVA_EXE=%JPBX_JAVA%\bin\java.exe"
for %%J in ("F:\Dev\jdk\jdk-21" "F:\Dev\jdk\jdk-21.0.5" "F:\Dev\jdk\jdk-17.0.18" "F:\Program Files\jdk8u512-b09" "F:\Dev\jdk\jdk1.8.0_151" "C:\Program Files\Java\jdk1.8.0_151") do (
  if not defined JAVA_EXE if exist "%%~J\bin\java.exe" set "JAVA_EXE=%%~J\bin\java.exe"
)
rem JDK21 下 Spring 5.3/Shiro 1.13 仍会触及部分 JDK 内部反射，显式放行
set "JVM_OPTS=-Dname=av-admin.jar -Dfile.encoding=UTF-8 -Duser.timezone=GMT+8 -Xms512m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m -XX:+HeapDumpOnOutOfMemoryError --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED --add-opens java.base/java.text=ALL-UNNAMED"
if not defined JAVA_EXE if defined JAVA_HOME if exist "%JAVA_HOME%\bin\java.exe" set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
if not defined JAVA_EXE for %%J in (java.exe) do set "JAVA_EXE=%%~$PATH:J"
if not defined JAVA_EXE (
  echo [ERROR] no java.exe found - install JDK 21 or set JAVA_HOME / JPBX_JAVA
  goto :hold
)
if not exist "%APP_HOME%\av-admin.jar" (
  echo [ERROR] %APP_HOME%\av-admin.jar not found
  echo         build first:  mvn clean package -Dmaven.test.skip=true
  goto :hold
)

echo [JPBX] java : %JAVA_EXE%
echo [JPBX] jar  : %APP_HOME%\av-admin.jar
echo [JPBX] log  : %LOG%

if /i "%~1"=="service" goto :service

rem ---------- 2. FreeSWITCH / ESL check ----------
netstat -ano | findstr /r /c:":8021 .*LISTENING" >nul 2>&1
if not errorlevel 1 goto :fsok
echo.
echo [WARN] ESL 8021 is not listening - FreeSWITCH is NOT running.
echo        Without it: registration / calls / CDR are all dead, and the app
echo        will just spam "ESL connect failed ... Connection refused".
set "ANS="
set /p "ANS=       start FreeSWITCH now? [Y/n] "
if /i "%ANS%"=="n" goto :fsok
if not defined JPBX_FS_HOME set "JPBX_FS_HOME=K:\Projects\C++\FreeSWITCH"
if not exist "%JPBX_FS_HOME%\FreeSwitchConsole.exe" (
  echo [WARN] FreeSwitchConsole.exe not found, skipping.
  goto :fsok
)
echo [JPBX] starting FreeSWITCH ...
start "FreeSWITCH" /min /D "%JPBX_FS_HOME%" FreeSwitchConsole.exe -nonat
for /l %%i in (1,1,15) do (
  netstat -ano | findstr /r /c:":8021 .*LISTENING" >nul 2>&1
  if not errorlevel 1 goto :fsok
  call :sleep 2
)
echo [WARN] FreeSWITCH still not listening on 8021 - continuing anyway.
:fsok

rem ---------- 3. port check ----------
call :pidOnPort
if defined BUSY_PID (
  echo.
  echo [WARN] port %PORT% already in use by PID %BUSY_PID%
  if /i not "%~1"=="force" (
    set "ANS="
    set /p "ANS=       kill it and restart? [y/N] "
    if /i not "%ANS%"=="y" (
      echo [JPBX] aborted - run "start-jpbx.bat status" to inspect
      goto :hold
    )
  )
  call :stop
  call :sleep 2
)

rem ---------- 3. rotate log, launch detached ----------
if exist "%LOG%" move /y "%LOG%" "%LOG%.1" >nul 2>&1
del /q "%LOG_DIR%\jpbx-stderr.log" >nul 2>&1

pushd "%APP_HOME%"
start "JPBX-8090" /min cmd /c ""%JAVA_EXE%" %JVM_OPTS% -jar "av-admin.jar" >> "%LOG%" 2>&1"
popd

rem ---------- 4. wait for http ----------
echo [JPBX] waiting for http://localhost:%PORT% ...
set "UP="
for /l %%i in (1,1,45) do (
  if not defined UP (
    netstat -ano | findstr /r /c:":%PORT% .*LISTENING" >nul 2>&1
    if not errorlevel 1 set "UP=1"
    if not defined UP call :sleep 2
  )
)

if defined UP (
  echo.
  echo [JPBX] OK  -^> http://localhost:%PORT%     login: admin / admin123
  echo [JPBX] log -^> %LOG%
) else (
  echo.
  echo [JPBX] NOT listening after ~90s. last 40 log lines:
  echo ------------------------------------------------------------
  if exist "%LOG%" powershell -NoProfile -Command "Get-Content -Tail 40 -Encoding UTF8 '%LOG%'"
  echo ------------------------------------------------------------
  echo full log: %LOG%
)
rem keep the window open only for double-click use (no args)
if "%~1"=="" pause
exit /b 0

rem ---------- foreground mode for Task Scheduler / service wrappers ----------
:service
if exist "%LOG%" move /y "%LOG%" "%LOG%.1" >nul 2>&1
echo [JPBX] %DATE% %TIME% starting av-admin.jar (service) > "%LOG%"
pushd "%APP_HOME%"
"%JAVA_EXE%" %JVM_OPTS% -jar "av-admin.jar" >> "%LOG%" 2>&1
set "EXIT_CODE=%ERRORLEVEL%"
popd
echo [JPBX] %DATE% %TIME% exited with code %EXIT_CODE% >> "%LOG%"
exit /b %EXIT_CODE%

rem ============================================================
:pidOnPort
set "BUSY_PID="
for /f "tokens=5" %%p in ('netstat -ano ^| findstr /r /c:":%PORT% .*LISTENING"') do (
  if not defined BUSY_PID set "BUSY_PID=%%p"
)
exit /b 0

:sleep
ping -n %1 127.0.0.1 >nul 2>&1
exit /b 0

:stop
call :pidOnPort
if not defined BUSY_PID (
  echo [JPBX] nothing listening on %PORT%
  exit /b 0
)
echo [JPBX] stopping PID %BUSY_PID% ...
taskkill /PID %BUSY_PID% /T /F >nul 2>&1
call :sleep 2
call :pidOnPort
if defined BUSY_PID (echo [JPBX] still alive: %BUSY_PID%) else (echo [JPBX] stopped)
exit /b 0

:status
call :pidOnPort
if defined BUSY_PID (
  echo [JPBX] running  PID=%BUSY_PID%  http://localhost:%PORT%
) else (
  echo [JPBX] not running
)
if exist "%LOG%" (
  echo -------- last 8 lines of %LOG% --------
  powershell -NoProfile -Command "Get-Content -Tail 8 -Encoding UTF8 '%LOG%'"
)
exit /b 0

:hold
echo.
pause
exit /b 0
