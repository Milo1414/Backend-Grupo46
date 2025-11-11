# Arquitectura y Decisiones de Diseño - API REST

**Documento:** Decisiones Arquitectónicas para ms-logistica  
**Versión:** 1.0  
**Fecha:** Noviembre 2025  
**Equipo:** TPI Grupo 46

---

## 1. Patrones Arquitectónicos

### 1.1 Arquitectura en Capas

Se implementó una arquitectura en capas siguiendo Clean Architecture principles:

```
┌─────────────────────────────────────┐
│         Presentación (API)          │
│  Controllers + OpenAPI Configuration│
└──────────────────┬──────────────────┘
                   │ (DTOs)
                   ↓
┌─────────────────────────────────────┐
│       Aplicación (Application)      │
│  Services + Mappers (MapStruct)     │
└──────────────────┬──────────────────┘
                   │ (Entidades)
                   ↓
┌─────────────────────────────────────┐
│       Dominio (Domain)              │
│  Entidades + Enums + Value Objects  │
└──────────────────┬──────────────────┘
                   │ (Queries)
                   ↓
┌─────────────────────────────────────┐
│    Infraestructura (Infrastructure) │
│  Repositories + Config + Clients    │
└─────────────────────────────────────┘
```

**Beneficios:**
- ✅ Separación clara de responsabilidades
- ✅ Fácil de probar unitariamente
- ✅ Independencia de frameworks
- ✅ Escalabilidad y mantenibilidad

### 1.2 REST API Design

Se siguieron REST conventions:

```
Recurso          Verbo HTTP  Significado           Estatus
─────────────────────────────────────────────────────────
/solicitudes     POST        Crear                 201
/solicitudes     GET         Listar (filtrado)     200
/solicitudes/1   GET         Obtener por ID        200
/solicitudes/1   PUT         Actualizar            200
/solicitudes/1   DELETE      Eliminar (futuro)     204
/solicitudes     PATCH       Actualización parcial (futuro)
```

**URL Design:**
- ✅ Versioning: `/api/v1/` permite futuros cambios sin breaking
- ✅ Recursos en plural: `/solicitudes`, `/tramos`, `/rutas`
- ✅ Acciones en operaciones: `/solicitudes/{id}/programar`
- ✅ Jerarquías de sub-recursos: `/solicitudes/{id}/historial`

### 1.3 Madurez de Richardson (Richardson Maturity Model)

```
Nivel 0: POX (Plain Old XML)
  - Único endpoint
  - Métodos personalizados

Nivel 1: Recursos
  - Múltiples endpoints
  - Un verbo HTTP

Nivel 2: HTTP Verbs ← IMPLEMENTADO
  - Múltiples endpoints
  - Verbos HTTP apropiados (GET, POST, PUT)
  
Nivel 3: HATEOAS (Hypermedia)
  - Links en respuestas
  - Cliente-driven navigation (futuro)
```

---

## 2. Decisiones Técnicas

### 2.1 MapStruct vs Alternativas

| Característica | MapStruct | ModelMapper | Manual |
|---|---|---|---|
| Compilación | **Tiempo de compilación** | Runtime | N/A |
| Performance | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| Type-safety | ✅ | ❌ | ✅ |
| Debugging | ✅ Fácil | ❌ Complejo | ✅ Muy fácil |
| Configuración | Mínima | Mínima | Manual |

**Decisión:** MapStruct
- ✅ Mejor performance (código generado)
- ✅ Type-safe (errores en compilación)
- ✅ Verificable (código generado visible)

### 2.2 DTOs vs Entidades Directas

**Opción 1: Retornar entidades directamente**
```java
@GetMapping("/{id}")
public Solicitud obtenerSolicitud(Long id) {
    return repository.findById(id);
}
```

❌ **Problemas:**
- Circular references (Solicitud → Ruta → Tramos → Solicitud)
- Lazy loading exceptions
- Exposición de campos internos
- Fuerte acoplamiento cliente-servidor

**Opción 2: DTOs estructurados (IMPLEMENTADO)**
```java
@GetMapping("/{id}")
public ResponseEntity<SolicitudDTO> obtenerSolicitud(Long id) {
    return ResponseEntity.ok(
        mapper.solicitudToDto(repository.findById(id))
    );
}
```

