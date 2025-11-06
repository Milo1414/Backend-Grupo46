package tpi_grupo46.recursos.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tpi_grupo46.recursos.domain.Camion;
import tpi_grupo46.recursos.dto.CamionDTO;
import tpi_grupo46.recursos.infrastructure.repository.CamionRepository;
import tpi_grupo46.recursos.mapper.CamionMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CamionService {

    private final CamionRepository camionRepository;
    private final CamionMapper camionMapper = CamionMapper.INSTANCE;

    public List<CamionDTO> obtenerTodos() {
        return camionRepository.findAll().stream()
                .map(camionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CamionDTO obtenerPorId(String dominio) {
        return camionRepository.findById(dominio)
                .map(camionMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Camion no encontrado con dominio: " + dominio));
    }

    public CamionDTO crear(CamionDTO camionDTO) {
        Camion camion = camionMapper.toEntity(camionDTO);
        Camion camionGuardado = camionRepository.save(camion);
        return camionMapper.toDTO(camionGuardado);
    }

    public CamionDTO actualizar(String dominio, CamionDTO camionDTO) {
        Camion camion = camionRepository.findById(dominio)
                .orElseThrow(() -> new RuntimeException("Camion no encontrado con dominio: " + dominio));
        
        camion.setCapacidadPeso(camionDTO.getCapacidadPeso());
        camion.setCapacidadVolumen(camionDTO.getCapacidadVolumen());
        camion.setDisponibilidad(camionDTO.getDisponibilidad());
        camion.setNombreTransportista(camionDTO.getNombreTransportista());
        camion.setTelefonoTransportista(camionDTO.getTelefonoTransportista());
        camion.setCostoBaseKm(camionDTO.getCostoBaseKm());
        camion.setConsumoLKm(camionDTO.getConsumoLKm());
        
        Camion camionActualizado = camionRepository.save(camion);
        return camionMapper.toDTO(camionActualizado);
    }

    public void eliminar(String dominio) {
        if (!camionRepository.existsById(dominio)) {
            throw new RuntimeException("Camion no encontrado con dominio: " + dominio);
        }
        camionRepository.deleteById(dominio);
    }

}
