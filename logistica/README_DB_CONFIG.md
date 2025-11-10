# 🗄️ README_DB_CONFIG.md - Configuración de Base de Datos

**Proyecto**: TPI Grupo 46 - Microservicio ms-logistica  
**Versión**: 2.0  
**Fecha**: 7 de noviembre de 2025  
**Estado**: ✅ Documentado

---

## 📋 Resumen

El microservicio `ms-logistica` utiliza una **base de datos PostgreSQL compartida** con el microservicio `ms-recursos`, pero mantiene **independencia lógica** mediante la utilización de esquemas separados dentro de la misma instancia de base de datos.

---

## 🏗️ Arquitectura de Base de Datos

### Configuración Compartida
```
┌─────────────────────────────────────────────┐
│      PostgreSQL (instancia única)           │
├─────────────────────────────────────────────┤
│                                             │
│  ┌──────────────────┐  ┌──────────────────┐ │
│  │  Schema          │  │  Schema          │ │
│  │  logistica       │  │  recursos        │ │
│  │                  │  │                  │ │
│  │ ├─ solicitudes   │  │ ├─ clientes      │ │
│  │ ├─ rutas         │  │ ├─ tarifas       │ │
│  │ ├─ tramos        │  │ ├─ tarifas_rango│ │
│  │ ├─ cambios_est.. │  │ └─ contenedores  │ │
│  │ └─ etc.          │  │                  │ │
│  └──────────────────┘  └──────────────────┘ │
│                                             │
└─────────────────────────────────────────────┘
```

### Ventajas de esta Arquitectura

✅ **Independencia Lógica**: Cada microservicio tiene su propio schema, evitando conflictos de nombres  
✅ **Reutilización de Infraestructura**: Una sola instancia PostgreSQL para ambos servicios  
✅ **Mantenimiento Centralizado**: Backups, updates, monitoreo en un único punto  
✅ **Seguridad**: Es posible aplicar permisos a nivel de schema  
✅ **Escalabilidad**: Permite crecer sin duplicar infraestructura  

---

## 🔧 Configuración Inicial

### 1. Crear Esquemas

Ejecutar las siguientes sentencias SQL en PostgreSQL como administrador:

```sql
-- Crear esquema para logística
CREATE SCHEMA logistica;

-- Crear esquema para recursos
CREATE SCHEMA recursos;

-- Verificar que se crearon correctamente
\dn
```

**Salida esperada:**
```
  Name   | Owner 
---------+-------
 logistica  | postgres
 recursos   | postgres
 public     | postgres
```

### 2. Configurar Permisos (Opcional pero Recomendado)

Para mejorar la seguridad, se pueden crear usuarios específicos:

```sql
-- Crear usuario para logística
CREATE USER logistica_user WITH PASSWORD 'logistica_password';
GRANT USAGE ON SCHEMA logistica TO logistica_user;
GRANT CREATE ON SCHEMA logistica TO logistica_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA logistica TO logistica_user;

-- Crear usuario para recursos
CREATE USER recursos_user WITH PASSWORD 'recursos_password';
GRANT USAGE ON SCHEMA recursos TO recursos_user;
GRANT CREATE ON SCHEMA recursos TO recursos_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA recursos TO recursos_user;
```

---

## 📝 Configuración en la Aplicación

### Archivo: `application.yml`

El microservicio `ms-logistica` debe estar configurado para usar el schema `logistica`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/nombre_bd?currentSchema=logistica
    username: logistica_user  # o postgres si no hay usuarios separados
    password: logistica_password
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: validate  # usar 'update' solo en desarrollo
    properties:
      hibernate:
        default_schema: logistica
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

### Puntos Clave

- **`currentSchema=logistica`**: En la URL JDBC, especifica que todas las conexiones usen este schema por defecto
- **`default_schema: logistica`**: En Hibernate, asegura que las migraciones de esquema afecten solo este schema
- **`ddl-auto: validate`**: En producción, solo valida que la estructura existe (no la crea ni modifica)

---

## 🔄 Migraciones de Schema

Si se utiliza **Flyway** para migraciones (recomendado):

### Estructura de Carpetas

```
src/main/resources/
└── db/
    └── migration/
        ├── V1__init_logistica_schema.sql    (solo para logistica)
        └── V2__create_logistica_tables.sql
```

### Ejemplo: `V1__init_logistica_schema.sql`

```sql
-- Esta migración se ejecuta automáticamente por Flyway
-- Solo crea las tablas dentro del schema logistica

-- Crear tabla Solicitud
CREATE TABLE logistica.solicitudes (
    id BIGSERIAL PRIMARY KEY,
    estado VARCHAR(50) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ...
);

-- Crear tabla Ruta
CREATE TABLE logistica.rutas (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    ...
);

-- Más tablas...
```

