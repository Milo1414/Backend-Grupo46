package tpi_grupo46.logistica.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tpi_grupo46.logistica.domain.model.CambioEstado;
import tpi_grupo46.logistica.domain.model.Estado;

import java.util.List;

/**
 * Repositorio JPA para la entidad CambioEstado
 */
@Repository
public interface CambioEstadoRepository extends JpaRepository<CambioEstado, Long> {
    
    /**
     * Busca todos los cambios de estado de una solicitud específica
     */
    List<CambioEstado> findBySolicitudIdOrderByFechaInicioAsc(Long solicitudId);
    
    /**
     * Busca todos los cambios de estado de un tramo específico
     */
    List<CambioEstado> findByTramoIdOrderByFechaInicioAsc(Long tramoId);
    
    /**
     * Busca todos los cambios de estado de un contenedor específico
     */
    List<CambioEstado> findByContenedorIdOrderByFechaInicioAsc(Long contenedorId);
    
    /**
     * Busca todos los cambios de estado con un estado específico
     */
    List<CambioEstado> findByEstado(Estado estado);
}
