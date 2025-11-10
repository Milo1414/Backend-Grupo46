package tpi_grupo46.logistica.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tpi_grupo46.logistica.domain.model.Estado;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para acceder a la entidad Estado.
 * Permite buscar estados por tipo de entidad y código.
 */
@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {

    /**
     * Obtiene todos los estados para un tipo de entidad específico.
     *
     * @param entidadTipo tipo de entidad (SOLICITUD, TRAMO, CONTENEDOR)
     * @return lista de estados aplicables a esa entidad
     */
    List<Estado> findByEntidadTipo(String entidadTipo);

    /**
     * Obtiene todos los estados activos para un tipo de entidad.
     *
     * @param entidadTipo tipo de entidad (SOLICITUD, TRAMO, CONTENEDOR)
     * @param activo      si está activo
     * @return lista de estados activos
     */
    List<Estado> findByEntidadTipoAndActivo(String entidadTipo, Boolean activo);

    /**
     * Obtiene un estado específico por código y tipo de entidad.
     *
     * @param codigo      código único del estado (ej. BORRADOR, ESTIMADO)
     * @param entidadTipo tipo de entidad (SOLICITUD, TRAMO, CONTENEDOR)
     * @return Optional con el estado si existe
     */
    Optional<Estado> findByCodigoAndEntidadTipo(String codigo, String entidadTipo);

    /**
     * Obtiene un estado por su código.
     *
     * @param codigo código único del estado
     * @return Optional con el estado si existe
     */
    Optional<Estado> findByCodigo(String codigo);

}