✅ **Ventajas:**
- Independencia de estructura de entidades
- Control explícito de qué se expone
- Prevención de circular references
- Validación específica del API

### 2.3 Global Exception Handler vs Try-Catch Local

**Opción 1: Try-catch en cada método**
```java
@GetMapping("/{id}")
public ResponseEntity<SolicitudDTO> obtenerSolicitud(Long id) {
    try {
        var solicitud = repository.findById(id);
        return ResponseEntity.ok(mapper.solicitudToDto(solicitud));
    } catch (EntityNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}
```

❌ **Problemas:**
- Código repetitivo
- Inconsistencia en manejo de errores
- Difícil de mantener

**Opción 2: GlobalExceptionHandler (IMPLEMENTADO)**
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException e) {
        return ResponseEntity.status(404)
            .body(new ErrorResponse(...));
    }
}
```

✅ **Ventajas:**
- Código limpio
- Consistencia centralizada
- Logging unificado
- DRY principle

### 2.4 Configuración CORS: Permitiva vs Restrictiva

**Opción 1: CORS completamente abierto**
```java
.allowedOrigins("*")  // ❌ Inseguro
```

**Opción 2: CORS restrictivo (IMPLEMENTADO)**
```java
.allowedOrigins(
    "http://localhost:3000",
    "http://localhost:4200",
    "http://localhost:8080",
    "http://localhost:8081"
)
```

✅ **Razones:**
- Seguridad: Solo orígenes conocidos
- Desarrollo: Permite localhost
- Producción: Fácil actualización

---

## 3. Decisiones de Modelado de Datos

### 3.1 Estados de Solicitud: Enum vs Tabla

**Opción 1: Estados en tabla**
```sql
CREATE TABLE estado (
    id BIGINT PRIMARY KEY,
    nombre VARCHAR(50)
);
```

❌ **Problemas:**
- Query adicional para cada estado
- Riesgo de inconsistencia de datos
- Estados cambian raramente

**Opción 2: Enum Java (IMPLEMENTADO)**
```java
@Enumerated(EnumType.STRING)
private EstadoSolicitud estado;
```

✅ **Ventajas:**
- Type-safety en código
- Sin queries adicionales
- Valores controlados
- Cambios raros → cambio de código

### 3.2 Relación Solicitud ↔ Ruta: OneToOne vs OneToMany

**Análisis de negocio:**
- Una solicitud → Una entrega → Una ruta
- Una ruta → Múltiples tramos
- Una solicitud → Múltiple historial de cambios

**Decisión: OneToOne**
```java
@OneToOne(mappedBy = "solicitud", cascade = CascadeType.ALL)
private Ruta ruta;

@OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL)
private List<CambioEstado> cambios;
```

✅ **Justificación:**
- Relación explícita en modelo de negocio
- Evita queries complejas
- Cascade automático

### 3.3 CambioEstado: Auditoría Explícita

**Por qué separar tabla de auditoría:**

```java
public class CambioEstado {
    @Id @GeneratedValue
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado;
    
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaCambio;
}
```

✅ **Ventajas:**
- Historial completo inmutable
- Trazabilidad legal/auditoría
- Análisis de tiempos
- Debugging facilitado

---

## 4. Decisiones de API

### 4.1 Respuestas: Wrapper vs Directo

**Opción 1: Wrapper JSON**
```json
{
  "success": true,
  "data": { "id": 1, "estado": "PROGRAMADA" },
  "message": null
}
```

❌ **Problemas:**
- Verbosidad innecesaria
- Inconsistencia con REST estándar
- Clientes web esperan directamente el recurso

**Opción 2: Respuesta directa (IMPLEMENTADO)**
```json
{
  "id": 1,
  "estado": "PROGRAMADA",
  "clienteId": 10,
  ...
}
```

✅ **Ventajas:**
- HTTP status codes hablan por sí solos
- Coherente con REST standards
- Menos bytes transferidos

### 4.2 Paginación: Presente vs Ausente

**Estado actual:** Listas sin paginación
```java
@GetMapping("/cliente/{clienteId}")
public ResponseEntity<List<SolicitudDTO>> obtenerSolicitudesPorCliente(Long clienteId)
```

📋 **Futuro (recomendación):**
```java
@GetMapping("/cliente/{clienteId}")
public ResponseEntity<Page<SolicitudDTO>> obtenerSolicitudesPorCliente(
    Long clienteId,
    @PageableDefault(size = 20) Pageable pageable
)
```

**Razón:** Mejora performance con muchos registros

### 4.3 Versionado de API

**Estrategia: URL path versioning**
```
/api/v1/solicitudes     ← Current
/api/v2/solicitudes     ← Future (incompatible)
```

✅ **Ventajas:**
- Claro para clientes
- Fácil de deprecate
- Múltiples versiones simultáneas

---

## 5. Decisiones de Seguridad (Futuro)

### 5.1 Autenticación Planeada

**Opción recomendada: JWT + Spring Security**

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .requestMatchers("/api/v1/solicitudes").hasRole("CLIENTE")
                .requestMatchers("/api/v1/tramos/**/iniciar").hasRole("TRANSPORTISTA")
                .anyRequest().authenticated()
            .and()
            .oauth2ResourceServer()
                .jwt();
        return http.build();
    }
}
```

