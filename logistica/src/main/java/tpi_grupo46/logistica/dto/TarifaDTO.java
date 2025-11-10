package tpi_grupo46.logistica.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para recibir información de tarifas desde el microservicio de recursos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TarifaDTO {
    private Integer idTarifa;
    private String descripcion;
    private BigDecimal valor;
    private BigDecimal costoKmBase;
    private BigDecimal valorLitroCombustible;
    private BigDecimal consumoPromedio1Km;
}
