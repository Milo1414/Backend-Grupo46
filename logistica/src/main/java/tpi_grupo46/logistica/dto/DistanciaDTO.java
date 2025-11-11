package tpi_grupo46.logistica.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para representar la respuesta simplificada de Google Maps Distance Matrix API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistanciaDTO {
    
    /**
     * Ubicación de origen
     */
    private String origen;
    
    /**
     * Ubicación de destino
     */
    private String destino;
    
    /**
     * Distancia en kilómetros
     */
    private double kilometros;
    
    /**
     * Duración en formato legible (ej: "2 hours 30 mins")
     */
    private String duracionTexto;
}
