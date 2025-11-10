# 🚀 Guía Rápida: RecursosClient Genérico

## 📋 Descripción

Cliente HTTP genérico para consumir endpoints del microservicio **recursos** (puerto 8080) desde el microservicio **logística** (puerto 8081), **sin necesidad de crear DTOs específicos** para cada llamada.

---

## 🎯 Ventajas

✅ **No requiere DTOs**: Trabaja con `Map<String, Object>` dinámicamente  
✅ **Más simple**: Menos código boilerplate  
✅ **Más flexible**: Fácil de adaptar a cambios en la API  
✅ **Rápido desarrollo**: Agregar nuevos endpoints es trivial  

---

## 🛠️ Uso Básico

### 1️⃣ Inyectar el cliente

```java
@Service
@RequiredArgsConstructor
public class MiServicio {
    
    private final RecursosClient recursosClient;
    
    // ... tus métodos
}
```

---

## 📚 Métodos Disponibles

### GET Lista de recursos
```java
List<Map<String, Object>> lista = recursosClient.get("/clientes");
```

### GET Un recurso por ID
```java
Map<String, Object> recurso = recursosClient.get("/clientes/{id}", 1);
```

### POST Crear recurso
```java
Map<String, Object> nuevoRecurso = Map.of(
    "nombre", "Juan Perez",
    "email", "juan@email.com"
);
Map<String, Object> resultado = recursosClient.post("/clientes", nuevoRecurso);
```

### PUT Actualizar recurso
```java
Map<String, Object> datosActualizados = Map.of(
    "nombre", "Juan Carlos Perez"
);
Map<String, Object> resultado = recursosClient.put("/clientes/{id}", 1, datosActualizados);
```

### DELETE Eliminar recurso
```java
recursosClient.delete("/clientes/{id}", 1);
```

---

## 🔥 Ejemplos Prácticos

### Ejemplo 1: Obtener todos los clientes
```java
public List<Map<String, Object>> obtenerClientes() {
    return recursosClient.get("/clientes");
}
```

### Ejemplo 2: Obtener un cliente específico
```java
public Map<String, Object> obtenerCliente(Integer id) {
    return recursosClient.get("/clientes/{id}", id);
}
```

### Ejemplo 3: Verificar capacidad de un camión
```java
public boolean verificarCapacidad(String dominio, Double peso) {
    Map<String, Object> camion = recursosClient.get("/camiones/{id}", dominio);
    
    if (camion == null) return false;
    
    Double capacidad = ((Number) camion.get("capacidad")).doubleValue();
    return capacidad >= peso;
}
```

### Ejemplo 4: Filtrar depósitos por provincia
```java
public List<Map<String, Object>> depositosEnProvincia(String provincia) {
    List<Map<String, Object>> todos = recursosClient.get("/depositos");
    
    return todos.stream()
        .filter(d -> provincia.equals(d.get("provincia")))
        .toList();
}
```

### Ejemplo 5: Calcular tarifa
```java
public Double calcularTarifa(Integer tarifaId, Double distancia) {
    Map<String, Object> tarifa = recursosClient.get("/tarifas/{id}", tarifaId);
    
    if (tarifa == null) return null;
    
    Double precioBase = ((Number) tarifa.get("precioBase")).doubleValue();
    return precioBase * distancia;
}
```

---

## 🎯 Métodos Helper (atajos)

El cliente incluye métodos auxiliares para facilitar el uso:

```java
// Clientes
recursosClient.getClientes()          // Lista todos
recursosClient.getCliente(id)         // Obtiene uno

// Camiones
recursosClient.getCamiones()          // Lista todos
recursosClient.getCamion(dominio)     // Obtiene uno

// Depósitos
recursosClient.getDepositos()         // Lista todos
recursosClient.getDeposito(id)        // Obtiene uno

// Contenedores
recursosClient.getContenedores()      // Lista todos
recursosClient.getContenedor(id)      // Obtiene uno

// Tarifas
recursosClient.getTarifas()           // Lista todos
recursosClient.getTarifa(id)          // Obtiene uno
```

---

## 🧪 Testing - Endpoints de Ejemplo

Puedes probar la integración con estos endpoints en el microservicio de **logística**:

```bash
# Obtener todos los clientes desde recursos
GET http://localhost:8081/api/ejemplos-recursos/clientes

# Obtener un cliente específico
GET http://localhost:8081/api/ejemplos-recursos/clientes/1

# Obtener un camión por dominio
GET http://localhost:8081/api/ejemplos-recursos/camiones/ABC123

# Verificar disponibilidad de camión
GET http://localhost:8081/api/ejemplos-recursos/verificar-camion?dominio=ABC123&peso=1000

# Obtener depósitos de una provincia
GET http://localhost:8081/api/ejemplos-recursos/depositos/provincia/Buenos%20Aires

# Calcular tarifa aproximada
GET http://localhost:8081/api/ejemplos-recursos/calcular-tarifa?tarifaId=1&distancia=500

# Resumen de recursos
GET http://localhost:8081/api/ejemplos-recursos/resumen
```

