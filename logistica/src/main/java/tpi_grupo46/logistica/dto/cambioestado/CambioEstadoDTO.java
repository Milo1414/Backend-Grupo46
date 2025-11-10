package tpi_grupo46.logistica.dto.cambioestado;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para cambios de estado.
 * Usado para auditoría y trazabilidad de transiciones en solicitudes.
 */
public record CambioEstadoDTO(
    Long id,
    String estadoCodigo,  // Código del estado (BORRADOR, PROGRAMADA, etc.)
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin) {
}
