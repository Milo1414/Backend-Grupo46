package tpi_grupo46.logistica.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tpi_grupo46.logistica.service.RecursosEjemploService;

import java.util.List;
import java.util.Map;

/**
 * Controller de ejemplo para probar la integración con el microservicio de recursos
 * usando el cliente genérico sin DTOs
 */
@RestController
@RequestMapping("/api/ejemplos-recursos")
@RequiredArgsConstructor
@Slf4j
public class RecursosEjemploController {

    private final RecursosEjemploService ejemploService;

    /**
     * GET /api/ejemplos-recursos/clientes
     * Obtiene todos los clientes desde el microservicio de recursos
     */
    @GetMapping("/clientes")
    public ResponseEntity<List<Map<String, Object>>> obtenerClientes() {
        log.info("GET /api/ejemplos-recursos/clientes");
        List<Map<String, Object>> clientes = ejemploService.obtenerTodosLosClientes();
        return ResponseEntity.ok(clientes);
    }

    /**
     * GET /api/ejemplos-recursos/clientes/{id}
     * Obtiene un cliente específico por ID
     */
    @GetMapping("/clientes/{id}")
    public ResponseEntity<Map<String, Object>> obtenerClientePorId(@PathVariable Integer id) {
        log.info("GET /api/ejemplos-recursos/clientes/{}", id);
        Map<String, Object> cliente = ejemploService.obtenerClientePorId(id);
        return ResponseEntity.ok(cliente);
    }

    /**
     * GET /api/ejemplos-recursos/camiones/{dominio}
     * Obtiene un camión por dominio
     */
    @GetMapping("/camiones/{dominio}")
    public ResponseEntity<Map<String, Object>> obtenerCamionPorDominio(@PathVariable String dominio) {
        log.info("GET /api/ejemplos-recursos/camiones/{}", dominio);
        Map<String, Object> camion = ejemploService.obtenerCamionPorDominio(dominio);
        return ResponseEntity.ok(camion);
    }

    /**
     * GET /api/ejemplos-recursos/verificar-camion?dominio=ABC123&peso=1000
     * Verifica si un camión tiene capacidad suficiente
     */
    @GetMapping("/verificar-camion")
    public ResponseEntity<Map<String, Object>> verificarDisponibilidadCamion(
            @RequestParam String dominio,
            @RequestParam Double peso) {
        log.info("GET /api/ejemplos-recursos/verificar-camion?dominio={}&peso={}", dominio, peso);
        
        boolean disponible = ejemploService.verificarDisponibilidadCamiones(dominio, peso);
        
        return ResponseEntity.ok(Map.of(
            "dominio", dominio,
            "pesoRequerido", peso,
            "disponible", disponible
        ));
    }

    /**
     * GET /api/ejemplos-recursos/depositos/provincia/{provincia}
     * Obtiene depósitos filtrados por provincia
     */
    @GetMapping("/depositos/provincia/{provincia}")
    public ResponseEntity<List<Map<String, Object>>> obtenerDepositosPorProvincia(
            @PathVariable String provincia) {
        log.info("GET /api/ejemplos-recursos/depositos/provincia/{}", provincia);
        
        List<Map<String, Object>> depositos = ejemploService.obtenerDepositosPorProvincia(provincia);
        
        return ResponseEntity.ok(depositos);
    }

    /**
     * GET /api/ejemplos-recursos/calcular-tarifa?tarifaId=1&distancia=500
     * Calcula tarifa aproximada
     */
    @GetMapping("/calcular-tarifa")
    public ResponseEntity<Map<String, Object>> calcularTarifa(
            @RequestParam Integer tarifaId,
            @RequestParam Double distancia) {
        log.info("GET /api/ejemplos-recursos/calcular-tarifa?tarifaId={}&distancia={}", tarifaId, distancia);
        
        Double tarifaCalculada = ejemploService.calcularTarifaAproximada(tarifaId, distancia);
        
        if (tarifaCalculada == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(Map.of(
            "tarifaId", tarifaId,
            "distancia", distancia,
            "tarifaCalculada", tarifaCalculada
        ));
    }

    /**
     * GET /api/ejemplos-recursos/resumen
     * Obtiene un resumen de todos los recursos disponibles
     */
    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Object>> obtenerResumen() {
        log.info("GET /api/ejemplos-recursos/resumen");
        Map<String, Object> resumen = ejemploService.obtenerResumenRecursos();
        return ResponseEntity.ok(resumen);
    }
}
