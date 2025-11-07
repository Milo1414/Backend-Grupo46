package tpi_grupo46.logistica.dto;

import java.math.BigDecimal;

/**
 * DTO para finalizar/completar una solicitud
 */
public record FinalizacionDTO(
    BigDecimal costoFinal,
    Double tiempoRealHoras) {
}
