# API de Recursos - Endpoints Públicos

## Base URL
```
http://localhost:8080/api
```

## Endpoints Disponibles

### 📋 Clientes

#### Obtener todos los clientes
```http
GET /api/clientes
```

#### Obtener cliente por ID
```http
GET /api/clientes/{id}
```

#### Crear cliente
```http
POST /api/clientes
Content-Type: application/json

{
  "nombre": "Juan",
  "apellido": "Pérez",
  "tipoDoc": "DNI",
  "nroDoc": 12345678,
  "telefono": "1123456789",
  "mail": "juan.perez@example.com"
}
```

#### Actualizar cliente
```http
PUT /api/clientes/{id}
Content-Type: application/json

{
  "nombre": "Juan",
  "apellido": "Pérez",
  "tipoDoc": "DNI",
  "nroDoc": 12345678,
  "telefono": "1123456789",
  "mail": "juan.perez@example.com"
}
```

---

### 🚛 Camiones

#### Obtener todos los camiones
```http
GET /api/camiones
```

#### Obtener camión por dominio
```http
GET /api/camiones/{dominio}
```

#### Crear camión
```http
POST /api/camiones
Content-Type: application/json

{
  "dominioCamion": "ABC123",
  "capacidadPeso": 10000.0,
  "capacidadVolumen": 50.0,
  "disponibilidad": true,
  "nombreTransportista": "Pedro López",
  "telefonoTransportista": "1156789012",
  "costoBaseKm": 5.5,
  "consumoLKm": 0.35
}
```

#### Actualizar camión
```http
PUT /api/camiones/{dominio}
Content-Type: application/json

{
  "dominioCamion": "ABC123",
  "capacidadPeso": 10000.0,
  "capacidadVolumen": 50.0,
  "disponibilidad": true,
  "nombreTransportista": "Pedro López",
  "telefonoTransportista": "1156789012",
  "costoBaseKm": 5.5,
  "consumoLKm": 0.35
}
```

---

### 🏢 Depósitos

#### Obtener todos los depósitos
```http
GET /api/depositos
```

#### Obtener depósito por ID
```http
GET /api/depositos/{id}
```

#### Crear depósito
```http
POST /api/depositos
Content-Type: application/json

{
  "latitudDeposito": -34.603722,
  "longitudDeposito": -58.381592,
  "calleDeposito": "Av. Corrientes",
  "nroDeposito": 1234,
  "costoDiaTransportista": 500.0
}
```

#### Actualizar depósito
```http
PUT /api/depositos/{id}
Content-Type: application/json

{
  "latitudDeposito": -34.603722,
  "longitudDeposito": -58.381592,
  "calleDeposito": "Av. Corrientes",
  "nroDeposito": 1234,
  "costoDiaTransportista": 500.0
}
```

---

### 📦 Contenedores

#### Obtener todos los contenedores
```http
GET /api/contenedores
```

#### Obtener contenedor por ID
```http
GET /api/contenedores/{id}
```

#### Crear contenedor
```http
POST /api/contenedores
Content-Type: application/json

{
  "peso": 100.00,
  "volumen": 50.00,
  "idEstadoContenedor": 1,
  "clienteAsociado": "Transportes ABC",
  "fechaCreacion": "2025-11-10T18:42:50",
  "idCliente": 1
}
```

#### Actualizar contenedor
```http
PUT /api/contenedores/{id}
Content-Type: application/json

{
  "peso": 100.00,
  "volumen": 50.00,
  "idEstadoContenedor": 1,
  "clienteAsociado": "Transportes ABC",
  "fechaCreacion": "2025-11-10T18:42:50",
  "idCliente": 1
}
```

---

### 💰 Tarifas

#### Obtener todas las tarifas
```http
GET /api/tarifas
```

#### Obtener tarifa por ID
```http
GET /api/tarifas/{id}
```

