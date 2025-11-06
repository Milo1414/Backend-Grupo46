package tpi_grupo46.recursos.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tpi_grupo46.recursos.domain.Tarifa;
import tpi_grupo46.recursos.dto.TarifaDTO;
import tpi_grupo46.recursos.infrastructure.repository.TarifaRepository;
import tpi_grupo46.recursos.mapper.TarifaMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TarifaService {

    private final TarifaRepository tarifaRepository;
    private final TarifaMapper tarifaMapper = TarifaMapper.INSTANCE;

    public List<TarifaDTO> obtenerTodos() {
        return tarifaRepository.findAll().stream()
                .map(tarifaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TarifaDTO obtenerPorId(Integer id) {
        return tarifaRepository.findById(id)
                .map(tarifaMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Tarifa no encontrada con ID: " + id));
    }

    public TarifaDTO crear(TarifaDTO tarifaDTO) {
        Tarifa tarifa = tarifaMapper.toEntity(tarifaDTO);
        Tarifa tarifaGuardada = tarifaRepository.save(tarifa);
        return tarifaMapper.toDTO(tarifaGuardada);
    }

    public TarifaDTO actualizar(Integer id, TarifaDTO tarifaDTO) {
        Tarifa tarifa = tarifaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarifa no encontrada con ID: " + id));
        
        tarifa.setDescripcion(tarifaDTO.getDescripcion());
        tarifa.setValor(tarifaDTO.getValor());
        tarifa.setCostoKmBase(tarifaDTO.getCostoKmBase());
        tarifa.setValorLitroCombustible(tarifaDTO.getValorLitroCombustible());
        tarifa.setConsumoPromedio1Km(tarifaDTO.getConsumoPromedio1Km());
        
        Tarifa tarifaActualizada = tarifaRepository.save(tarifa);
        return tarifaMapper.toDTO(tarifaActualizada);
    }

    public void eliminar(Integer id) {
        if (!tarifaRepository.existsById(id)) {
            throw new RuntimeException("Tarifa no encontrada con ID: " + id);
        }
        tarifaRepository.deleteById(id);
    }

}
