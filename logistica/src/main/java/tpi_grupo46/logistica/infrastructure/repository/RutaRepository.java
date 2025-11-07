package tpi_grupo46.logistica.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tpi_grupo46.logistica.domain.model.Ruta;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad Ruta
 */
@Repository
public interface RutaRepository extends JpaRepository<Ruta, Long> {
    
    /**
     * Busca la ruta asociada a una solicitud específica
     */
    Optional<Ruta> findBySolicitudId(Long solicitudId);
}
