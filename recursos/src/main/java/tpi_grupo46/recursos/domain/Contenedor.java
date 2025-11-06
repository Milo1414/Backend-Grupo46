package tpi_grupo46.recursos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "CONTENEDOR")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contenedor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contenedor")
    private Integer idContenedor;

    @Column(name = "peso")
    private BigDecimal peso;

    @Column(name = "volumen")
    private BigDecimal volumen;

    @Column(name = "id_estado_contenedor")
    private Integer idEstadoContenedor;

    @Column(name = "cliente_asociado")
    private String clienteAsociado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

}
