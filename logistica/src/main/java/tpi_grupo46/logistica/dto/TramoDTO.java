package tpi_grupo46.logistica.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para la entidad Tramo
 */
public record TramoDTO(
        Long id,
        String origen,
        String destino,
        String tipo,
        String estado,
        BigDecimal costoAproximado,
        BigDecimal costoReal,
        Double distanciaKm,
        Double tiempoEstimadoHoras,
        LocalDateTime fechaHoraInicioReal,
        LocalDateTime fechaHoraFinReal,
        String camionId
) {}
