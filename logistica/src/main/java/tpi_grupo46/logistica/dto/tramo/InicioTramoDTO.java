package tpi_grupo46.logistica.dto.tramo;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * DTO de entrada para iniciar un tramo de ruta.
 * Registra la fecha y hora en que comienza el transporte del segmento.
 */
public record InicioTramoDTO(
    @NotNull(message = "fechaHoraInicio es requerido") LocalDateTime fechaHoraInicio) {
}
