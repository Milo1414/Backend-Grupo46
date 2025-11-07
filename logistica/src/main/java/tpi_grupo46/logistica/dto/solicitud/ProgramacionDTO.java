package tpi_grupo46.logistica.dto.solicitud;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * DTO de entrada para programar una solicitud.
 * Contiene los datos de costo y tiempo estimados para la entrega.
 */
public record ProgramacionDTO(
    @NotNull(message = "costoEstimado es requerido") @Positive(message = "costoEstimado debe ser un valor positivo") BigDecimal costoEstimado,

    @NotNull(message = "tiempoEstimadoHoras es requerido") @Positive(message = "tiempoEstimadoHoras debe ser un valor positivo") Double tiempoEstimadoHoras) {
}
