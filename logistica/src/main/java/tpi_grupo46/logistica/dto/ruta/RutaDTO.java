package tpi_grupo46.logistica.dto.ruta;

/**
 * DTO de respuesta para rutas de transporte.
 * Contiene información resumida de una ruta incluyendo cantidad de tramos y
 * depósitos.
 */
public record RutaDTO(
    Long id,
    Integer cantidadTramos,
    Integer cantidadDepositos) {
}
