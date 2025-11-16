package tpi_grupo46.logistica.dto.tramo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO de entrada para asignar un camión a un tramo.
 * Contiene el ID del camión que transportará este segmento.
 */
public record AsignarCamionDTO(
    @NotNull(message = "camionId es requerido") String camionId) {
}
