package tpi_grupo46.recursos.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tpi_grupo46.recursos.domain.Contenedor;

@Repository
public interface ContenedorRepository extends JpaRepository<Contenedor, Integer> {
}
