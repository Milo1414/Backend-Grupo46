package tpi_grupo46.logistica.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para recibir información de camiones desde el microservicio de recursos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CamionDTO {
    private String dominioCamion;
    private BigDecimal capacidadPeso;
    private BigDecimal capacidadVolumen;
    private Boolean disponibilidad;
    private String nombreTransportista;
    private String telefonoTransportista;
    private BigDecimal costoBaseKm;
    private BigDecimal consumoLKm;
}
