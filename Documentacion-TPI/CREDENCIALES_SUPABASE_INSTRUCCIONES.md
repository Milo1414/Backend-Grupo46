# 🔐 Instrucciones para Actualizar Credenciales Supabase

## Estado Actual ✅
- ✅ Código compilado exitosamente
- ✅ Configuración migrada de `logistica_db` a Supabase
- ✅ `application.properties` deshabilitado (renombrado a `.bak`)
- ✅ `application.yml` configurado para conectarse a Supabase
- ❌ **ERROR DE AUTENTICACIÓN**: Contraseña incorrecta o URL incompleta

## Problema Detectado
```
FATAL: la autentificación password falló para el usuario ?postgres?
```

Esto indica que:
1. La URL de conexión es correcta (llegó a la base de datos)
2. Las credenciales son **inválidas**

## Solución: Actualizar Credenciales

### Paso 1: Obtener Credenciales de Supabase
1. Ve a https://supabase.com y accede a tu proyecto
2. En el panel izquierdo, ve a **"Settings"** → **"Database"**
3. Busca la sección **"Connection string"** o **"Connection info"**
4. Verifica:
   - **Host**: `aws-1-sa-east-1.pooler.supabase.com` ✅
   - **Port**: `5432` ✅
   - **Database**: `postgres` ✅
   - **User**: Copia exactamente (ej: `postgres.jvxofmobzjyhfxhivxrl`)
   - **Password**: **COPIA LA CONTRASEÑA EXACTA** (no uses `milocatameli` si cambió)

### Paso 2: Actualizar `application.yml`

Edita `logistica/src/main/resources/application.yml` y reemplaza:

```yaml
  datasource:
    url: jdbc:postgresql://aws-1-sa-east-1.pooler.supabase.com:5432/postgres
    username: postgres.jvxofmobzjyhfxhivxrl
    password: 'CONTRASEÑA_EXACTA_AQUI'
    driver-class-name: org.postgresql.Driver
```

**⚠️ IMPORTANTE**: 
- Usa la **contraseña exacta sin caracteres especiales de URL**
- Si contiene caracteres especiales (`@`, `#`, `!`), no necesita escape en YAML si va entre comillas simples

### Paso 3: Ejecutar Spring Boot

```bash
cd logistica
mvn clean compile -DskipTests=true
mvn spring-boot:run
```

### Esperado
```
Started LogisticaApplication in X.XXX seconds
```

## Notas Técnicas

- Se renombró `application.properties` a `application.properties.bak` porque estaba conflictueando con `application.yml`
- Spring Boot ahora usa SOLO `application.yml` para la configuración
- `ddl-auto: update` está habilitado para crear/actualizar automáticamente el schema

## Archivos Modificados Esta Sesión
1. ✅ `EstadoSolicitudValidator.java` - Refactorizado a String-based
2. ✅ `PostgresDataSourceConfig.java` - Eliminado
3. ✅ `application.properties` - Renombrado a `.bak`
4. ✅ `application.yml` - Actualizado con credenciales Supabase

---

**Siguiente paso**: Proporciona la contraseña correcta de Supabase y actualizaré el archivo.
