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
public class TarifaDTO {

    private Integer idTarifa;
    private String descripcion;
    private Integer valor;
    private BigDecimal costoKmBase;
    private Integer valorLitroCombustible;
    private Integer consumoPromedio1Km;

}
