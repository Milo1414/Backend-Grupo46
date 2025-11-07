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
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Representa un tramo dentro de una ruta.
 * Cada tramo indica un recorrido entre dos puntos
 * (origen → destino, origen → depósito, depósito → destino, etc.).
 */
@Entity
@Table(name = "TRAMO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tramo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tramo")
    private Long id;

    @Column(name = "origen", nullable = false)
    private String origen;

    @Column(name = "destino", nullable = false)
    private String destino;

    @Column(name = "tipo", nullable = false)
    private String tipo; // origen-destino, origen-deposito, deposito-destino, deposito-deposito

    @Column(name = "estado", nullable = false)
    private String estado; // estimado, asignado, iniciado, finalizado

    @Column(name = "costo_aproximado")
    private BigDecimal costoAproximado;

    @Column(name = "costo_real")
    private BigDecimal costoReal;

    @Column(name = "distancia_km")
    private Double distanciaKm;

    @Column(name = "tiempo_estimado_horas")
    private Double tiempoEstimadoHoras;

    @Column(name = "fecha_hora_inicio_real")
    private LocalDateTime fechaHoraInicioReal;

    @Column(name = "fecha_hora_fin_real")
    private LocalDateTime fechaHoraFinReal;

    @Column(name = "camion_id")
    private Long camionId; // Referencia al camión asignado (ms-recursos)

    @ManyToOne
    @JoinColumn(name = "ruta_id", nullable = false)
    private Ruta ruta;

    @ManyToOne
    @JoinColumn(name = "solicitud_id", nullable = false)
    private Solicitud solicitud;
}
