package tpi_grupo46.recursos.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tpi_grupo46.recursos.domain.Tarifa;
import tpi_grupo46.recursos.domain.TarifaRango;
import tpi_grupo46.recursos.dto.TarifaRangoDTO;
import tpi_grupo46.recursos.infrastructure.repository.TarifaRangoRepository;
import tpi_grupo46.recursos.infrastructure.repository.TarifaRepository;
import tpi_grupo46.recursos.mapper.TarifaRangoMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TarifaRangoService {

    private final TarifaRangoRepository tarifaRangoRepository;
    private final TarifaRepository tarifaRepository;
    private final TarifaRangoMapper tarifaRangoMapper = TarifaRangoMapper.INSTANCE;

    public List<TarifaRangoDTO> obtenerTodos() {
        return tarifaRangoRepository.findAll().stream()
                .map(tarifaRangoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TarifaRangoDTO obtenerPorId(Integer id) {
        return tarifaRangoRepository.findById(id)
                .map(tarifaRangoMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("TarifaRango no encontrado con ID: " + id));
    }

    public TarifaRangoDTO crear(TarifaRangoDTO tarifaRangoDTO) {
        Tarifa tarifa = tarifaRepository.findById(tarifaRangoDTO.getIdTarifa())
                .orElseThrow(() -> new RuntimeException("Tarifa no encontrada con ID: " + tarifaRangoDTO.getIdTarifa()));
        
        TarifaRango tarifaRango = tarifaRangoMapper.toEntity(tarifaRangoDTO);
        tarifaRango.setTarifa(tarifa);
        TarifaRango tarifaRangoGuardado = tarifaRangoRepository.save(tarifaRango);
        return tarifaRangoMapper.toDTO(tarifaRangoGuardado);
    }

    public TarifaRangoDTO actualizar(Integer id, TarifaRangoDTO tarifaRangoDTO) {
        TarifaRango tarifaRango = tarifaRangoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TarifaRango no encontrado con ID: " + id));
        
        tarifaRango.setMinPesoKg(tarifaRangoDTO.getMinPesoKg());
        tarifaRango.setMaxPesoKg(tarifaRangoDTO.getMaxPesoKg());
        tarifaRango.setMinVolumenM3(tarifaRangoDTO.getMinVolumenM3());
        tarifaRango.setMaxVolumenM3(tarifaRangoDTO.getMaxVolumenM3());
        tarifaRango.setFactorCamion(tarifaRangoDTO.getFactorCamion());
        
        if (tarifaRangoDTO.getIdTarifa() != null) {
            Tarifa tarifa = tarifaRepository.findById(tarifaRangoDTO.getIdTarifa())
                    .orElseThrow(() -> new RuntimeException("Tarifa no encontrada con ID: " + tarifaRangoDTO.getIdTarifa()));
            tarifaRango.setTarifa(tarifa);
        }
        
        TarifaRango tarifaRangoActualizado = tarifaRangoRepository.save(tarifaRango);
        return tarifaRangoMapper.toDTO(tarifaRangoActualizado);
    }

    public void eliminar(Integer id) {
        if (!tarifaRangoRepository.existsById(id)) {
            throw new RuntimeException("TarifaRango no encontrado con ID: " + id);
        }
        tarifaRangoRepository.deleteById(id);
    }

}
