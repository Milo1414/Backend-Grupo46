package tpi_grupo46.logistica.domain.model;

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
import java.time.LocalDateTime;

/**
 * Representa un registro de cambio de estado dentro de una solicitud.
 * Permite conocer la evolución del envío a lo largo del tiempo.
 */
@Entity
@Table(name = "CAMBIO_ESTADO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CambioEstado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cambio_estado")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @ManyToOne
    @JoinColumn(name = "solicitud_id")
    private Solicitud solicitud;

    @ManyToOne
    @JoinColumn(name = "tramo_id")
    private Tramo tramo;

    @Column(name = "contenedor_id")
    private Long contenedorId;  // Referencia al contenedor (está en ms-recursos)
}
