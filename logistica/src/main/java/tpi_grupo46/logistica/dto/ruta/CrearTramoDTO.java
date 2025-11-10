package tpi_grupo46.logistica.dto.ruta;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * DTO para crear un nuevo tramo dentro de una ruta
 */
public record CrearTramoDTO(
    @NotNull(message = "El origen es requerido")
    Integer origen,  // ID o código del origen (depósito o dirección)
    
    @NotNull(message = "El destino es requerido")
    Integer destino,  // ID o código del destino
    
    @NotNull(message = "El tipo de tramo es requerido")
    String tipo,  // origen-destino, origen-deposito, deposito-destino, deposito-deposito
    
    BigDecimal costoAproximado,
    
    Double distanciaKm,
    
    Double tiempoEstimadoHoras
) {
}
