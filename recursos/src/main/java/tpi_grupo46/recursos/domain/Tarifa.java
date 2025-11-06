package tpi_grupo46.recursos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import tpi_grupo46.recursos.domain.TarifaRango;

@Entity
@Table(name = "TARIFA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tarifa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarifa")
    private Integer idTarifa;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "valor")
    private Integer valor;

    @Column(name = "costo_km_base")
    private BigDecimal costoKmBase;

    @Column(name = "valor_litro_combustible")
    private Integer valorLitroCombustible;

    @Column(name = "consumo_prom_1_km")
    private Integer consumoPromedio1Km;

    @OneToMany(mappedBy = "tarifa")
    private List<TarifaRango> tarifasRango;

}
