$project = Split-Path -Parent $MyInvocation.MyCommand.Path
$javaHome = 'C:\Users\raman\.Neo4jDesktop2\Cache\runtime\zulu17.60.17-ca-jdk17.0.16-win_x64'
$env:JAVA_HOME = $javaHome
$env:Path = "$javaHome\bin;$env:Path"

$appOut = Join-Path $project 'spring-boot.out.log'
$appErr = Join-Path $project 'spring-boot.err.log'
$tunnelOut = Join-Path $project 'localhostrun.log'
$tunnelErr = Join-Path $project 'localhostrun.err.log'

Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force
Get-Process ssh -ErrorAction SilentlyContinue | Stop-Process -Force

Remove-Item $appOut,$appErr,$tunnelOut,$tunnelErr -Force -ErrorAction SilentlyContinue

$app = Start-Process -FilePath (Join-Path $project 'mvnw.cmd') -ArgumentList 'spring-boot:run' -WorkingDirectory $project -RedirectStandardOutput $appOut -RedirectStandardError $appErr -PassThru
Write-Host "Spring Boot starting... PID=$($app.Id)"
Start-Sleep -Seconds 20

$tunnel = Start-Process -FilePath 'C:\Windows\System32\OpenSSH\ssh.exe' -ArgumentList '-o StrictHostKeyChecking=accept-new -o ServerAliveInterval=30 -R 80:localhost:8080 nokey@localhost.run' -WorkingDirectory $project -RedirectStandardOutput $tunnelOut -RedirectStandardError $tunnelErr -PassThru
Write-Host "Tunnel starting... PID=$($tunnel.Id)"
Start-Sleep -Seconds 12

$url = $null
if (Test-Path $tunnelOut) {
    $match = Select-String -Path $tunnelOut -Pattern 'https://[^\s]+' | Select-Object -First 1
    if ($match) {
        $url = [regex]::Match($match.Line, 'https://[^\s]+').Value
    }
}

if ($url) {
    Write-Host "Public URL: $url"
} else {
    Write-Host "Tunnel started, but URL was not parsed automatically. Check localhostrun.log"
}

Write-Host "Local URL: http://localhost:8080"
Write-Host "H2 Console: http://localhost:8080/h2-console"