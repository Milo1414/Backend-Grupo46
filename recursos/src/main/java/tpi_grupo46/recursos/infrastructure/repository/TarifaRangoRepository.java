package tpi_grupo46.recursos.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tpi_grupo46.recursos.domain.TarifaRango;

@Repository
public interface TarifaRangoRepository extends JpaRepository<TarifaRango, Integer> {
}
