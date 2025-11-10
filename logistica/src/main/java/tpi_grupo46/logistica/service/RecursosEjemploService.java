package tpi_grupo46.logistica.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tpi_grupo46.logistica.client.RecursosClient;

import java.util.List;
import java.util.Map;

/**
 * Servicio de ejemplo que muestra cómo usar el RecursosClient genérico
 * sin necesidad de crear DTOs específicos
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RecursosEjemploService {

    private final RecursosClient recursosClient;

    /**
     * Ejemplo 1: Obtener todos los clientes
     * Retorna una lista de Maps, cada Map es un cliente
     */
    public List<Map<String, Object>> obtenerTodosLosClientes() {
        log.info("Obteniendo todos los clientes desde recursos...");
        return recursosClient.get("/clientes");
    }

    /**
     * Ejemplo 2: Obtener un cliente específico por ID
     * Retorna un Map con los datos del cliente
     */
    public Map<String, Object> obtenerClientePorId(Integer id) {
        log.info("Obteniendo cliente con id={} desde recursos...", id);
        return recursosClient.get("/clientes/{id}", id);
    }

    /**
     * Ejemplo 3: Obtener un camión por dominio
     */
    public Map<String, Object> obtenerCamionPorDominio(String dominio) {
        log.info("Obteniendo camión con dominio={} desde recursos...", dominio);
        return recursosClient.get("/camiones/{id}", dominio);
    }

    /**
     * Ejemplo 4: Verificar disponibilidad de camiones para una ruta
     * Muestra cómo procesar datos sin DTOs
     */
    public boolean verificarDisponibilidadCamiones(String dominio, Double pesoRequerido) {
        log.info("Verificando disponibilidad del camión {} para peso {}", dominio, pesoRequerido);
        
        // Obtenemos el camión como Map
        Map<String, Object> camion = recursosClient.getCamion(dominio);
        
        if (camion == null) {
            log.warn("Camión {} no encontrado", dominio);
            return false;
        }

        // Accedemos a los campos directamente del Map
        Object capacidadObj = camion.get("capacidad");
        if (capacidadObj == null) {
            log.warn("Camión {} sin capacidad definida", dominio);
            return false;
        }

        // Convertimos a Double
        Double capacidad = capacidadObj instanceof Number 
            ? ((Number) capacidadObj).doubleValue() 
            : Double.parseDouble(capacidadObj.toString());

        boolean disponible = capacidad >= pesoRequerido;
        log.info("Camión {}: capacidad={}, requerido={}, disponible={}", 
            dominio, capacidad, pesoRequerido, disponible);
        
        return disponible;
    }

    /**
     * Ejemplo 5: Obtener depósitos en una provincia específica
     * Muestra filtrado de resultados sin DTOs
     */
    public List<Map<String, Object>> obtenerDepositosPorProvincia(String provincia) {
        log.info("Buscando depósitos en provincia: {}", provincia);
        
        List<Map<String, Object>> todosLosDepositos = recursosClient.getDepositos();
        
        // Filtramos los depósitos por provincia
        return todosLosDepositos.stream()
            .filter(deposito -> {
                Object prov = deposito.get("provincia");
                return prov != null && prov.toString().equalsIgnoreCase(provincia);
            })
            .toList();
    }

    /**
     * Ejemplo 6: Calcular tarifa aproximada
     * Muestra cómo trabajar con múltiples endpoints sin DTOs
     */
    public Double calcularTarifaAproximada(Integer tarifaId, Double distancia) {
        log.info("Calculando tarifa aproximada para tarifaId={}, distancia={}", tarifaId, distancia);
        
        // Obtenemos la tarifa
        Map<String, Object> tarifa = recursosClient.getTarifa(tarifaId);
        
        if (tarifa == null) {
            log.error("Tarifa {} no encontrada", tarifaId);
            return null;
        }

        // Extraemos el precio base
        Object precioBaseObj = tarifa.get("precioBase");
        if (precioBaseObj == null) {
            log.error("Tarifa {} sin precio base", tarifaId);
            return null;
        }

        Double precioBase = precioBaseObj instanceof Number 
            ? ((Number) precioBaseObj).doubleValue() 
            : Double.parseDouble(precioBaseObj.toString());

        // Calculamos (esto es un ejemplo simple, la lógica real puede ser más compleja)
        Double tarifaCalculada = precioBase * distancia;
        
        log.info("Tarifa calculada: ${}", tarifaCalculada);
        return tarifaCalculada;
    }

    /**
     * Ejemplo 7: Obtener resumen de recursos disponibles
     * Muestra cómo combinar múltiples llamadas
     */
    public Map<String, Object> obtenerResumenRecursos() {
        log.info("Generando resumen de recursos disponibles...");
        
        List<Map<String, Object>> clientes = recursosClient.getClientes();
        List<Map<String, Object>> camiones = recursosClient.getCamiones();
        List<Map<String, Object>> depositos = recursosClient.getDepositos();
        List<Map<String, Object>> contenedores = recursosClient.getContenedores();

        return Map.of(
            "totalClientes", clientes.size(),
            "totalCamiones", camiones.size(),
            "totalDepositos", depositos.size(),
            "totalContenedores", contenedores.size(),
            "timestamp", System.currentTimeMillis()
        );
    }
}
