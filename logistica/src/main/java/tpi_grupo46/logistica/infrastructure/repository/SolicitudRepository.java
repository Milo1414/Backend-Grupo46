package tpi_grupo46.logistica.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tpi_grupo46.logistica.domain.model.Solicitud;

import java.util.List;

/**
 * Repositorio JPA para la entidad Solicitud
 */
@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    
    /**
     * Busca todas las solicitudes de un cliente específico
     */
    List<Solicitud> findByClienteId(Long clienteId);
}
