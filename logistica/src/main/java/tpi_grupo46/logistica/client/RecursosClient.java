package tpi_grupo46.logistica.client;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Cliente genérico para consumir endpoints del microservicio de recursos.
 * 
 * Uso simple sin necesidad de DTOs específicos:
 * - recursosClient.get("/clientes")
 * - recursosClient.get("/clientes/{id}", 1)
 * - recursosClient.post("/clientes", body)
 * - recursosClient.put("/clientes/{id}", 1, body)
 * 
 * Endpoints disponibles:
 * - /clientes, /camiones, /depositos, /contenedores, /tarifas, /tarifa-rangos
 */
@Component
@AllArgsConstructor
@Slf4j
public class RecursosClient {

    private final RestTemplate restTemplate;
    
    private static final String RECURSOS_BASE_URL = "http://localhost:8080/api";

    // ==================== MÉTODOS GENÉRICOS ====================
    
    /**
     * GET genérico - Obtiene una lista de recursos
     * Ejemplo: get("/clientes") → List de clientes
     */
    public List<Map<String, Object>> get(String endpoint) {
        String url = RECURSOS_BASE_URL + endpoint;
        log.debug("GET {}", url);
        
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );
        return response.getBody();
    }

    /**
     * GET genérico con path param - Obtiene un recurso por ID
     * Ejemplo: get("/clientes/{id}", 1) → cliente con id=1
     */
    public Map<String, Object> get(String endpoint, Object id) {
        String url = RECURSOS_BASE_URL + endpoint.replace("{id}", String.valueOf(id));
        log.debug("GET {}", url);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> result = restTemplate.getForObject(url, Map.class);
        return result;
    }

    /**
     * POST genérico - Crea un nuevo recurso
     * Ejemplo: post("/clientes", clienteData)
     */
    public Map<String, Object> post(String endpoint, Object body) {
        String url = RECURSOS_BASE_URL + endpoint;
        log.debug("POST {} - Body: {}", url, body);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> result = restTemplate.postForObject(url, body, Map.class);
        return result;
    }

    /**
     * PUT genérico - Actualiza un recurso existente
     * Ejemplo: put("/clientes/{id}", 1, clienteData)
     */
    public Map<String, Object> put(String endpoint, Object id, Object body) {
        String url = RECURSOS_BASE_URL + endpoint.replace("{id}", String.valueOf(id));
        log.debug("PUT {} - Body: {}", url, body);
        
        HttpEntity<Object> requestEntity = new HttpEntity<>(body);
        
        @SuppressWarnings("unchecked")
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
            url,
            HttpMethod.PUT,
            requestEntity,
            (Class<Map<String, Object>>)(Class<?>)Map.class
        );
        
        Map<String, Object> result = response.getBody();
        return result;
    }

    /**
     * DELETE genérico - Elimina un recurso
     * Ejemplo: delete("/clientes/{id}", 1)
     */
    public void delete(String endpoint, Object id) {
        String url = RECURSOS_BASE_URL + endpoint.replace("{id}", String.valueOf(id));
        log.debug("DELETE {}", url);
        
        restTemplate.delete(url);
    }

    // ==================== MÉTODOS AUXILIARES (opcionales) ====================
    
    /**
     * Helper para clientes
     */
    public List<Map<String, Object>> getClientes() {
        return get("/clientes");
    }

    public Map<String, Object> getCliente(Integer id) {
        return get("/clientes/{id}", id);
    }

    /**
     * Helper para camiones
     */
    public List<Map<String, Object>> getCamiones() {
        return get("/camiones");
    }

    public Map<String, Object> getCamion(String dominio) {
        return get("/camiones/{id}", dominio);
    }

    /**
     * Helper para depósitos
     */
    public List<Map<String, Object>> getDepositos() {
        return get("/depositos");
    }

    public Map<String, Object> getDeposito(Integer id) {
        return get("/depositos/{id}", id);
    }

    /**
     * Helper para contenedores
     */
    public List<Map<String, Object>> getContenedores() {
        return get("/contenedores");
    }

    public Map<String, Object> getContenedor(Integer id) {
        return get("/contenedores/{id}", id);
    }

    /**
     * Helper para tarifas
     */
    public List<Map<String, Object>> getTarifas() {
        return get("/tarifas");
    }

    public Map<String, Object> getTarifa(Integer id) {
        return get("/tarifas/{id}", id);
    }
}
