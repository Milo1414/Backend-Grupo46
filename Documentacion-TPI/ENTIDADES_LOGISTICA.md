# 📋 Documentación de Entidades - Microservicio Logística

## 📊 Descripción General

El microservicio **logística** contiene la lógica operativa completa del sistema de transporte de contenedores. Las entidades modelan el proceso de:

- ✅ Solicitud de transporte
- ✅ Asignación de rutas
- ✅ Gestión de tramos (segmentos de la ruta)
- ✅ Seguimiento de estados de entrega
- ✅ Historial de cambios de estado

---

## 🏗️ Estructura de Carpetas

```
logistica/
├── src/main/java/tpi_grupo46/logistica/
│   ├── LogisticaApplication.java              # Clase principal de Spring Boot
│   ├── domain/
│   │   ├── enums/
│   │   │   └── EstadoSolicitud.java          # Estados posibles de una solicitud
│   │   └── model/
│   │       ├── Solicitud.java                # Entidad central (Solicitud de transporte)
│   │       ├── Ruta.java                     # Entidad que agrupa tramos
│   │       ├── Tramo.java                    # Segmento individual de la ruta
│   │       └── CambioEstado.java             # Historial de cambios de estado
│   ├── application/                          # Servicios de aplicación
│   ├── api/                                  # Controllers REST
│   ├── dto/                                  # Data Transfer Objects
│   ├── infrastructure/
│   │   ├── repository/                       # Interfaces JPA Repository
│   │   └── client/                           # Clientes para otros microservicios
│   └── mapper/                               # Mapeos de entidades a DTOs
└── src/main/resources/
    └── application.properties                 # Configuración de la aplicación
```

---

## 🔍 Detalle de Entidades

### 1️⃣ **EstadoSolicitud (Enum)**

**Ubicación:** `domain/enums/EstadoSolicitud.java`

Estados posibles de una solicitud:

```java
public enum EstadoSolicitud {
    BORRADOR,        // Solicitud creada pero no confirmada
    PROGRAMADA,      // Solicitud confirmada y ruta asignada
    EN_TRANSITO,     // Entrega en camino
    ENTREGADA,       // Entrega completada
    CANCELADA        // Solicitud cancelada
}
```

---

### 2️⃣ **Solicitud (Entidad Central)**

**Ubicación:** `domain/model/Solicitud.java`

**Propósito:** Representa una solicitud de transporte realizada por un cliente. Es la entidad central del microservicio.

**Atributos principales:**

| Atributo | Tipo | Descripción |
|----------|------|-------------|
| `id` | `Long` | Identificador único (autoincremental) |
| `clienteId` | `Long` | ID del cliente (referencia externa a ms-recursos) |
| `contenedorId` | `Long` | ID del contenedor a trasladar |
| `estado` | `EstadoSolicitud` | Estado actual de la solicitud |
| `costoEstimado` | `BigDecimal` | Costo estimado del transporte |
| `costoFinal` | `BigDecimal` | Costo final del transporte |
| `tiempoEstimadoHoras` | `Double` | Tiempo estimado en horas |
| `tiempoRealHoras` | `Double` | Tiempo real de entrega |
| `fechaCreacion` | `LocalDateTime` | Fecha de creación de la solicitud |

**Relaciones:**

- **OneToOne** con `Ruta` (mappedBy="solicitud") - Una solicitud tiene UNA ruta
- **OneToMany** con `CambioEstado` (mappedBy="solicitud") - Una solicitud tiene MÚLTIPLES cambios de estado

**Características especiales:**

- 📍 El estado por defecto es **BORRADOR**
- 🔔 Se registra automáticamente un `CambioEstado` inicial al crear la solicitud (mediante `@PrePersist`)
- 🗄️ La `fechaCreacion` se establece automáticamente si no se proporciona

**Tabla en BD:** `SOLICITUD`

---

### 3️⃣ **Ruta (Contenedor de Tramos)**

**Ubicación:** `domain/model/Ruta.java`

**Propósito:** Agrupa los tramos que forman la ruta completa de una solicitud, conectando origen con destino, pasando opcionalmente por uno o varios depósitos.

**Atributos:**

| Atributo | Tipo | Descripción |
|----------|------|-------------|
| `id` | `Long` | Identificador único (autoincremental) |
| `cantidadTramos` | `Integer` | Cantidad total de tramos en la ruta |
| `cantidadDepositos` | `Integer` | Cantidad de depósitos intermedios |
| `solicitud` | `Solicitud` | Referencia a la solicitud asociada |
| `tramos` | `List<Tramo>` | Lista de tramos que componen la ruta |

**Relaciones:**

- **OneToOne** con `Solicitud` - La ruta pertenece a UNA solicitud
- **OneToMany** con `Tramo` (mappedBy="ruta", cascade=ALL) - Una ruta contiene MÚLTIPLES tramos

**Tabla en BD:** `RUTA`

---

### 4️⃣ **Tramo (Segmento de Ruta)**

**Ubicación:** `domain/model/Tramo.java`

**Propósito:** Representa un segmento individual de la ruta. Cada tramo indica un recorrido entre dos puntos (origen → destino, origen → depósito, depósito → destino, etc.).

**Atributos:**

