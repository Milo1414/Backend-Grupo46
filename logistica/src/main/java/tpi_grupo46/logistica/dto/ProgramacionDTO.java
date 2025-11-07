package tpi_grupo46.logistica.dto;

import java.math.BigDecimal;

/**
 * DTO para programar una solicitud
 */
public record ProgramacionDTO(
    BigDecimal costoEstimado,
    Double tiempoEstimadoHoras) {
}
