# Guía de Uso: Nuevos Endpoints REST (Etapa 2)

**Versión**: 2.0  
**Última Actualización**: 6 de noviembre de 2025  
**Estado**: ✅ Documentación completa

---

## 📖 Índice

1. [Cambios principales](#cambios-principales)
2. [Endpoints de Solicitudes](#endpoints-de-solicitudes)
3. [Endpoints de Tramos](#endpoints-de-tramos)
4. [Validación de Transiciones](#validación-de-transiciones)
5. [Ejemplos cURL](#ejemplos-curl)
6. [Migración de Código Legado](#migración-de-código-legado)

---

## 🔄 Cambios Principales

### Filosofía REST
Los endpoints ahora siguen **REST puro**: el verbo HTTP es la acción, no la URL.

```
❌ ANTES (Verbo en URL)          ✅ AHORA (REST Puro)
PUT /solicitudes/{id}/programar   PUT /solicitudes/{id}/estado/programada
PUT /tramos/{id}/asignar-camion   PUT /tramos/{id}/camion
```

---

## 📝 Endpoints de Solicitudes

### 1. Crear Solicitud
```http
POST /api/v1/solicitudes
Content-Type: application/json

{
  "clienteId": 1,
  "contenedorId": 5,
  "destino": "Puerto de Rosario"
}
```

**Respuesta (201 Created):**
```json
{
  "id": 10,
  "clienteId": 1,
  "contenedorId": 5,
  "destino": "Puerto de Rosario",
  "estado": "BORRADOR",
  "fechaCreacion": "2025-11-06T10:30:00",
  "ruta": null
}
```

### 2. Obtener Solicitud
```http
GET /api/v1/solicitudes/10
```

**Respuesta (200 OK):**
```json
{
  "id": 10,
  "clienteId": 1,
  "contenedorId": 5,
  "destino": "Puerto de Rosario",
  "estado": "BORRADOR",
  "fechaCreacion": "2025-11-06T10:30:00",
  "ruta": null
}
```

### 3. Programar Solicitud ⭐ (NUEVO - REST PURO)
```http
PUT /api/v1/solicitudes/10/estado/programada
Content-Type: application/json

{
  "rutaId": 1,
  "fechaProgramada": "2025-11-07T08:00:00"
}
```

**Cambio de estado:**
```
BORRADOR → PROGRAMADA ✅ Permitido
```

**Respuesta (200 OK):**
```json
{
  "id": 10,
  "clienteId": 1,
  "contenedorId": 5,
  "destino": "Puerto de Rosario",
  "estado": "PROGRAMADA",
  "fechaCreacion": "2025-11-06T10:30:00",
  "ruta": { "id": 1, "origen": "Buenos Aires", "destino": "Rosario" }
}
```

### 4. Entregar Solicitud ⭐ (NUEVO - REST PURO)
```http
PUT /api/v1/solicitudes/10/estado/entregada
Content-Type: application/json

{
  "observaciones": "Entrega confirmada por destinatario"
}
```

**Cambios de estado válidos:**
```
EN_TRANSITO → ENTREGADA ✅ Permitido
BORRADOR → ENTREGADA ❌ NO permitido (error 400)
```

**Respuesta (200 OK):**
```json
{
  "id": 10,
  "clienteId": 1,
  "contenedorId": 5,
  "destino": "Puerto de Rosario",
  "estado": "ENTREGADA",
  "fechaCreacion": "2025-11-06T10:30:00",
  "ruta": { "id": 1, "origen": "Buenos Aires", "destino": "Rosario" }
}
```

### 5. Obtener Historial de Cambios de Estado
```http
GET /api/v1/solicitudes/10/historial
```

**Respuesta (200 OK):**
```json
[
  {
    "id": 1,
    "solicitudId": 10,
    "estadoAnterior": "BORRADOR",
    "estadoNuevo": "PROGRAMADA",
    "fechaCambio": "2025-11-06T10:35:00",
    "observaciones": "Asignada a ruta 1"
  },
  {
    "id": 2,
    "solicitudId": 10,
    "estadoAnterior": "PROGRAMADA",
    "estadoNuevo": "EN_TRANSITO",
    "fechaCambio": "2025-11-06T11:00:00",
    "observaciones": "Tramo iniciado"
  },
  {
    "id": 3,
    "solicitudId": 10,
    "estadoAnterior": "EN_TRANSITO",
    "estadoNuevo": "ENTREGADA",
    "fechaCambio": "2025-11-06T14:30:00",
    "observaciones": "Entrega confirmada"
  }
]
```

### 6. Filtrar por Cliente
```http
GET /api/v1/solicitudes/cliente/1
```

### 7. Filtrar por Estado
```http
GET /api/v1/solicitudes/estado/PROGRAMADA
```

---

## 🚚 Endpoints de Tramos

### 1. Obtener Tramo
```http
GET /api/v1/tramos/5
```

**Respuesta:**
```json
{
  "id": 5,
  "rutaId": 1,
  "numeroTramo": 1,
  "ciudadOrigen": "Buenos Aires",
  "ciudadDestino": "La Plata",
  "distancia": 50.5,
  "estado": "PENDIENTE",
  "camionAsignado": null
}
```

### 2. Asignar Camión ⭐ (NUEVO - REST PURO)
```http
PUT /api/v1/tramos/5/camion
Content-Type: application/json

{
  "camionId": 3,
  "choferAsignado": "Juan Pérez"
}
```

**Cambio de estado:**
```
PENDIENTE → ASIGNADO ✅ Permitido
```

**Respuesta (200 OK):**
```json
{
  "id": 5,
  "rutaId": 1,
  "numeroTramo": 1,
  "ciudadOrigen": "Buenos Aires",
  "ciudadDestino": "La Plata",
  "distancia": 50.5,
  "estado": "ASIGNADO",
  "camionAsignado": {
    "id": 3,
    "patente": "AA-123-BB",
    "modelo": "Volvo FH16"
  }
}
```

### 3. Iniciar Tramo ⭐ (NUEVO - REST PURO)
```http
PUT /api/v1/tramos/5/inicio
Content-Type: application/json

{
  "lugarPartida": "Depósito Central",
  "horaSalida": "2025-11-07T08:30:00"
}
```

**Cambio de estado:**
```
ASIGNADO → EN_TRANSITO ✅ Permitido
```

**Respuesta (200 OK):**
```json
{
  "id": 5,
  "rutaId": 1,
  "numeroTramo": 1,
  "ciudadOrigen": "Buenos Aires",
  "ciudadDestino": "La Plata",
  "distancia": 50.5,
  "estado": "EN_TRANSITO",
  "camionAsignado": { "id": 3, "patente": "AA-123-BB" },
  "horaInicio": "2025-11-07T08:30:00"
}
```

### 4. Finalizar Tramo ⭐ (NUEVO - REST PURO)
```http
PUT /api/v1/tramos/5/fin
Content-Type: application/json

{
  "lugarLlegada": "Destino Final",
  "horaLlegada": "2025-11-07T11:15:00",
  "kmRecorridos": 52.3
}
```

**Cambio de estado:**
```
EN_TRANSITO → COMPLETADO ✅ Permitido
```

**Respuesta (200 OK):**
```json
{
  "id": 5,
  "rutaId": 1,
  "numeroTramo": 1,
  "ciudadOrigen": "Buenos Aires",
  "ciudadDestino": "La Plata",
  "distancia": 50.5,
  "estado": "COMPLETADO",
  "camionAsignado": { "id": 3, "patente": "AA-123-BB" },
  "horaInicio": "2025-11-07T08:30:00",
  "horaFin": "2025-11-07T11:15:00"
}
```

### 5. Filtrar por Ruta
```http
GET /api/v1/tramos/ruta/1
```

### 6. Filtrar por Camión
```http
GET /api/v1/tramos/camion/3
```

---

## 🔒 Validación de Transiciones

### Flujo de Estados Permitido

**Solicitud:**
```
┌─────────────────────────────────────────────────────┐
│  BORRADOR  →  PROGRAMADA  →  EN_TRANSITO  →  ENTREGADA  │
│    (Crear)      (Asignar)      (Iniciar)     (Finalizar) │
└─────────────────────────────────────────────────────┘
```

**Tramo:**
```
┌──────────────────────────────────────────────────────┐
│  PENDIENTE  →  ASIGNADO  →  EN_TRANSITO  →  COMPLETADO  │
│               (Asignar)      (Iniciar)        (Finalizar) │
└──────────────────────────────────────────────────────┘
```

### Respuestas de Error

**Error: Transición inválida (HTTP 400)**
```http
PUT /api/v1/solicitudes/10/estado/entregada
```

Si el estado actual es `BORRADOR` (debería ser `EN_TRANSITO`):

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Transición de estado no permitida: BORRADOR → ENTREGADA. Transiciones válidas desde BORRADOR: [PROGRAMADA]",
  "timestamp": "2025-11-06T10:40:00"
}
```

---

## 💻 Ejemplos cURL

### Flujo Completo: Crear y Procesar Solicitud

```bash
# 1. Crear solicitud (estado: BORRADOR)
curl -X POST http://localhost:8081/api/v1/solicitudes \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "contenedorId": 5,
    "destino": "Puerto de Rosario"
  }'

# Respuesta: { "id": 10, "estado": "BORRADOR", ... }

# 2. Programar solicitud (BORRADOR → PROGRAMADA)
curl -X PUT http://localhost:8081/api/v1/solicitudes/10/estado/programada \
  -H "Content-Type: application/json" \
  -d '{
    "rutaId": 1,
    "fechaProgramada": "2025-11-07T08:00:00"
  }'

# Respuesta: { "id": 10, "estado": "PROGRAMADA", ... }

# 3. Obtener historial
curl -X GET http://localhost:8081/api/v1/solicitudes/10/historial

# Respuesta: [{ "estadoAnterior": "BORRADOR", "estadoNuevo": "PROGRAMADA", ... }]

# 4. Asignar camión al tramo (PENDIENTE → ASIGNADO)
curl -X PUT http://localhost:8081/api/v1/tramos/5/camion \
  -H "Content-Type: application/json" \
  -d '{
    "camionId": 3,
    "choferAsignado": "Juan Pérez"
  }'

# 5. Iniciar tramo (ASIGNADO → EN_TRANSITO)
curl -X PUT http://localhost:8081/api/v1/tramos/5/inicio \
  -H "Content-Type: application/json" \
  -d '{
    "lugarPartida": "Depósito Central",
    "horaSalida": "2025-11-07T08:30:00"
  }'

# 6. Finalizar tramo (EN_TRANSITO → COMPLETADO)
curl -X PUT http://localhost:8081/api/v1/tramos/5/fin \
  -H "Content-Type: application/json" \
  -d '{
    "lugarLlegada": "Destino Final",
    "horaLlegada": "2025-11-07T11:15:00",
    "kmRecorridos": 52.3
  }'

