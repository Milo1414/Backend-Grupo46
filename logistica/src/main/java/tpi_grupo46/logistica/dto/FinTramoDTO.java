package tpi_grupo46.logistica.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para registrar finalización de un tramo
 */
public record FinTramoDTO(
    LocalDateTime fechaHoraFin,
    BigDecimal costoReal) {
}
