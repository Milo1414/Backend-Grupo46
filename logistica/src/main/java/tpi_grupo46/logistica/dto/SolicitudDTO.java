package tpi_grupo46.logistica.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para la entidad Solicitud
 * Utilizado en solicitudes/respuestas REST
 */
public record SolicitudDTO(
        Long id,
        Long clienteId,
        Long contenedorId,
        String estadoActual,  // Código del estado actual
        BigDecimal costoEstimado,
        BigDecimal costoFinal,
        Double tiempoEstimadoHoras,
        Double tiempoRealHoras,
        LocalDateTime fechaCreacion,
        RutaDTO ruta,
        List<CambioEstadoDTO> cambiosEstado
) {}
