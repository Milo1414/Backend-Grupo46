# Guía: Ejecutar Proyecto con Docker Desktop

## Requisitos previos
- Docker Desktop instalado y ejecutándose
- Git Bash o PowerShell (en tu caso PowerShell)

## Opción 1: SOLO Base de Datos (Recomendado para desarrollo)

Si quieres ejecutar solo PostgreSQL en Docker y la aplicación en tu máquina local:

### Paso 1: Navega a la carpeta del proyecto
```powershell
cd "c:\Users\camil\Documents\2025\BACKEND\TPI\Backend-Grupo46"
```

### Paso 2: Inicia solo PostgreSQL
```powershell
docker-compose up postgres -d
```

Esto levanta solo el servicio de PostgreSQL en background.

### Paso 3: Verifica que está corriendo
```powershell
docker ps
```

Deberías ver un contenedor `logistica_postgres` corriendo.

### Paso 4: Ejecuta tu aplicación Spring Boot localmente
```powershell
cd logistica
mvn spring-boot:run
```

**Ventaja:** Más rápido para desarrollo, fácil debugging desde IDE

---

## Opción 2: COMPLETA (BD + App en Docker)

Si quieres que todo corra en Docker:

### Paso 1: Navega a la carpeta del proyecto
```powershell
cd "c:\Users\camil\Documents\2025\BACKEND\TPI\Backend-Grupo46"
```

### Paso 2: Construye y levanta todo
```powershell
docker-compose up --build -d
```

Esto:
- Construye la imagen de tu app
- Levanta PostgreSQL
- Levanta tu aplicación

### Paso 3: Verifica que todo esté corriendo
```powershell
docker ps
```

Deberías ver dos contenedores: `logistica_postgres` y `logistica_app`

### Paso 4: Ver logs de la aplicación
```powershell
docker-compose logs -f logistica-app
```

---

## Comandos útiles

### Ver logs de PostgreSQL
```powershell
docker-compose logs -f postgres
```

### Ver logs de todo
```powershell
docker-compose logs -f
```

### Detener los servicios
```powershell
docker-compose down
```

### Detener y eliminar volúmenes (ELIMINA LOS DATOS)
```powershell
docker-compose down -v
```

### Reiniciar servicios
```powershell
docker-compose restart
```

### Ejecutar comando en un contenedor (ej: entrar a PostgreSQL)
```powershell
docker-compose exec postgres psql -U postgres -d logistica_db
```

---

## Verificar que todo funciona

### 1. Acceder a Swagger (API Documentation)
```
http://localhost:8081/swagger-ui.html
```

### 2. Ver logs para verificar que Hibernate creó las tablas
Busca en los logs:
```
Hibernate: CREATE TABLE...
```

### 3. Conectar a PostgreSQL directamente
```powershell
docker-compose exec postgres psql -U postgres -d logistica_db
```

Dentro de PostgreSQL:
```sql
\dt  -- Lista todas las tablas
\d nombre_tabla  -- Muestra estructura de una tabla
SELECT * FROM information_schema.tables;
```

---

## Problemas comunes

### Puerto 5432 ya está en uso
```powershell
# Cambiar el puerto en docker-compose.yml
# De: "5432:5432"
# A:  "5433:5432"
```

### La app no se conecta a la BD
1. Asegúrate de que PostgreSQL esté corriendo: `docker ps`
2. Verifica que el servicio de app espera a que postgres esté listo (healthcheck)
3. Ver logs: `docker-compose logs postgres`

### Eliminar todo y empezar de cero
```powershell
docker-compose down -v
docker-compose up --build -d
```

---

## Mi recomendación para desarrollo

1. Usa **Opción 1** (solo BD en Docker)
2. Ejecuta la app en Visual Studio Code
3. Así puedes hacer debugging y cambios más rápido

Si necesitas producción o tests finales, usa **Opción 2**.