# 7. Entregar solicitud (EN_TRANSITO → ENTREGADA)
curl -X PUT http://localhost:8081/api/v1/solicitudes/10/estado/entregada \
  -H "Content-Type: application/json" \
  -d '{
    "observaciones": "Entrega confirmada por destinatario"
  }'

# 8. Verificar estado final
curl -X GET http://localhost:8081/api/v1/solicitudes/10
```

---

## 🔄 Migración de Código Legado

### Endpoints Antiguos (DEPRECATED - Todavía Funcionan)

```http
# ❌ DEPRECATED - Reemplazar con nueva ruta
PUT /api/v1/solicitudes/{id}/programar
→ ✅ USE: PUT /api/v1/solicitudes/{id}/estado/programada

# ❌ DEPRECATED - Reemplazar con nueva ruta
PUT /api/v1/solicitudes/{id}/entregar
→ ✅ USE: PUT /api/v1/solicitudes/{id}/estado/entregada

# ❌ DEPRECATED - Reemplazar con nueva ruta
PUT /api/v1/tramos/{id}/asignar-camion
→ ✅ USE: PUT /api/v1/tramos/{id}/camion

# ❌ DEPRECATED - Reemplazar con nueva ruta
PUT /api/v1/tramos/{id}/iniciar
→ ✅ USE: PUT /api/v1/tramos/{id}/inicio

