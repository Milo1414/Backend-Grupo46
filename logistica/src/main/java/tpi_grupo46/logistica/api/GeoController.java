package tpi_grupo46.logistica.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tpi_grupo46.logistica.application.GeoService;
import tpi_grupo46.logistica.dto.DistanciaDTO;

/**
 * Controlador REST para cálculo de distancias usando Google Maps Distance
 * Matrix API
 * Endpoints:
 * - GET /api/distancia?origen=Buenos%20Aires&destino=La%20Plata
 * - GET
 * /api/distancia?origen=-34.603722,-58.381592&destino=-34.615851,-58.433238
 */
@RestController
@RequestMapping("/api/distancia")
@RequiredArgsConstructor
@Slf4j
public class GeoController {

    private final GeoService geoService;

    /**
     * Calcula la distancia y duración entre dos ubicaciones
     * 
     * @param origen  ubicación de origen (ej: "Buenos Aires" o coordenadas
     *                "-34.603722,-58.381592")
     * @param destino ubicación de destino (ej: "La Plata" o coordenadas
     *                "-34.615851,-58.433238")
     * @return DistanciaDTO con distancia en km y duración
     */
    @GetMapping
    public ResponseEntity<DistanciaDTO> obtenerDistancia(
            @RequestParam String origen,
            @RequestParam String destino) {
        try {
            log.info("Petición recibida: origen={}, destino={}", origen, destino);
            DistanciaDTO resultado = geoService.calcularDistancia(origen, destino);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            log.error("Error al calcular distancia: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Endpoint de prueba para validar que el servicio está funcionando
     * 
     * @return mensaje de confirmación
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Servicio de Geo Distance Matrix API funcionando correctamente");
    }
}
