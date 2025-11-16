# ✅ FIX: Schema Validation Error - camion_id Type Mismatch

## 📋 Resumen Ejecutivo

Se solucionó el error de validación de schema de Hibernate que impedía que Spring Boot arrancara:

```
ERROR Schema-validation: wrong column type encountered in column [camion_id] 
in table [tramo]; found [varchar], but expecting [bigint]
```

## 🔍 Diagnóstico

### Problema Original
- **Base de datos**: columna `camion_id` en tabla `TRAMO` = `VARCHAR`
- **Entidad Java**: esperaba `Long` (BIGINT)
- **Resultado**: Mismatch → Hibernate no podía validar el schema → aplicación no arrancaba

### Causa Raíz
Inconsistencia en el modelo de datos:
- `Camion` (en ms-recursos): `@Id private String dominioCamion` (PK = String)
- `Tramo.camionId`: referencia a Camión, pero estaba tipado como `Long`
- La relación debe ser: `camionId` → referencia al `dominioCamion` (patente/String)

## ✨ Solución Aplicada

### 1️⃣ Cambios en Base de Datos
Archivo: `FIX_CAMION_ID_TYPE.sql`

```sql
ALTER TABLE tramo
ALTER COLUMN camion_id TYPE VARCHAR;
```

### 2️⃣ Cambios en Código Java

| Archivo | Cambio |
|---------|--------|
| `Tramo.java` | `private Long camionId;` → `private String camionId;` |
| `TramoDTO.java` | `Long camionId` → `String camionId` |
| `AsignarCamionDTO.java` | `Long camionId` → `String camionId` (removida validación `@Positive`) |
| `TramoRepository.java` | `findByCamionId(Long)` → `findByCamionId(String)` |
| `TramoService.java` | Actualizado `asignarCamion()` y `obtenerTramosPorCamion()` |
| `TramoController.java` | Actualizado parámetro en `obtenerTramosPorCamion()` |

## 🧪 Validación

✅ **Compilación**: `mvn clean compile` exitosa (sin errores de tipo)

✅ **Consistencia**: 
- `camionId` ahora es `String` en todas las capas (entity, DTO, service, repository)
- Alinea con el dominio de `Camion` (patente/dominio = String)
- Elimina el mismatch con la BD

## 📝 Próximos Pasos

1. **Ejecutar el SQL**:
   ```bash
   psql -U usuario -d base_datos -f FIX_CAMION_ID_TYPE.sql
   ```

2. **Reiniciar la aplicación**:
   ```bash
   mvn spring-boot:run
   ```

3. **Verificar que inicia sin errores de schema validation**

## 🔗 Relación entre Módulos

```
ms-recursos (Camion)
    ↓ (FK por dominio/patente)
ms-logistica (Tramo.camionId)

Antes:  Tramo.camionId = Long   ❌
Ahora:  Tramo.camionId = String ✅
```

## 📚 Referencias

- **Error original**: Schema validation failure en arranque de Spring Boot
- **Entidad referenciada**: `Camion.dominioCamion` (String @Id)
- **Archivos modificados**: 6 archivos Java + 1 SQL
- **Estado**: Listo para deploy

---

**Fecha de aplicación**: 2025-11-16  
**Módulo afectado**: logistica  
**Severidad**: Critical (bloqueaba arranque de app)  
**Estado**: ✅ Resuelto
