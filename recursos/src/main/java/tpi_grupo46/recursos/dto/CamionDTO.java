package tpi_grupo46.recursos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
    private Integer telefonoTransportista;
    private BigDecimal costoBaseKm;
    private BigDecimal consumoLKm;

}
