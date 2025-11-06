package tpi_grupo46.recursos.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tpi_grupo46.recursos.domain.Tarifa;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, Integer> {
}
