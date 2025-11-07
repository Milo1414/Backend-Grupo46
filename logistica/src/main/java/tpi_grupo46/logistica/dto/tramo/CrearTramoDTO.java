package tpi_grupo46.logistica.dto.tramo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * DTO de entrada para crear un nuevo tramo de ruta.
 * Contiene la información del segmento de transporte individual.
 */
public record CrearTramoDTO(
    @NotNull(message = "origen es requerido") @NotBlank(message = "origen no puede estar vacío") String origen,

    @NotNull(message = "destino es requerido") @NotBlank(message = "destino no puede estar vacío") String destino,

    @NotNull(message = "tipo es requerido") @NotBlank(message = "tipo no puede estar vacío") String tipo,

    @NotNull(message = "costoAproximado es requerido") @Positive(message = "costoAproximado debe ser un valor positivo") BigDecimal costoAproximado,

    @NotNull(message = "distanciaKm es requerido") @Positive(message = "distanciaKm debe ser un valor positivo") Double distanciaKm,

    @NotNull(message = "tiempoEstimadoHoras es requerido") @Positive(message = "tiempoEstimadoHoras debe ser un valor positivo") Double tiempoEstimadoHoras) {
}