---

## ⚠️ Manejo de Tipos

Al trabajar con `Map<String, Object>`, los valores vienen como `Object`. Debes hacer cast cuando necesites tipos específicos:

### Números
```java
Object capacidadObj = camion.get("capacidad");
Double capacidad = capacidadObj instanceof Number 
    ? ((Number) capacidadObj).doubleValue() 
    : Double.parseDouble(capacidadObj.toString());
```

### Strings
```java
String provincia = (String) deposito.get("provincia");
// O más seguro:
String provincia = String.valueOf(deposito.get("provincia"));
```

### Integers
```java
Integer id = ((Number) recurso.get("id")).intValue();
```

### Booleanos
```java
Boolean activo = (Boolean) recurso.get("activo");
```

---

## 🔒 Validaciones Recomendadas

```java
// Siempre validar null
Map<String, Object> recurso = recursosClient.get("/clientes/{id}", id);
if (recurso == null) {
    throw new ResourceNotFoundException("Cliente no encontrado");
}

// Validar campos requeridos
Object valor = recurso.get("campo");
if (valor == null) {
    throw new IllegalStateException("Campo requerido no presente");
}
```

---

## 🌐 Endpoints Disponibles en Recursos

Todos estos endpoints están disponibles para consumir:

| Recurso | Endpoint | Método | Descripción |
|---------|----------|--------|-------------|
| **Clientes** | `/api/clientes` | GET | Lista todos |
| | `/api/clientes/{id}` | GET | Obtiene uno |
| | `/api/clientes` | POST | Crea uno |
| | `/api/clientes/{id}` | PUT | Actualiza |
| | `/api/clientes/{id}` | DELETE | Elimina |
| **Camiones** | `/api/camiones` | GET | Lista todos |
| | `/api/camiones/{dominio}` | GET | Obtiene uno |
| | `/api/camiones` | POST | Crea uno |
| | `/api/camiones/{dominio}` | PUT | Actualiza |
| | `/api/camiones/{dominio}` | DELETE | Elimina |
| **Depósitos** | `/api/depositos` | GET | Lista todos |
| | `/api/depositos/{id}` | GET | Obtiene uno |
| | `/api/depositos` | POST | Crea uno |
| | `/api/depositos/{id}` | PUT | Actualiza |
| | `/api/depositos/{id}` | DELETE | Elimina |
| **Contenedores** | `/api/contenedores` | GET | Lista todos |
| | `/api/contenedores/{id}` | GET | Obtiene uno |
| | `/api/contenedores` | POST | Crea uno |
| | `/api/contenedores/{id}` | PUT | Actualiza |
| | `/api/contenedores/{id}` | DELETE | Elimina |
| **Tarifas** | `/api/tarifas` | GET | Lista todos |
| | `/api/tarifas/{id}` | GET | Obtiene uno |
| | `/api/tarifas` | POST | Crea uno |
| | `/api/tarifas/{id}` | PUT | Actualiza |
| | `/api/tarifas/{id}` | DELETE | Elimina |
| **TarifaRangos** | `/api/tarifa-rangos` | GET | Lista todos |
| | `/api/tarifa-rangos/{id}` | GET | Obtiene uno |
| | `/api/tarifa-rangos` | POST | Crea uno |
| | `/api/tarifa-rangos/{id}` | PUT | Actualiza |
| | `/api/tarifa-rangos/{id}` | DELETE | Elimina |

---

## 💡 Tips

1. **Usa los helpers**: Para código más limpio, usa `recursosClient.getClientes()` en lugar de `recursosClient.get("/clientes")`

2. **Logs**: El cliente ya incluye logs automáticos, no necesitas agregar más

3. **Errores**: RestTemplate lanza excepciones automáticamente para errores HTTP (4xx, 5xx)

4. **Performance**: Si necesitas hacer múltiples llamadas, considera hacerlas en paralelo con CompletableFuture

5. **Testing**: Mockea el RecursosClient en tus tests para simular respuestas

---

## 🔧 Configuración

El cliente está preconfigurado con:
- **URL base**: `http://localhost:8080/api`
- **Timeouts**: 5 segundos (connect y read)
- **CORS**: Habilitado en recursos para recibir peticiones desde logística

Si necesitas cambiar la URL o timeouts, edita:
- `RecursosClient.java` → `RECURSOS_BASE_URL`
- `RestTemplateConfig.java` → `.setConnectTimeout()` y `.setReadTimeout()`

---

## 📝 Resumen

Este cliente genérico te permite:
- ✅ Consumir cualquier endpoint de recursos sin crear DTOs
- ✅ Trabajar con Maps para máxima flexibilidad
- ✅ Agregar nuevos endpoints en segundos
- ✅ Menos código = menos bugs
- ✅ Más fácil de mantener y evolucionar

**¡Listo para usar! 🚀**
