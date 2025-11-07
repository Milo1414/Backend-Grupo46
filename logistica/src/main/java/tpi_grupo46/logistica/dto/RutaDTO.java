package tpi_grupo46.logistica.dto;

import java.util.List;

/**
 * DTO para la entidad Ruta
 */
public record RutaDTO(
        Long id,
        Integer cantidadTramos,
        Integer cantidadDepositos,
        List<TramoDTO> tramos
) {}
