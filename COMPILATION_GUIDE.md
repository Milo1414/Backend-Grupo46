# 📋 Guía Rápida: Compilar y Ejecutar Backend-Grupo46

## 🗂️ Estructura del Proyecto

Este es un proyecto Maven multi-módulo con 2 servicios:

```
Backend-Grupo46/
├── logistica/          (Puerto: 8080, Spring Boot app)
├── recursos/           (Puerto: 8082, Spring Boot app)
└── scripts/
    ├── compile-all.ps1     ← Compila TODOS los módulos
    ├── run-logistica.ps1   ← Ejecuta módulo logistica
    └── run-recursos.ps1    ← Ejecuta módulo recursos
```

## 🚀 Compilar Todos los Módulos

### Opción 1: Desde PowerShell (Recomendado)
```powershell
cd c:\Users\Usuario\OneDrive\Documentos\Facu\Tercero\Backend\TPI\Backend-Grupo46
.\compile-all.ps1
```

### Opción 2: Manualmente (desde cada módulo)
```powershell
# Compilar logistica
cd logistica
mvn clean compile

# Compilar recursos
cd ..\recursos
mvn clean compile
```

## ▶️ Ejecutar Aplicaciones

### Ejecutar Logistica (Puerto 8080)
```powershell
.\run-logistica.ps1
```

### Ejecutar Recursos (Puerto 8082)
```powershell
cd recursos
mvn spring-boot:run
```

## ⚙️ Requisitos

- ✅ **Java 17+** (verificar con `java -version`)
- ✅ **Maven 3.8.9+** (verificar con `mvn -version`)
- ✅ **PostgreSQL** (base de datos configurada)
- ✅ **Supabase** (credenciales en `application.yml`)

## 🧪 Verificar Compilación

Después de compilar, debería ver:

```
[INFO] ✅ logistica compilado exitosamente
[INFO] ✅ recursos compilado exitosamente
[INFO] ✅ Compilación completada exitosamente
```

## ❌ Solucionar Errores Comunes

### Error: "The goal you specified requires a project..."
```
❌ mvn compile                    (desde raíz)
✅ cd logistica && mvn compile    (desde módulo)
```

### Error: "Maven not found"
```powershell
# Verificar Maven
mvn -version

# Si no existe, usar el wrapper
cd logistica
.\mvnw clean compile
```

### Error: "Schema-validation: wrong column type..."
```
✅ Fix ya aplicado: camion_id cambió de BIGINT a VARCHAR
📝 Ver: SCHEMA_VALIDATION_FIX_SUMMARY.md
🗄️  Ejecutar: FIX_CAMION_ID_TYPE.sql en la BD
```

## 📚 Documentación Adicional

- **[SCHEMA_VALIDATION_FIX_SUMMARY.md](./SCHEMA_VALIDATION_FIX_SUMMARY.md)** - Fix del error de schema validation
- **[FIX_CAMION_ID_TYPE.sql](./FIX_CAMION_ID_TYPE.sql)** - Script SQL para actualizar la BD
- **[README.md](./README.md)** - Documentación general del proyecto

## 🔗 URLs Locales

Una vez que levantes la aplicación:

- **Logistica** (8080): http://localhost:8080/api
  - Swagger: http://localhost:8080/swagger-ui.html
  - API Docs: http://localhost:8080/v3/api-docs

- **Recursos** (8082): http://localhost:8082/api
  - Swagger: http://localhost:8082/swagger-ui.html
  - API Docs: http://localhost:8082/v3/api-docs

---

**Última actualización**: 2025-11-16
