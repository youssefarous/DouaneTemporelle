$env:PATH = "$env:LOCALAPPDATA\MinGit\cmd;$env:LOCALAPPDATA\MinGit\mingw64\bin;$env:PATH"
Set-Location $PSScriptRoot

Write-Host "Vérification des commits locaux :" -ForegroundColor Cyan
git log --oneline -n 6

Write-Host "`nEnvoi vers GitHub (https://github.com/youssefarous/DouaneTemporelle.git)..." -ForegroundColor Yellow
git push -u origin main --force
