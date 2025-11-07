package tpi_grupo46.logistica.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import tpi_grupo46.logistica.domain.enums.EstadoSolicitud;

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

    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado;

    @Column(name = "fecha_cambio", nullable = false)
    private LocalDateTime fechaCambio;

    @ManyToOne
    @JoinColumn(name = "solicitud_id", nullable = false)
    private Solicitud solicitud;
}
