# Script para manejar Docker fácilmente
# Uso: .\docker-commands.ps1 start
#      .\docker-commands.ps1 stop
#      .\docker-commands.ps1 logs
#      .\docker-commands.ps1 reset

param(
    [string]$command = "help"
)

$projectPath = Get-Location

Write-Host "🐳 Docker Compose Helper" -ForegroundColor Cyan
Write-Host "========================`n" -ForegroundColor Cyan

switch ($command) {
    "start" {
        Write-Host "▶️  Iniciando PostgreSQL..." -ForegroundColor Green
        docker-compose up postgres -d
        Write-Host "`n✅ PostgreSQL está corriendo en puerto 5432" -ForegroundColor Green
        Write-Host "📝 Para ejecutar la app: cd logistica && mvn spring-boot:run`n" -ForegroundColor Yellow
    }
    
    "start-all" {
        Write-Host "▶️  Iniciando BD + Aplicación..." -ForegroundColor Green
        docker-compose up --build -d
        Write-Host "`n✅ PostgreSQL y App están corriendo" -ForegroundColor Green
        Write-Host "🌐 Accede a: http://localhost:8081/swagger-ui.html`n" -ForegroundColor Yellow
    }
    
    "stop" {
        Write-Host "⏹️  Deteniendo servicios..." -ForegroundColor Yellow
        docker-compose down
        Write-Host "✅ Servicios detenidos`n" -ForegroundColor Green
    }
    
    "logs" {
        Write-Host "📋 Mostrando logs..." -ForegroundColor Cyan
        docker-compose logs -f
    }
    
    "logs-db" {
        Write-Host "📋 Logs de PostgreSQL..." -ForegroundColor Cyan
        docker-compose logs -f postgres
    }
    
    "logs-app" {
        Write-Host "📋 Logs de la App..." -ForegroundColor Cyan
        docker-compose logs -f logistica-app
    }
    
    "status" {
        Write-Host "📊 Estado de servicios:" -ForegroundColor Cyan
        docker ps --format "table {{.Names}}\t{{.Image}}\t{{.Status}}"
    }
    
    "db" {
        Write-Host "🗄️  Conectando a PostgreSQL..." -ForegroundColor Cyan
        docker-compose exec postgres psql -U postgres -d logistica_db
    }
    
    "reset" {
        Write-Host "🔄 Reseteando todo (ELIMINA DATOS)..." -ForegroundColor Red
        $confirm = Read-Host "¿Estás seguro? (s/n)"
        if ($confirm -eq "s") {
            docker-compose down -v
            Write-Host "✅ Todo reseteado`n" -ForegroundColor Green
        }
    }
    
    "clean" {
        Write-Host "🧹 Limpiando contenedores parados..." -ForegroundColor Yellow
        docker system prune -f
        Write-Host "✅ Limpieza completada`n" -ForegroundColor Green
    }
    
    default {
        Write-Host "📖 Comandos disponibles:`n" -ForegroundColor Cyan
        Write-Host "  start       - Inicia solo PostgreSQL (Recomendado para desarrollo)" -ForegroundColor White
        Write-Host "  start-all   - Inicia PostgreSQL + App en Docker" -ForegroundColor White
        Write-Host "  stop        - Detiene todos los servicios" -ForegroundColor White
        Write-Host "  status      - Muestra estado de los contenedores" -ForegroundColor White
        Write-Host "  logs        - Muestra logs en tiempo real" -ForegroundColor White
        Write-Host "  logs-db     - Logs de PostgreSQL" -ForegroundColor White
        Write-Host "  logs-app    - Logs de la aplicación" -ForegroundColor White
        Write-Host "  db          - Conecta a PostgreSQL (psql)" -ForegroundColor White
        Write-Host "  reset       - Reinicia todo (ELIMINA DATOS)" -ForegroundColor Red
        Write-Host "  clean       - Limpia contenedores parados`n" -ForegroundColor White
        Write-Host "Ejemplo: .\docker-commands.ps1 start`n" -ForegroundColor Yellow
    }
}