#### Crear tarifa
```http
POST /api/tarifas
Content-Type: application/json

{
  "descripcion": "Tarifa Estándar 2025",
  "valor": 100.0,
  "costoKmBase": 8.5,
  "valorLitroCombustible": 150.0,
  "consumoPromedio1Km": 30.0
}
```

#### Actualizar tarifa
```http
PUT /api/tarifas/{id}
Content-Type: application/json

{
  "descripcion": "Tarifa Estándar 2025",
  "valor": 100.0,
  "costoKmBase": 8.5,
  "valorLitroCombustible": 150.0,
  "consumoPromedio1Km": 30.0
}
```

---

### 📊 Rangos de Tarifa

#### Obtener todos los rangos de tarifa
```http
GET /api/tarifa-rangos
```

#### Obtener rango de tarifa por ID
```http
GET /api/tarifa-rangos/{id}
```

#### Crear rango de tarifa
```http
POST /api/tarifa-rangos
Content-Type: application/json

{
  "minPesoKg": 0.0,
  "maxPesoKg": 1000.0,
  "minVolumenM3": 0.0,
  "maxVolumenM3": 50.0,
  "factorCamion": 1.5,
  "idTarifa": 1
}
```

#### Actualizar rango de tarifa
```http
PUT /api/tarifa-rangos/{id}
Content-Type: application/json

{
  "minPesoKg": 0.0,
  "maxPesoKg": 1000.0,
  "minVolumenM3": 0.0,
  "maxVolumenM3": 50.0,
  "factorCamion": 1.5,
  "idTarifa": 1
}
```

---

## Uso desde el Microservicio de Logística

### Ejemplo en un servicio de logística

```java
@Service
@AllArgsConstructor
public class MiServicio {

    private final RecursosClient recursosClient;

    public void ejemploDeUso() {
        // Obtener todos los camiones
        List<CamionDTO> camiones = recursosClient.obtenerTodosLosCamiones(CamionDTO.class);
        
        // Obtener un camión específico
        CamionDTO camion = recursosClient.obtenerCamionPorDominio("ABC123", CamionDTO.class);
        
        // Obtener todos los depósitos
        List<DepositoDTO> depositos = recursosClient.obtenerTodosLosDepositos(DepositoDTO.class);
        
        // Obtener una tarifa
        TarifaDTO tarifa = recursosClient.obtenerTarifaPorId(1, TarifaDTO.class);
    }
}
```

### Ejemplo usando RecursosIntegrationService

```java
@Service
@AllArgsConstructor
public class MiServicio {

    private final RecursosIntegrationService recursosService;

    public void ejemploDeUso() {
        // Obtener camiones disponibles
        List<CamionDTO> camiones = recursosService.obtenerCamionesDisponibles();
        
        // Obtener depósitos
        List<DepositoDTO> depositos = recursosService.obtenerDepositos();
        
        // Obtener tarifas
        List<TarifaDTO> tarifas = recursosService.obtenerTarifas();
    }
}
```

---

## Configuración

### Puertos
- **Recursos**: `http://localhost:8080`
- **Logística**: `http://localhost:8081`

### CORS
El microservicio de recursos está configurado para permitir requests desde:
- `http://localhost:8081` (logística)
- `http://127.0.0.1:8081` (logística)

### Timeout
- **Connect Timeout**: 5 segundos
- **Read Timeout**: 5 segundos

---

## Códigos de Estado HTTP

- `200 OK`: Operación exitosa
- `201 Created`: Recurso creado exitosamente
- `204 No Content`: Eliminación exitosa
- `400 Bad Request`: Datos inválidos
- `404 Not Found`: Recurso no encontrado
- `500 Internal Server Error`: Error del servidor

---

## Manejo de Errores

El cliente incluye manejo de errores básico. En caso de error, se lanzará una `RuntimeException` con un mensaje descriptivo.

```java
try {
    CamionDTO camion = recursosService.obtenerCamionPorDominio("ABC123");
} catch (RuntimeException e) {
    log.error("Error al obtener camión: {}", e.getMessage());
    // Manejar el error según tu lógica de negocio
}
```