# ❌ DEPRECATED - Reemplazar con nueva ruta
PUT /api/v1/tramos/{id}/finalizar
→ ✅ USE: PUT /api/v1/tramos/{id}/fin
```

### Cómo Actualizar tu Código

**Antes (Legacy - Todavía funciona pero DEPRECATED):**
```java
// Cliente HTTP antiguo
restTemplate.exchange(
    "http://localhost:8081/api/v1/solicitudes/10/programar",
    HttpMethod.PUT,
    httpEntity,
    SolicitudDTO.class
);
```

**Después (Nuevo - REST Puro):**
```java
// Cliente HTTP nuevo
restTemplate.exchange(
    "http://localhost:8081/api/v1/solicitudes/10/estado/programada",
    HttpMethod.PUT,
    httpEntity,
    SolicitudDTO.class
);
```

### Advertencias del IDE

Los endpoints legacy mostrarán:
```
@Deprecated(forRemoval = true)
warning: [deprecation] programarSolicitudLegacy() in SolicitudController has been deprecated
```

Esto es **intencional** y sirve como señal para actualizar tu código.

---

## 📊 Tabla de Resumen: Antes vs Después

| Recurso | Acción | ANTES | DESPUÉS | Tipo |
|---------|--------|-------|---------|------|
| Solicitud | Crear | POST /solicitudes | POST /solicitudes | ✅ Igual |
| Solicitud | Programar | PUT /solicitudes/{id}/programar | PUT /solicitudes/{id}/estado/programada | 🔄 Actualizado |
| Solicitud | Entregar | PUT /solicitudes/{id}/entregar | PUT /solicitudes/{id}/estado/entregada | 🔄 Actualizado |
| Tramo | Asignar Camión | PUT /tramos/{id}/asignar-camion | PUT /tramos/{id}/camion | 🔄 Actualizado |
| Tramo | Iniciar | PUT /tramos/{id}/iniciar | PUT /tramos/{id}/inicio | 🔄 Actualizado |
| Tramo | Finalizar | PUT /tramos/{id}/finalizar | PUT /tramos/{id}/fin | 🔄 Actualizado |

---

## ✅ Validación de Implementación

### Verificar que todo funciona:

```bash
# 1. Iniciar el servidor
cd logistica
./mvnw.cmd spring-boot:run

