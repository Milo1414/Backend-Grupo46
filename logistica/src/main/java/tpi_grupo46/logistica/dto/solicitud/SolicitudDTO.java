package tpi_grupo46.logistica.dto.solicitud;

import tpi_grupo46.logistica.domain.enums.EstadoSolicitud;
import tpi_grupo46.logistica.dto.ruta.RutaDTO;
import tpi_grupo46.logistica.dto.cambioestado.CambioEstadoDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de respuesta para solicitudes de transporte.
 * Contiene información completa de una solicitud incluyendo ruta y historial de
 * cambios.
 */
public record SolicitudDTO(
    Long id,
    Long clienteId,
    Long contenedorId,
    EstadoSolicitud estado,
    BigDecimal costoEstimado,
    BigDecimal costoFinal,
    Double tiempoEstimadoHoras,
    Double tiempoRealHoras,
    LocalDateTime fechaCreacion,
    RutaDTO ruta,
    List<CambioEstadoDTO> cambiosEstado) {
}
