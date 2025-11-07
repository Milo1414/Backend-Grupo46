package tpi_grupo46.logistica.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tpi_grupo46.logistica.domain.enums.EstadoSolicitud;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una solicitud de transporte realizada por un cliente.
 * Es la entidad central del microservicio de logística.
 * Cada solicitud tiene asociado un contenedor, una ruta y un historial de estados.
 */
@Entity
@Table(name = "SOLICITUD")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Solicitud implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Long id;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId; // ID del cliente (referencia externa al ms-recursos)

    @Column(name = "contenedor_id", nullable = false)
    private Long contenedorId; // ID del contenedor a trasladar

    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado; // BORRADOR, PROGRAMADA, EN_TRANSITO, ENTREGADA, CANCELADA

    @Column(name = "costo_estimado")
    private BigDecimal costoEstimado;

    @Column(name = "costo_final")
    private BigDecimal costoFinal;

    @Column(name = "tiempo_estimado_horas")
    private Double tiempoEstimadoHoras;

    @Column(name = "tiempo_real_horas")
    private Double tiempoRealHoras;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @OneToOne(mappedBy = "solicitud", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Ruta ruta;

    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    private List<CambioEstado> cambiosEstado = new ArrayList<>();

    /**
     * Registra automáticamente un cambio de estado cuando se crea una solicitud.
     * Se ejecuta antes de persistir la entidad.
     */
    @jakarta.persistence.PrePersist
    public void prePersist() {
        if (this.fechaCreacion == null) {
            this.fechaCreacion = LocalDateTime.now();
        }
        if (this.estado == null) {
            this.estado = EstadoSolicitud.BORRADOR;
        }
        // Registrar cambio de estado inicial
        if (this.cambiosEstado == null) {
            this.cambiosEstado = new ArrayList<>();
        }
        CambioEstado cambioInicial = CambioEstado.builder()
                .estado(this.estado)
                .fechaCambio(LocalDateTime.now())
                .solicitud(this)
                .build();
        this.cambiosEstado.add(cambioInicial);
    }
}
