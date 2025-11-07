package tpi_grupo46.logistica.dto.solicitud;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * DTO de entrada para finalizar una solicitud.
 * Contiene los datos de costo y tiempo reales de la entrega.
 */
public record FinalizacionDTO(
    @NotNull(message = "costoFinal es requerido") @Positive(message = "costoFinal debe ser un valor positivo") BigDecimal costoFinal,

    @NotNull(message = "tiempoRealHoras es requerido") @Positive(message = "tiempoRealHoras debe ser un valor positivo") Double tiempoRealHoras) {
}