**Roles previstos:**
- `CLIENTE`: Crear solicitudes
- `OPERADOR`: Programar rutas
- `TRANSPORTISTA`: Ejecutar tramos
- `ADMIN`: Acceso total

### 5.2 Validación (Futuro Cercano)

```java
public record CrearSolicitudDTO(
    @NotNull(message = "clienteId es requerido")
    @Positive(message = "clienteId debe ser positivo")
    Long clienteId,
    
    @NotNull(message = "contenedorId es requerido")
    @Positive(message = "contenedorId debe ser positivo")
    Long contenedorId
)
```

---

## 6. Comparación: Monolito vs Microservicios

### 6.1 Arquitectura Elegida: Microservicios

```
Antes (Monolito):
┌─────────────────────────────────────┐
│     Aplicación Monolítica           │
│  ├─ Usuarios                        │
│  ├─ Logística                       │
│  ├─ Recursos                        │
│  ├─ Facturación                     │
│  └─ ...                             │
└─────────────────────────────────────┘

Ahora (Microservicios):
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│ ms-recursos  │  │ms-logistica  │  │ ms-usuarios  │
│   :8080      │  │   :8081      │  │   :8082      │
└──────────────┘  └──────────────┘  └──────────────┘
     ↑                   ↑                ↑
  [API Gateway / Load Balancer]
```

**Ventajas:**
- ✅ Escalabilidad independiente
- ✅ Deployment independiente
- ✅ Fail isolation
- ✅ Equipos independientes

**Desventajas:**
- ❌ Complejidad operacional
- ❌ Testing distribuido
- ❌ Latencia de red

---

## 7. Justificación de Tecnologías

### 7.1 Por qué Spring Boot

```
Criterio              Spring Boot    Laravel    Django    ASP.NET
─────────────────────────────────────────────────────────────────
Ecosistema            ⭐⭐⭐⭐⭐     ⭐⭐⭐⭐   ⭐⭐⭐⭐  ⭐⭐⭐⭐
Performance           ⭐⭐⭐⭐⭐     ⭐⭐⭐⭐   ⭐⭐⭐⭐  ⭐⭐⭐⭐⭐
Java Ecosystem        ⭐⭐⭐⭐⭐     ❌        ❌       ❌
ORM maduro            ⭐⭐⭐⭐⭐     ⭐⭐⭐⭐   ⭐⭐⭐⭐  ⭐⭐⭐⭐
Seguridad             ⭐⭐⭐⭐⭐     ⭐⭐⭐     ⭐⭐⭐⭐  ⭐⭐⭐⭐⭐
```

✅ **Decisión:** Spring Boot 3.5.7
- Java 21 support
- Spring Data JPA
- Spring Web
- Spring Security (futuro)
- Ecosistema maduro

### 7.2 Por qué PostgreSQL