| Atributo | Tipo | Descripción |
|----------|------|-------------|
| `id` | `Long` | Identificador único |
| `origen` | `String` | Punto de partida del tramo |
| `destino` | `String` | Punto de llegada del tramo |
| `tipo` | `String` | Tipo de tramo: `origen-destino`, `origen-deposito`, `deposito-destino`, `deposito-deposito` |
| `estado` | `String` | Estado: `estimado`, `asignado`, `iniciado`, `finalizado` |
| `costoAproximado` | `BigDecimal` | Costo estimado del tramo |
| `costoReal` | `BigDecimal` | Costo real después de completar |
| `distanciaKm` | `Double` | Distancia en kilómetros |
| `tiempoEstimadoHoras` | `Double` | Tiempo estimado en horas |
| `fechaHoraInicioReal` | `LocalDateTime` | Fecha/hora real de inicio |
| `fechaHoraFinReal` | `LocalDateTime` | Fecha/hora real de finalización |
| `camionId` | `Long` | ID del camión asignado (referencia a ms-recursos) |
| `ruta` | `Ruta` | Referencia a la ruta que contiene este tramo |
| `solicitud` | `Solicitud` | Referencia a la solicitud asociada |

**Relaciones:**

- **ManyToOne** con `Ruta` - Múltiples tramos pertenecen a UNA ruta
- **ManyToOne** con `Solicitud` - Múltiples tramos pueden asociarse a UNA solicitud

**Estados del Tramo:**
- 🔵 `estimado` - Estado inicial, se ha estimado el recorrido
- 🟢 `asignado` - Se ha asignado un camión
- 🟡 `iniciado` - El camión inició el recorrido
- ✅ `finalizado` - El camión completó el recorrido

**Tabla en BD:** `TRAMO`

---

### 5️⃣ **CambioEstado (Historial de Estados)**

**Ubicación:** `domain/model/CambioEstado.java`

**Propósito:** Registra cada cambio de estado de una solicitud, permitiendo conocer la evolución del envío a lo largo del tiempo.

**Atributos:**

| Atributo | Tipo | Descripción |
|----------|------|-------------|
| `id` | `Long` | Identificador único (autoincremental) |
| `estado` | `EstadoSolicitud` | Estado registrado en ese momento |
| `fechaCambio` | `LocalDateTime` | Fecha y hora del cambio de estado |
| `solicitud` | `Solicitud` | Referencia a la solicitud |

**Relaciones:**

- **ManyToOne** con `Solicitud` - Múltiples cambios de estado pertenecen a UNA solicitud

**Características especiales:**

- 📍 Se crea automáticamente un registro inicial en estado `BORRADOR` al crear una solicitud
- ⏱️ La `fechaCambio` se establece en el momento en que se registra

**Tabla en BD:** `CAMBIO_ESTADO`

---

## 📚 Repositorios JPA

Cada entidad tiene un repositorio en `infrastructure/repository/`:

### **SolicitudRepository**
```java
- findByClienteId(Long clienteId)           // Solicitudes de un cliente
- findByEstado(EstadoSolicitud estado)      // Solicitudes con estado específico
- findByClienteIdAndEstado(...)             // Combinación de filtros
```

### **RutaRepository**
```java
- findBySolicitudId(Long solicitudId)       // Obtener ruta de una solicitud
```

### **TramoRepository**
```java
- findByRutaId(Long rutaId)                 // Tramos de una ruta específica
- findBySolicitudId(Long solicitudId)       // Tramos de una solicitud
- findByCamionId(Long camionId)             // Tramos asignados a un camión
```

### **CambioEstadoRepository**
```java
- findBySolicitudIdOrderByFechaCambioAsc(...) // Historial de cambios (ordenado)
- findByEstado(EstadoSolicitud estado)       // Cambios de un estado específico
```

---

## 🔗 Relaciones entre Entidades

```
Solicitud (1) ────── (1) Ruta
    │
    ├──────── (1..n) CambioEstado
    │                    
    └──────── (1..n) Tramo ────── (1) Ruta
```

**Flujo de datos:**

1. **Cliente crea Solicitud** → Estado: `BORRADOR`
2. **Sistema crea Ruta** → Se calculan y crean `Tramo`s
3. **Se asigna Camión a cada Tramo** → Estado: `ASIGNADO`
4. **Camión inicia recorrido** → Tramo: `INICIADO`, Solicitud: `EN_TRANSITO`
5. **Se registran CambioEstado** en cada transición
6. **Al completar todos los Tramo`s** → Solicitud: `ENTREGADA`

---

## ⚙️ Configuración de la Base de Datos

**Archivo:** `src/main/resources/application.properties`

- Base de datos: **PostgreSQL**
- BD por defecto: `logistica_db`
- Puerto de aplicación: **8081**
- DDL automático: `update` (crea/actualiza tablas)

---

## 🛠️ Anotaciones JPA Utilizadas

- `@Entity` - Marca la clase como entidad JPA
- `@Table(name = "...")` - Especifica el nombre de la tabla
- `@Id` - Identifica el atributo como PK
- `@GeneratedValue(strategy = GenerationType.IDENTITY)` - Autoincremento
- `@Column(...)` - Define propiedades de columna
- `@Enumerated(EnumType.STRING)` - Almacena enum como string
- `@OneToOne`, `@OneToMany`, `@ManyToOne` - Define relaciones
- `@JoinColumn(...)` - Especifica la columna de FK
- `@PrePersist` - Hook ejecutado antes de insertar

---

## 💡 Anotaciones Lombok Utilizadas

- `@Data` - Genera getters, setters, toString, equals, hashCode
- `@NoArgsConstructor` - Constructor sin parámetros
- `@AllArgsConstructor` - Constructor con todos los parámetros
- `@Builder` - Patrón Builder
- `@ToString.Exclude` - Excluye atributo de toString (evita ciclos)

---

## ✅ Validación de Compilación

```
[INFO] BUILD SUCCESS
[INFO] Compiling 10 source files with javac [debug parameters release 21]
[INFO] Total time: 8.993 s
```

✨ Todas las entidades se compilan correctamente con **Java 21** y **Spring Boot 3.5.7**.