# 2. Acceder a Swagger UI
open http://localhost:8081/swagger-ui.html

# 3. Ver que aparecen los nuevos endpoints:
# ✅ PUT /api/v1/solicitudes/{id}/estado/programada
# ✅ PUT /api/v1/solicitudes/{id}/estado/entregada
# ✅ PUT /api/v1/tramos/{id}/camion
# ✅ PUT /api/v1/tramos/{id}/inicio
# ✅ PUT /api/v1/tramos/{id}/fin

# 4. Probar endpoint de ejemplo
curl -X GET http://localhost:8081/api/v1/solicitudes/1
```

---

## 🆘 Troubleshooting

### Error: "Transición no permitida"
```
HTTP 400: Transición de estado no permitida: BORRADOR → ENTREGADA
```
**Solución:** Verifica que estés siguiendo el flujo correcto:
```
BORRADOR → PROGRAMADA → EN_TRANSITO → ENTREGADA
```

### Error: "Endpoint not found"
```
HTTP 404: No mapping for PUT /api/v1/solicitudes/{id}/programar
```
**Solución:** El endpoint legacy fue removido o estás usando versión anterior. Usa:
```
PUT /api/v1/solicitudes/{id}/estado/programada
```

### Error: "Validation failed"
```
HTTP 400: Validation failed: Field 'rutaId' is required
```
**Solución:** Verifica que el JSON del body tenga todos los campos requeridos.

---

**Documento Generado**: 6 de noviembre de 2025  
**Versión**: 2.0  
**Estado**: ✅ LISTO PARA USO

Para más información, ver: `REFACTORING_SEGUNDA_ETAPA_COMPLETADA.md`
