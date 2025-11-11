# Guía de Prueba: Google Maps Distance Matrix API

## 1. Iniciar la aplicación
```bash
mvn spring-boot:run
```

La aplicación arrancará en `http://localhost:8081`

## 2. Endpoints disponibles

### Test endpoint (verificar que el servicio está funcionando)
```bash
curl http://localhost:8081/api/distancia/test
```

**Respuesta esperada:**
```json
"Servicio de Geo Distance Matrix API funcionando correctamente"
```

### Calcular distancia con nombres de lugares
```bash
curl "http://localhost:8081/api/distancia?origen=Buenos%20Aires&destino=La%20Plata"
```

### Calcular distancia con coordenadas (lat,lon)
```bash
curl "http://localhost:8081/api/distancia?origen=-34.603722,-58.381592&destino=-34.615851,-58.433238"
```

**Respuesta esperada:**
```json
{
  "origen": "Buenos Aires",
  "destino": "La Plata",
  "kilometros": 52.5,
  "duracionTexto": "1 hour 5 mins"
}
```

## 3. Ejemplos de uso en PowerShell

### Test
```powershell
Invoke-WebRequest -Uri "http://localhost:8081/api/distancia/test" -Method Get
```

### Calcular distancia
```powershell
$params = @{
    origen = "Buenos Aires"
    destino = "La Plata"
}
Invoke-WebRequest -Uri "http://localhost:8081/api/distancia" -Method Get -Body $params
```

## 4. Ejemplos de uso desde otra aplicación (Java)

```java
// Inyectar el servicio
@Autowired
private GeoService geoService;

// Usar el servicio
public void calcularRuta() throws Exception {
    DistanciaDTO distancia = geoService.calcularDistancia(
        "-34.603722,-58.381592",  // Buenos Aires
        "-34.615851,-58.433238"   // La Plata
    );
    
    System.out.println("Distancia: " + distancia.getKilometros() + " km");
    System.out.println("Duración: " + distancia.getDuracionTexto());
}
```

## 5. Parámetros aceptados

### Formato de ubicaciones

1. **Nombres de lugares** (ej: "Buenos Aires", "La Plata")
   - URL encode: reemplaza espacios con %20

2. **Coordenadas decimales** (lat,lon)
   - Ej: "-34.603722,-58.381592" (Buenos Aires)
   - Ej: "-34.615851,-58.433238" (La Plata)

3. **Direcciones** (ej: "Av. Corrientes 1234, Buenos Aires")
   - URL encode: reemplaza espacios con %20

## 6. Configuración actual

- **API Key**: AIzaSyCtOQP2BTfr-E0k6QsZx5boR1U2_iZlvzM
- **Base URL**: https://maps.googleapis.com/maps/api
- **Endpoint**: /distancematrix/json
- **Unidades**: Métrica (km, metros)

## 7. Notas importantes

- La API devuelve distancias en metros; se convierten a km automáticamente
- La duración está en formato legible (ej: "1 hour 5 mins")
- Los errores se logean en la consola para debugging
- Máximo 25 pares origen-destino por request (límite de Google Maps)
