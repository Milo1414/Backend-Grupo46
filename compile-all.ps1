# ============================================================================
# Script: Compilar todos los módulos Maven
# Uso: .\compile-all.ps1
# ============================================================================

Write-Host "================================" -ForegroundColor Cyan
Write-Host "Compilando módulos Backend-Grupo46" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan

$modules = @("logistica", "recursos")
$scriptPath = Split-Path -Parent -Path $MyInvocation.MyCommand.Definition
$projectRoot = $scriptPath

$allSuccess = $true

foreach ($module in $modules) {
    $modulePath = Join-Path $projectRoot $module
    
    if (Test-Path $modulePath) {
        Write-Host "`n[1/2] Compilando módulo: $module" -ForegroundColor Yellow
        Write-Host "Directorio: $modulePath" -ForegroundColor Gray
        
        Push-Location $modulePath
        
        # Ejecutar compilación
        & mvn clean compile -q
        $compileExit = $LASTEXITCODE
        
        Pop-Location
        
        if ($compileExit -eq 0) {
            Write-Host "✅ $module compilado exitosamente" -ForegroundColor Green
        } else {
            Write-Host "❌ Error compilando $module" -ForegroundColor Red
            $allSuccess = $false
        }
    } else {
        Write-Host "⚠️  Módulo no encontrado: $module en $modulePath" -ForegroundColor Yellow
    }
}

Write-Host "`n================================" -ForegroundColor Cyan

if ($allSuccess) {
    Write-Host "✅ Compilación completada exitosamente" -ForegroundColor Green
    exit 0
} else {
    Write-Host "❌ Compilación falló en uno o más módulos" -ForegroundColor Red
    exit 1
}
