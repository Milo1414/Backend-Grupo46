package tpi_grupo46.logistica.dto;

import tpi_grupo46.logistica.domain.enums.EstadoSolicitud;
import java.time.LocalDateTime;

/**
 * DTO para la entidad CambioEstado
 */
public record CambioEstadoDTO(
        Long id,
        EstadoSolicitud estado,
        LocalDateTime fechaCambio
) {}