```
Criterio              PostgreSQL    MySQL      MongoDB   SQL Server
─────────────────────────────────────────────────────────────────
ACID Compliance       ✅ Nativo     ✅ Nativo  ⚠️ Reciente  ✅ Nativo
Relaciones complejas  ⭐⭐⭐⭐⭐    ⭐⭐⭐⭐   ❌          ⭐⭐⭐⭐⭐
JSON support          ⭐⭐⭐⭐⭐    ⭐⭐⭐    ✅ Nativo   ⭐⭐⭐⭐
Performance           ⭐⭐⭐⭐⭐    ⭐⭐⭐⭐   ⭐⭐⭐      ⭐⭐⭐⭐⭐
Open Source           ✅ Si         ✅ Si      ✅ Si       ❌ No
```

✅ **Decisión:** PostgreSQL
- Relaciones complejas (Solicitud → Ruta → Tramos)
- ACID compliance
- JSON support (futuro)
- Open source

---

## 8. Consideraciones de Performance

### 8.1 N+1 Query Problem

**Problema:**
```java
List<Solicitud> solicitudes = solicitudRepository.findAll(); // 1 query
for (Solicitud s : solicitudes) {
    Ruta ruta = s.getRuta(); // N queries
}
```

**Solución con Fetch:**
```java
@Query("SELECT s FROM Solicitud s JOIN FETCH s.ruta WHERE s.estado = :estado")
List<Solicitud> findByEstadoWithRuta(EstadoSolicitud estado);
```

**Implementación actual:**
- ✅ DTOs minimizan datos transferidos
- ✅ Lazy loading previene problemas
- 📋 Futuro: Fetch optimizado

### 8.2 Indexación de Base de Datos

**Recomendaciones de índices:**

```sql
-- Búsquedas frecuentes
CREATE INDEX idx_solicitud_cliente ON solicitud(cliente_id);
CREATE INDEX idx_solicitud_estado ON solicitud(estado);
CREATE INDEX idx_tramo_ruta ON tramo(ruta_id);
CREATE INDEX idx_tramo_camion ON tramo(camion_id);

-- Ordenamientos frecuentes
CREATE INDEX idx_cambio_estado_fecha ON cambio_estado(fecha_cambio DESC);
```

---

## 9. Mantenibilidad y Extensibilidad

### 9.1 Principios SOLID

| Principio | Implementación |
|-----------|---|
| **S**ingle Responsibility | Controllers, Services, Repositories tienen responsabilidades únicas |
| **O**pen/Closed | Nuevos estados sin modificar código existente (Enum) |
| **L**iskov Substitution | Interfaces bien definidas |
| **I**nterface Segregation | DTOs específicos por endpoint |
| **D**ependency Inversion | @Autowired, @RequiredArgsConstructor |

### 9.2 DRY (Don't Repeat Yourself)

- ✅ GlobalExceptionHandler: Manejo centralizado
- ✅ LogisticaMapper: Mapeo centralizado
- ✅ ErrorResponse: Estructura estándar
- ✅ Constants: Valores compartidos (futuro)

### 9.3 Testing (Futuro)

```
Unit Tests (Servicios)
    ↓
Integration Tests (Controllers + DB)
    ↓
End-to-End Tests (Docker + Full Stack)
    ↓
Load Tests (JMeter/Gatling)
```

---

## 10. Roadmap Técnico

### Fase 1: Completada ✅
- ✅ Entidades JPA
- ✅ Repositories
- ✅ Services
- ✅ REST Controllers
- ✅ Mappers
- ✅ Exception Handling
- ✅ OpenAPI/Swagger

### Fase 2: Próxima (Seguridad)
- 📋 Spring Security
- 📋 JWT/OAuth2
- 📋 Roles y permisos
- 📋 Validación con @Valid

### Fase 3: Testing
- 📋 JUnit 5
- 📋 Mockito
- 📋 @WebMvcTest
- 📋 TestContainers

### Fase 4: Optimización
- 📋 Caché (Redis)
- 📋 Índices de BD
- 📋 Paginación
- 📋 Batch operations

### Fase 5: Integración
- 📋 Feign Client (ms-recursos)
- 📋 Google Maps API
- 📋 Message Queue (RabbitMQ)
- 📋 Circuit Breaker (Resilience4j)

---

## 11. Alineación con Modelo de Capas de la Cátedra (Etapa 2)

