package tpi_grupo46.logistica.dto;

import java.math.BigDecimal;

/**
 * DTO para crear/asignar un tramo
 */
public record CrearTramoDTO(
    String origen,
    String destino,
    String tipo,
    BigDecimal costoAproximado,
    Double distanciaKm,
    Double tiempoEstimadoHoras) {
}
