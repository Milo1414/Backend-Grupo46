# API REST - Guía Rápida para Desarrolladores

## 🚀 Inicio Rápido

### Iniciar el servidor

```bash
cd logistica
mvn spring-boot:run
```

**URL Local:** `http://localhost:8081`  
**Swagger UI:** `http://localhost:8081/swagger-ui.html`

---

## 📊 Endpoints Principales

### Solicitudes

```bash
# Crear nueva solicitud
POST /api/v1/solicitudes
{
  "clienteId": 1,
  "contenedorId": 100
}

# Obtener solicitud
GET /api/v1/solicitudes/1

# Solicitudes por cliente
GET /api/v1/solicitudes/cliente/1

# Solicitudes por estado
GET /api/v1/solicitudes/estado/PROGRAMADA

# Historial de cambios
GET /api/v1/solicitudes/1/historial

# Programar solicitud
PUT /api/v1/solicitudes/1/programar
{
  "costoEstimado": 500.50,
  "tiempoEstimadoHoras": 12.5
}

# Finalizar entrega
PUT /api/v1/solicitudes/1/entregar
{
  "costoFinal": 485.00,
  "tiempoRealHoras": 11.75
}
```

### Rutas

```bash
# Crear ruta
POST /api/v1/rutas
{
  "solicitudId": 1,
  "tramos": [
    {
      "origen": "Depósito",
      "destino": "Cliente",
      "tipo": "ENTREGA",
      "costoAproximado": 250.00,
      "distanciaKm": 45.5,
      "tiempoEstimadoHoras": 2.5
    }
  ]
}

# Obtener ruta
GET /api/v1/rutas/1
```

### Tramos

```bash
# Obtener tramo
GET /api/v1/tramos/1

# Tramos de una ruta
GET /api/v1/tramos/ruta/1

# Tramos de un camión
GET /api/v1/tramos/camion/5

# Asignar camión
PUT /api/v1/tramos/1/asignar-camion
{
  "camionId": 5
}

# Iniciar tramo
PUT /api/v1/tramos/1/iniciar
{
  "fechaHoraInicio": "2025-11-06T10:30:00"
}

# Finalizar tramo
PUT /api/v1/tramos/1/finalizar
{
  "fechaHoraFin": "2025-11-06T13:15:00",
  "costoReal": 245.75
}
```

### Cambios de Estado (Auditoría)

```bash
# Obtener cambio
GET /api/v1/cambios-estado/1

# Cambios por estado
GET /api/v1/cambios-estado/estado/ENTREGADA
```

---

## 🔄 Flujo Típico de Negocio

```
1. POST /api/v1/solicitudes
   → Crea solicitud en estado BORRADOR

2. PUT /api/v1/solicitudes/{id}/programar
   → Transición a PROGRAMADA

3. POST /api/v1/rutas
   → Crea ruta con tramos para la solicitud

4. PUT /api/v1/tramos/{id}/asignar-camion
   → Asigna transporte

5. PUT /api/v1/tramos/{id}/iniciar
   → Marca inicio de recorrido

6. PUT /api/v1/tramos/{id}/finalizar
   → Registra fin del tramo

7. PUT /api/v1/solicitudes/{id}/entregar
   → Finaliza solicitud (ENTREGADA)

8. GET /api/v1/solicitudes/{id}/historial
   → Auditoría de cambios
```

---

## 📝 Códigos HTTP

| Código | Significado |
|--------|-------------|
| 200 | OK - Operación exitosa |
| 201 | Created - Recurso creado |
| 400 | Bad Request - Datos inválidos |
| 404 | Not Found - Recurso no existe |
| 422 | Unprocessable Entity - Validación fallida |
| 500 | Server Error - Error interno |

---

## 🛠️ Herramientas Útiles

### curl

```bash
# GET
curl http://localhost:8081/api/v1/solicitudes/1

# POST
curl -X POST http://localhost:8081/api/v1/solicitudes \
  -H "Content-Type: application/json" \
  -d '{"clienteId": 1, "contenedorId": 100}'

# PUT
curl -X PUT http://localhost:8081/api/v1/solicitudes/1/programar \
  -H "Content-Type: application/json" \
  -d '{"costoEstimado": 500.50, "tiempoEstimadoHoras": 12.5}'
```

### Postman

1. Importar colección desde Swagger: `http://localhost:8081/v3/api-docs`
2. O crear manual en Postman

### httpie

```bash
http GET http://localhost:8081/api/v1/solicitudes/1

http POST http://localhost:8081/api/v1/solicitudes \
  clienteId:=1 contenedorId:=100
```

---

## 📚 Documentación Interactiva

**URL:** `http://localhost:8081/swagger-ui.html`

Aquí puedes:
- Ver documentación completa de cada endpoint
- Probar endpoints directamente
- Ver ejemplos de request/response
- Descargar especificación OpenAPI

---

## 🐛 Troubleshooting

### "Connection refused"

```bash
# Verificar que PostgreSQL está corriendo
psql -U logistica_user -d logistica_db

# Verificar que la BD existe
createdb -U logistica_user logistica_db
```

### "Mangled response" en Swagger

→ Refrescar navegador o limpiar caché

### Errores de compilación

```bash
mvn clean compile -U
# -U fuerza descarga de dependencias nuevas
```

---

## 🔗 Integración con otros Microservicios

### ms-recursos (puerto 8080)

La API permite CORS desde `http://localhost:8080`

```javascript
// Ejemplo en JavaScript/Node.js
fetch('http://localhost:8081/api/v1/solicitudes', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    clienteId: 1,
    contenedorId: 100
  })
})
.then(r => r.json())
.then(data => console.log(data))
```

---

## 📊 Estados de Solicitud

```
BORRADOR ──→ PROGRAMADA ──→ EN_TRANSITO ──→ ENTREGADA
  (Inicial)   (Asignación)   (Ejecución)    (Final)
```

---

## 💾 Variables de Configuración

En `application.properties`:

```properties
# Puerto
server.port=8081

# Base de datos
spring.datasource.url=jdbc:postgresql://localhost:5432/logistica_db
spring.datasource.username=logistica_user
spring.datasource.password=logistica_pwd

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# Logging
logging.level.root=INFO
logging.level.tpi_grupo46.logistica=DEBUG
```

---

## 🎯 Checklist de Desarrollo

- [ ] BD PostgreSQL creada
- [ ] application.properties configurado
- [ ] `mvn clean compile` ejecutado exitosamente
- [ ] Servidor iniciado con `mvn spring-boot:run`
- [ ] Swagger UI accesible en `http://localhost:8081/swagger-ui.html`
- [ ] Endpoints probados con curl o Postman
- [ ] Logs muestran operaciones correctamente

---

**Versión:** 1.0  
**Última actualización:** 2025-11-06