### 11.1 Reorganización de la Estructura Interna

En esta segunda etapa, se alineó completamente la estructura interna del microservicio con el modelo de capas definido por la cátedra de Backend de Aplicaciones:

#### Cambios Realizados

**1. Validadores de Dominio (Domain Layer)**

```
ANTES:  domain/util/EstadoSolicitudValidator.java
AHORA:  domain/service/EstadoSolicitudValidator.java
```

**Justificación:** Los validadores de transiciones de estado son servicios de dominio que encapsulan reglas de negocio puras, no utilidades técnicas. Pertenecen a `domain/service` junto con otras reglas de negocio.

**JavaDoc Asociado:**
```java
/**
 * Servicio de dominio responsable de validar las transiciones
 * de estado permitidas entre instancias de Solicitud.
 *
 * Forma parte de la capa de dominio y define las reglas del negocio
 * asociadas al ciclo de vida de una solicitud.
 */
```

---

**2. Mappers de MapStruct (Infrastructure Layer)**

```
ANTES:  mapper/LogisticaMapper.java
AHORA:  infrastructure/mapper/LogisticaMapper.java
```

**Justificación:** Los mappers son artefactos técnicos que facilitan la transformación entre entidades y DTOs. Representan una decisión arquitectónica (usar MapStruct), no lógica de negocio, por lo que pertenecen a infraestructura.

**Beneficio:** Separa claramente qué es "técnica" (infraestructura) de qué es "negocio" (dominio/aplicación).

**JavaDoc Asociado:**
```java
/**
 * Mapper de infraestructura que realiza la conversión entre
 * entidades del dominio y DTOs de la capa API.
 * Utiliza MapStruct para simplificar el mapeo.
 */
```

---

**3. Integraciones Externas (Infrastructure Layer)**

```
NUEVO:  infrastructure/external/GoogleMapsClient.java
```

**Justificación:** Las llamadas a servicios externos (Google Maps, APIs de terceros, etc.) son detalles técnicos. Encapsularlos en infraestructura mantiene:
- Bajo acoplamiento con capas superiores
- Fácil cambio de proveedor externo
- Punto único de entrada para estas integraciones

**Estructura:**
```
infrastructure/external/
├── GoogleMapsClient.java      (Google Maps Directions API)
└── [Otros clientes externos]
```

**JavaDoc Asociado:**
```java
/**
 * Cliente de infraestructura encargado de la comunicación
 * con el servicio externo Google Maps Directions API.
 * 
 * Encapsula las llamadas HTTP y abstrae los detalles de conexión,
 * manteniendo bajo acoplamiento con la capa de aplicación.
 */
```

---

### 11.2 Estructura Final Alineada con la Cátedra

```
tpi_grupo46/logistica/
│
├── api/                          ← PRESENTACIÓN
│   ├── SolicitudController.java
│   ├── RutaController.java
│   ├── TramoController.java
│   └── CambioEstadoController.java
│
├── application/                  ← APLICACIÓN (Services)
│   ├── SolicitudService.java
│   ├── RutaService.java
│   └── TramoService.java
│
├── domain/                       ← DOMINIO (Reglas de Negocio)
│   ├── model/                    (Entidades JPA)
│   ├── enums/                    (Enumeraciones)
│   └── service/                  ← NUEVO: Validadores/Servicios de Dominio
│       └── EstadoSolicitudValidator.java  (MOVIDO aquí)
│
├── infrastructure/               ← INFRAESTRUCTURA (Técnica)
│   ├── repository/               (Acceso a datos)
│   ├── mapper/                   ← NUEVO: Mappers MapStruct
│   │   └── LogisticaMapper.java  (MOVIDO aquí)
│   ├── config/                   (Configuración Spring)
│   ├── external/                 ← NUEVO: Integraciones externas
│   │   └── GoogleMapsClient.java (NUEVO)
│   └── client/                   (Feign clients para otros microservicios)
│
├── dto/                          ← DTOs (Presentación)
│   ├── solicitud/
│   ├── ruta/
│   ├── tramo/
│   └── cambioestado/
│
├── exception/                    ← Manejo de excepciones
│   └── GlobalExceptionHandler.java
│
└── LogisticaApplication.java    ← Entry Point
```

