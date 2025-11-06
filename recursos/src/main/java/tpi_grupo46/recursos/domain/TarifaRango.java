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

@Entity
@Table(name = "TARIFA_RANGO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TarifaRango implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarifa_rango")
    private Integer idTarifaRango;

    @Column(name = "min_peso_kg")
    private BigDecimal minPesoKg;

    @Column(name = "max_peso_kg")
    private BigDecimal maxPesoKg;

    @Column(name = "min_volumen_m3")
    private BigDecimal minVolumenM3;

    @Column(name = "max_volumen_m3")
    private BigDecimal maxVolumenM3;

    @Column(name = "factor_camion")
    private BigDecimal factorCamion;

    @ManyToOne
    @JoinColumn(name = "id_tarifa", nullable = false)
    private Tarifa tarifa;

}
