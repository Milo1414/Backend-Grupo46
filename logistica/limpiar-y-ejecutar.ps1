# Script para limpiar completamente el proyecto y ejecutarlo
Write-Host "=== Limpiando proyecto Maven ===" -ForegroundColor Cyan
mvn clean

Write-Host "`n=== Compilando proyecto ===" -ForegroundColor Cyan
mvn compile

Write-Host "`n=== Ejecutando aplicación ===" -ForegroundColor Green
mvn spring-boot:run
