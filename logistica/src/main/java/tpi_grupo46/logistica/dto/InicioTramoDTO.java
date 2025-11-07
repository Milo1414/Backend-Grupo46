package tpi_grupo46.logistica.dto;

import java.time.LocalDateTime;

/**
 * DTO para registrar inicio de un tramo
 */
public record InicioTramoDTO(
    LocalDateTime fechaHoraInicio) {
}
