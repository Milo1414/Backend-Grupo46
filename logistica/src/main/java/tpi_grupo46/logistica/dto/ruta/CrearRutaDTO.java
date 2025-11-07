package tpi_grupo46.logistica.dto.ruta;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import tpi_grupo46.logistica.dto.tramo.CrearTramoDTO;
import java.util.List;

/**
 * DTO de entrada para crear una nueva ruta.
 * Contiene el ID de la solicitud y la lista de tramos que componen la ruta.
 */
public record CrearRutaDTO(
    @NotNull(message = "solicitudId es requerido") Long solicitudId,

    @NotNull(message = "tramos es requerido") @NotEmpty(message = "debe haber al menos un tramo") @Valid List<CrearTramoDTO> tramos) {
}
