package tpi_grupo46.logistica.dto;

import java.time.LocalDateTime;

/**
 * DTO para la entidad CambioEstado
 */
public record CambioEstadoDTO(
        Long id,
        String estadoCodigo,
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin
) {}
