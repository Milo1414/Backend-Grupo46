package tpi_grupo46.logistica.dto;

import java.util.List;

/**
 * DTO para crear una ruta con múltiples tramos
 */
public record CrearRutaDTO(
    Long solicitudId,
    List<CrearTramoDTO> tramos) {
}
