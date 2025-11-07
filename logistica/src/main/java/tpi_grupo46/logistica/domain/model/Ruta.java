package tpi_grupo46.logistica.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Representa la ruta completa asignada a una solicitud.
 * Contiene los tramos que conectan el origen con el destino,
 * pasando opcionalmente por uno o varios depósitos.
 */
@Entity
@Table(name = "RUTA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ruta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ruta")
    private Long id;

    @Column(name = "cantidad_tramos")
    private Integer cantidadTramos;

    @Column(name = "cantidad_depositos")
    private Integer cantidadDepositos;

    @OneToOne
    @JoinColumn(name = "solicitud_id", unique = true, nullable = false)
    private Solicitud solicitud;

    @OneToMany(mappedBy = "ruta", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Tramo> tramos;
}
