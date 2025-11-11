package tpi_grupo46.logistica.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuración de Google Maps API
 * Carga valores desde application.yml bajo la clave 'google.maps'
 */
@Component
@ConfigurationProperties(prefix = "google.maps")
@Data
public class GoogleMapsConfig {
    
    /**
     * API Key de Google Maps
     */
    private String apiKey;
    
    /**
     * URL base de Distance Matrix API
     */
    private String distanceMatrixUrl;
    
    /**
     * Timeout en milisegundos para las peticiones HTTP
     */
    private int timeoutMs = 5000;
}
