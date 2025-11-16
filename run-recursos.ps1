# ============================================================================
# Script: Ejecutar la aplicación recursos
# Uso: .\run-recursos.ps1
# ============================================================================

Write-Host "================================" -ForegroundColor Cyan
Write-Host "Iniciando aplicación Recursos" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan

$scriptPath = Split-Path -Parent -Path $MyInvocation.MyCommand.Definition
$recursosPath = Join-Path $scriptPath "recursos"

if (Test-Path $recursosPath) {
    Write-Host "Directorio: $recursosPath" -ForegroundColor Gray
    Push-Location $recursosPath
    
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
    Write-Host "❌ Módulo recursos no encontrado en: $recursosPath" -ForegroundColor Red
    exit 1
}
