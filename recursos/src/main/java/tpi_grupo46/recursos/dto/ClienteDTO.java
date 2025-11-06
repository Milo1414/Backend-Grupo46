package tpi_grupo46.recursos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteDTO {

    private Integer idCliente;
    private String nombre;
    private String apellido;
    private String tipoDoc;
    private Integer nroDoc;
    private Integer telefono;
    private String mail;

}
