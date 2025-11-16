# ============================================================================
# Script: Ejecutar la aplicación logistica
# Uso: .\run-logistica.ps1
# ============================================================================

Write-Host "================================" -ForegroundColor Cyan
Write-Host "Iniciando aplicación Logistica" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan

$scriptPath = Split-Path -Parent -Path $MyInvocation.MyCommand.Definition
$logisticaPath = Join-Path $scriptPath "logistica"

if (Test-Path $logisticaPath) {
    Write-Host "Directorio: $logisticaPath" -ForegroundColor Gray
    Push-Location $logisticaPath
    
    Write-Host "`n[1] Compilando..." -ForegroundColor Yellow
    & mvn clean compile -q
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Compilación exitosa" -ForegroundColor Green
        Write-Host "`n[2] Ejecutando Spring Boot..." -ForegroundColor Yellow
        & mvn spring-boot:run
    } else {
        Write-Host "❌ Error durante la compilación" -ForegroundColor Red
    }
    
    Pop-Location
} else {
    Write-Host "❌ Módulo logistica no encontrado en: $logisticaPath" -ForegroundColor Red
    exit 1
}
