@echo off
setlocal
echo ========================================================
echo   Envoi du projet Douane Temporelle vers GitHub
echo   Repo : https://github.com/youssefarous/DouaneTemporelle.git
echo ========================================================
echo.

set "PATH=%LOCALAPPDATA%\MinGit\cmd;%LOCALAPPDATA%\MinGit\mingw64\bin;%PATH%"

cd /d "%~dp0"

echo Verification des commits locaux :
git log --oneline -n 6
echo.
echo Envoi en cours vers GitHub (main)...
git push -u origin main --force

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================================
    echo   SUCCES ! Vos commits ont ete publies sur GitHub !
    echo   Verifiez sur : https://github.com/youssefarous/DouaneTemporelle
    echo ========================================================
) else (
    echo.
    echo [!] Une authentification GitHub est requise.
    echo Connectez-vous avec votre compte youssefarous.
)

pause
