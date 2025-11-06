package tpi_grupo46.recursos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContenedorDTO {

    private Integer idContenedor;
    private BigDecimal peso;
    private BigDecimal volumen;
    private Integer idEstadoContenedor;
    private String clienteAsociado;
    private LocalDateTime fechaCreacion;
    private Integer idCliente;

}
