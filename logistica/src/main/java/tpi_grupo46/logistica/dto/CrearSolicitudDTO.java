package tpi_grupo46.logistica.dto;

/**
 * DTO para crear/actualizar una solicitud
 */
public record CrearSolicitudDTO(
    Long clienteId,
    Long contenedorId) {
}
