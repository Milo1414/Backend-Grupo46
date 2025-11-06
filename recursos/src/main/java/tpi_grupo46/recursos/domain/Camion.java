package tpi_grupo46.recursos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "CAMION")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Camion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "dominio_camion")
    private String dominioCamion;

    @Column(name = "capacidad_peso")
    private BigDecimal capacidadPeso;

    @Column(name = "capacidad_volumen")
    private BigDecimal capacidadVolumen;

    @Column(name = "disponibilidad")
    private Boolean disponibilidad;

    @Column(name = "nombre_transportista")
    private String nombreTransportista;

    @Column(name = "telefono_transportista")
    private Integer telefonoTransportista;

    @Column(name = "costo_base_km")
    private BigDecimal costoBaseKm;

    @Column(name = "consumo_l_km")
    private BigDecimal consumoLKm;

}
