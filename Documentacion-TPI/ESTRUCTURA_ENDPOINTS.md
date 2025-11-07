# Estructura del Microservicio Recursos

## Archivos Creados

```
recursos/src/main/java/tpi_grupo46/recursos/
├── api/
│   └── ClienteController.java         # REST Endpoints para Cliente
├── application/
│   └── ClienteService.java            # Lógica de negocio para Cliente
├── domain/
│   ├── Cliente.java                   # Entidad JPA
│   ├── Contenedor.java                # Entidad JPA
│   ├── Camion.java                    # Entidad JPA
│   ├── Tarifa.java                    # Entidad JPA
│   ├── TarifaRango.java               # Entidad JPA
│   └── Deposito.java                  # Entidad JPA
├── dto/
│   └── ClienteDTO.java                # Data Transfer Object
├── infrastructure/
│   └── repository/
│       ├── ClienteRepository.java     # JPA Repository
│       ├── ContenedorRepository.java  # JPA Repository
│       ├── CamionRepository.java      # JPA Repository
│       ├── TarifaRepository.java      # JPA Repository
│       ├── TarifaRangoRepository.java # JPA Repository
│       └── DepositoRepository.java    # JPA Repository
├── mapper/
│   └── ClienteMapper.java             # Mapper: Entity ↔ DTO
└── RecursosApplication.java           # Spring Boot Application
```

## Endpoints Disponibles para Cliente

### Base URL: `/api/clientes`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET    | `/api/clientes` | Obtener todos los clientes |
| GET    | `/api/clientes/{id}` | Obtener cliente por ID |
| POST   | `/api/clientes` | Crear nuevo cliente |
| PUT    | `/api/clientes/{id}` | Actualizar cliente |
| DELETE | `/api/clientes/{id}` | Eliminar cliente |

### Ejemplos de Uso

#### 1. Obtener todos los clientes
```bash
GET http://localhost:8080/api/clientes
```

#### 2. Obtener cliente por ID
```bash
GET http://localhost:8080/api/clientes/1
```

#### 3. Crear cliente
```bash
POST http://localhost:8080/api/clientes
Content-Type: application/json

{
  "nombre": "Juan",
  "apellido": "Pérez",
  "tipoDoc": "DNI",
  "nroDoc": 12345678,
  "telefono": 1234567890,
  "mail": "juan@example.com"
}
```

#### 4. Actualizar cliente
```bash
PUT http://localhost:8080/api/clientes/1
Content-Type: application/json

{
  "nombre": "Juan",
  "apellido": "García",
  "tipoDoc": "DNI",
  "nroDoc": 12345678,
  "telefono": 1234567890,
  "mail": "juan.garcia@example.com"
}
```

#### 5. Eliminar cliente
```bash
DELETE http://localhost:8080/api/clientes/1
```

## Patrón de Arquitectura

```
Controller (api/) 
    ↓ (recibe solicitudes HTTP)
Service (application/) 
    ↓ (aplica lógica de negocio)
Repository (infrastructure/)
    ↓ (accede a base de datos)
Entity (domain/)
    ↓ (modela datos)
DTO (dto/) + Mapper (mapper/)
    ↓ (serializa/deserializa)
Cliente (Frontend)
```

## Próximos Pasos

1. Replicar la estructura para las otras 5 entidades:
   - ContenedorController, ContenedorService, ContenedorDTO, ContenedorMapper
   - CamionController, CamionService, CamionDTO, CamionMapper
   - TarifaController, TarifaService, TarifaDTO, TarifaMapper
   - TarifaRangoController, TarifaRangoService, TarifaRangoDTO, TarifaRangoMapper
   - DepositoController, DepositoService, DepositoDTO, DepositoMapper

2. Agregar validaciones (Jakarta Validation)
3. Manejo de excepciones centralizado
4. Documentación Swagger/OpenAPI
