package tpi_grupo46.logistica.dto.cambioestado;

import tpi_grupo46.logistica.domain.enums.EstadoSolicitud;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para cambios de estado.
 * Usado para auditoría y trazabilidad de transiciones en solicitudes.
 */
public record CambioEstadoDTO(
    Long id,
    EstadoSolicitud estado,
    LocalDateTime fechaCambio) {
}
