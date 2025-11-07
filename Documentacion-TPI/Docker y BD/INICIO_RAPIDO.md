# Resumen de la configuración

## ✅ Qué se ha hecho

1. **docker-compose.yml** - Configuración completa para:
   - PostgreSQL 15 con volumen persistente
   - Spring Boot App (opcional)
   - Networking automático entre servicios
   - Health check para PostgreSQL

2. **Dockerfile** - Multi-stage build para:
   - Compilar el proyecto Maven
   - Ejecutar la app en Java 21

3. **.dockerignore** - Excluye archivos innecesarios del build

4. **.env** - Variables de entorno centralizadas

5. **application.yml** - YA ESTÁ CONFIGURADO CORRECTAMENTE con:
   - `ddl-auto: update` ✅ (Hibernate crea/actualiza tablas automáticamente)
   - PostgreSQL dialect ✅
   - Conexión a `jdbc:postgresql://localhost:5432/logistica_db`

## 🚀 Cómo empezar AHORA

### Paso 1: Abre PowerShell en la carpeta del proyecto
```powershell
cd "c:\Users\camil\Documents\2025\BACKEND\TPI\Backend-Grupo46"
```

### Paso 2: Levanta solo la BD (RECOMENDADO para desarrollo)
```powershell
docker-compose up postgres -d
```

### Paso 3: Ejecuta la app localmente
```powershell
cd logistica
mvn spring-boot:run
```

## ¿Cómo funciona?

1. Docker levanta PostgreSQL
2. Spring Boot se conecta automáticamente
3. Hibernate detecta tus `@Entity` classes
4. Crea las tablas automáticamente en la BD
5. En los logs verás: `Hibernate: CREATE TABLE...`

## Verificar que funciona

### Opción A: Swagger (más fácil)
```
http://localhost:8081/swagger-ui.html
```

### Opción B: Conectar a PostgreSQL desde PowerShell
```powershell
docker-compose exec postgres psql -U postgres -d logistica_db
```

Luego:
```sql
\dt  -- muestra todas las tablas creadas
SELECT table_name FROM information_schema.tables WHERE table_schema='public';
```

## Si algo no funciona

1. **Verifica que Docker Desktop está ejecutándose**
2. **Ver logs de PostgreSQL:**
   ```powershell
   docker-compose logs postgres
   ```
3. **Ver logs de la app:**
   ```powershell
   docker-compose logs logistica-app
   ```
4. **Reiniciar todo:**
   ```powershell
   docker-compose down
   docker-compose up postgres -d
   ```

## Próximos pasos

1. Verifica que tu código tiene clases con `@Entity`
2. Ejecuta la app
3. Las tablas se crearán automáticamente
4. Listo para desarrollar! 🎉

Ver archivo `DOCKER_GUIA.md` para más detalles.
