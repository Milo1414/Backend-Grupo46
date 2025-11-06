package tpi_grupo46.recursos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "DEPOSITO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deposito implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_deposito")
    private Integer idDeposito;

    @Column(name = "latitud_deposito")
    private BigDecimal latitudDeposito;

    @Column(name = "longitud_deposito")
    private BigDecimal longitudDeposito;

    @Column(name = "calle_deposito")
    private String calleDeposito;

    @Column(name = "nro_deposito")
    private Integer nroDeposito;

    @Column(name = "costo_dia_transportista")
    private BigDecimal costoDiaTransportista;

}
