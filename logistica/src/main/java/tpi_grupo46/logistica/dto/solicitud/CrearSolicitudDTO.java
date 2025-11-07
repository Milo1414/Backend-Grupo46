package tpi_grupo46.logistica.dto.solicitud;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO de entrada para crear una nueva solicitud de transporte.
 * Contiene solo los datos requeridos para inicializar una solicitud.
 */
public record CrearSolicitudDTO(
    @NotNull(message = "clienteId es requerido") @Positive(message = "clienteId debe ser un número positivo") Long clienteId,

    @NotNull(message = "contenedorId es requerido") @Positive(message = "contenedorId debe ser un número positivo") Long contenedorId) {
}
