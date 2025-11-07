package tpi_grupo46.logistica.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tpi_grupo46.logistica.domain.model.Tramo;

import java.util.List;

/**
 * Repositorio JPA para la entidad Tramo
 */
@Repository
public interface TramoRepository extends JpaRepository<Tramo, Long> {
    
    /**
     * Busca todos los tramos asociados a una ruta específica
     */
    List<Tramo> findByRutaId(Long rutaId);
    
    /**
     * Busca todos los tramos asociados a una solicitud específica
     */
    List<Tramo> findBySolicitudId(Long solicitudId);
    
    /**
     * Busca todos los tramos asignados a un camión
     */
    List<Tramo> findByCamionId(Long camionId);
}
