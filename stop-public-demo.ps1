Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force
Get-Process ssh -ErrorAction SilentlyContinue | Stop-Process -Force
Write-Host 'Stopped Spring Boot and tunnel processes.'