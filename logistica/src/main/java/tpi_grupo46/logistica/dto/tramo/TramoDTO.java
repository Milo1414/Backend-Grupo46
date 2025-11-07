package tpi_grupo46.logistica.dto.tramo;

/**
 * DTO de respuesta para tramos de ruta.
 * Contiene información detallada de un segmento de transporte.
 */
public record TramoDTO(
    Long id,
    String origen,
    String destino,
    String tipo,
    java.math.BigDecimal costoAproximado,
    java.math.BigDecimal costoReal,
    Double distanciaKm,
    Double tiempoEstimadoHoras,
    java.time.LocalDateTime fechaHoraInicioReal,
    java.time.LocalDateTime fechaHoraFinReal) {
}
