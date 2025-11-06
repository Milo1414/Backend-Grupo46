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
public class DepositoDTO {

    private Integer idDeposito;
    private BigDecimal latitudDeposito;
    private BigDecimal longitudDeposito;
    private String calleDeposito;
    private Integer nroDeposito;
    private BigDecimal costoDiaTransportista;

}