---

### 11.3 Principios SOLID Aplicados

#### S - Single Responsibility Principle
✅ Cada clase tiene una única razón para cambiar:
- `EstadoSolicitudValidator`: Solo valida transiciones (DOMINIO)
- `LogisticaMapper`: Solo mapea entidades a DTOs (INFRAESTRUCTURA)
- `GoogleMapsClient`: Solo comunica con Google Maps (INFRAESTRUCTURA)

#### O - Open/Closed Principle
✅ Abierto a extensión, cerrado a modificación:
- Si cambia el proveedor de mapas, solo cambia `GoogleMapsClient`
- Si se agrega un nuevo mapper, se extiende la interfaz sin tocar la existente

#### L - Liskov Substitution Principle
✅ Las implementaciones pueden sustituirse sin quebrar el código:
- Los mappers (MapStruct) pueden reemplazarse con manuales sin afectar los servicios

#### I - Interface Segregation Principle
✅ Los clientes no dependen de interfaces innecesarias:
- Controllers solo inyectan lo que necesitan
- Services solo llaman a repositorios e infraestructura que usan

#### D - Dependency Inversion Principle
✅ Dependemos de abstracciones, no de concreciones:
- Services dependen de Repository (interfaz), no de implementaciones
- Controllers dependen de Service (interfaz)

---

### 11.4 Desacoplamiento por Capas

```
API (Controllers)
  ↓ DTOs
Application (Services) ← Lógica de negocio
  ↓ Entidades
Domain (Validadores/Rules) ← Reglas de negocio puro
  ↓
Infrastructure
  ├─ Repositories (DB)
  ├─ Mappers (MapStruct)
  ├─ Clients (Google Maps, ms-recursos)
  └─ Config (Spring Security, etc.)
```

**Beneficio:** Cada capa es independiente:
- Cambiar PostgreSQL por MongoDB: Afecta solo `infrastructure/repository`
- Cambiar MapStruct por manuales: Afecta solo `infrastructure/mapper`
- Cambiar Google Maps por otro proveedor: Afecta solo `infrastructure/external`

---

### 11.5 Imports Actualizados

Todos los imports fueron refactorizados para reflejar la nueva ubicación:

**En `SolicitudService.java`:**
```java
// ANTES
import tpi_grupo46.logistica.domain.util.EstadoSolicitudValidator;

// AHORA
import tpi_grupo46.logistica.domain.service.EstadoSolicitudValidator;
```

**En todos los Controllers:**
```java
// ANTES
import tpi_grupo46.logistica.mapper.LogisticaMapper;

// AHORA
import tpi_grupo46.logistica.infrastructure.mapper.LogisticaMapper;
```

---

### 11.6 Documentación Complementaria

Se agregó documentación adicional:
- **`README_DB_CONFIG.md`**: Explica la configuración de schemas PostgreSQL compartidos
- **Estructura de capas validada** en este documento

---

## 12. Conclusiones

### Decisiones Validadas

✅ **Arquitectura en capas:** Escalable y mantenible  
✅ **MapStruct:** Performance y type-safety  
✅ **DTOs:** Desacoplamiento cliente-servidor  
✅ **GlobalExceptionHandler:** Código limpio  
✅ **PostgreSQL:** Relaciones complejas  
✅ **Spring Boot 3.5.7:** Ecosistema maduro  
✅ **REST conventions:** Estándar de industria  
✅ **Enum para estados:** Type-safety  
✅ **Alineación con cátedra:** Validadores en dominio, mappers en infraestructura  
✅ **Integraciones externas encapsuladas:** GoogleMapsClient en infraestructura  

### Próximos Pasos Críticos

1. 🔐 **Autenticación/Autorización:** Spring Security
2. 🧪 **Testing:** Coverage > 80%
3. 📊 **Monitoreo:** Logs, métricas, alertas
4. 🚀 **CI/CD:** GitHub Actions / Jenkins
5. 📈 **Performance:** Load tests

---

**Documento:** Decisiones de Diseño ms-logistica  
**Status:** ✅ Arquitectura validada  
**Versión:** 1.0  
**Próxima revisión:** Después de Phase 2 (Seguridad)

