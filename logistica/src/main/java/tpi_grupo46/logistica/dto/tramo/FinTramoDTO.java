package tpi_grupo46.logistica.dto.tramo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de entrada para finalizar un tramo de ruta.
 * Registra la fecha, hora y costo real del transporte del segmento.
 */
public record FinTramoDTO(
    @NotNull(message = "fechaHoraFin es requerido") LocalDateTime fechaHoraFin,

    @NotNull(message = "costoReal es requerido") @Positive(message = "costoReal debe ser un valor positivo") BigDecimal costoReal) {
}
