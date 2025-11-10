package tpi_grupo46.logistica.dto.tramo;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * DTO de entrada para crear un nuevo tramo de ruta.
 * Contiene la información del segmento de transporte individual.
 */
public record CrearTramoDTO(
    @NotNull(message = "origen es requerido")
    Integer origen,  // ID o código del origen

    @NotNull(message = "destino es requerido")
    Integer destino,  // ID o código del destino

    @NotNull(message = "tipo es requerido")
    String tipo,  // origen-destino, origen-deposito, etc.

    BigDecimal costoAproximado,

    Double distanciaKm,

    Double tiempoEstimadoHoras) {
}
