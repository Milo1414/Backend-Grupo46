package tpi_grupo46.logistica.domain.model;

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

/**
 * Entidad que representa los diferentes estados
 * que pueden tener las solicitudes, tramos y contenedores.
 * Simplificada con solo atributos esenciales.
 */
@Entity
@Table(name = "ESTADO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Long id;

    @Column(name = "descripcion", nullable = false, length = 100)
    private String descripcion; // Descripción del estado: "En Borrador", "Programada", etc.

    @Column(name = "codigo", nullable = false, unique = true, length = 50)
    private String codigo; // BORRADOR, PROGRAMADA, EN_TRANSITO, etc. (para búsquedas programáticas)

    @Column(name = "entidad_tipo", nullable = false, length = 50)
    private String entidadTipo; // SOLICITUD, TRAMO, CONTENEDOR (discriminador de tipo)

    /**
     * Enum auxiliar para los tipos de entidades
     */
    public enum TipoEntidad {
        SOLICITUD, TRAMO, CONTENEDOR
    }
}