### Configuración en Flyway

```yaml
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration
    schemas: logistica
    baseline-on-migrate: true
    baseline-version: 1.0
```

---

## 🧪 Validación de Configuración

### Verificar que la Configuración es Correcta

Ejecutar el siguiente script SQL para confirmar que todo está en su lugar:

```sql
-- Conectarse a la BD
\c nombre_bd

-- Cambiar al schema logistica
SET search_path TO logistica;

-- Listar tablas creadas
\dt

-- Verificar que se ve tabla de solicitudes
SELECT * FROM solicitudes LIMIT 1;
```

### Desde la Aplicación

Una vez que el microservicio está corriendo, verificar los logs:

```
2025-11-07 10:15:23 INFO  : Schema creation with Hibernate completed
2025-11-07 10:15:24 INFO  : HikariPool-1 - is starting.
```

Si aparecen mensajes sin errores, la configuración es correcta.

---

## 🌉 Comunicación entre Microservicios

### Caso de Uso: Logística necesita datos de Recursos

**Escenario**: Al crear una Solicitud, se necesita validar si existe un Cliente en `ms-recursos`

```
┌──────────────────────┐         HTTP/REST       ┌──────────────────────┐
│  ms-logistica        │ ─────────────────────►  │  ms-recursos         │
│  Schema: logistica   │ (Consulta cliente ID)   │  Schema: recursos    │
│                      │ ◄─────────────────────  │                      │
│                      │   (JSON con datos)      │                      │
└──────────────────────┘                         └──────────────────────┘
```

**Nunca acceder directamente a la BD de otro microservicio**: Usar APIs REST.

---

## 📊 Monitoreo y Mantenimiento

### Queries Útiles para DBA

#### Ver uso de espacio por schema
```sql
SELECT 
    schemaname,
    pg_size_pretty(pg_total_relation_size(schemaname::regnamespace)) AS tamaño
FROM pg_tables
GROUP BY schemaname
ORDER BY pg_total_relation_size(schemaname::regnamespace) DESC;
```

#### Listar conexiones activas por schema
```sql
SELECT usename, datname, count(*) 
FROM pg_stat_activity 
GROUP BY usename, datname;
```

#### Hacer backup solo del schema logistica
```bash
pg_dump \
  --schema=logistica \
  --username=postgres \
  --host=localhost \
  --port=5432 \
  nombre_bd > backup_logistica_2025-11-07.sql
```

---

## 🔐 Seguridad y Buenas Prácticas

### ✅ Recomendaciones

1. **Separar credenciales por ambiente**: Dev, staging, prod deben tener usuarios diferentes
2. **No usar usuario `postgres`**: Crear usuarios específicos con permisos limitados
3. **SSL en conexiones**: En producción, usar `sslmode=require` en la URL JDBC
4. **Auditoría**: Habilitar logs de acceso a base de datos
5. **Backups regulares**: Automatizar backups a nivel de schema

### ⚠️ Lo que NO hacer

- ❌ No acceder directamente a schema `recursos` desde `ms-logistica`
- ❌ No compartir credenciales de base de datos entre ambientes
- ❌ No usar `ddl-auto: create-drop` en producción
- ❌ No hardcodear credenciales en el código

---

## 🚀 Configuración para Diferentes Ambientes

### Desarrollo (`application-dev.yml`)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/tpi_grupo46_dev?currentSchema=logistica
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update  # Permitir cambios en esquema
```

### Producción (`application-prod.yml`)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://prod-db.example.com:5432/tpi_grupo46?currentSchema=logistica&sslmode=require
    username: logistica_user
    password: ${DB_PASSWORD}  # Desde variable de entorno
  jpa:
    hibernate:
      ddl-auto: validate  # Solo validar, no modificar
    show-sql: false  # No registrar SQL
```

---

## 📚 Referencias

- [PostgreSQL Schemas Documentation](https://www.postgresql.org/docs/current/ddl-schemas.html)
- [Hibernate ORM Guide](https://hibernate.org/orm/)
- [Spring Data JPA Configuration](https://spring.io/projects/spring-data-jpa)
- [Flyway Migrations](https://flywaydb.org/)

---

## 📞 Soporte

Si tienes dudas sobre la configuración:

1. Revisar los logs de la aplicación: `log/logistica.log`
2. Consultar la base de datos directamente: `psql -U postgres`
3. Revisar el archivo `ARQUITECTURA_DECISIONES.md` para más contexto

---

**Documento Generado**: 7 de noviembre de 2025  
**Versión**: 2.0  
**Estado**: ✅ Completado
