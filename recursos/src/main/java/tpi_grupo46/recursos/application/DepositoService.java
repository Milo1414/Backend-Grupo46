package tpi_grupo46.recursos.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tpi_grupo46.recursos.domain.Deposito;
import tpi_grupo46.recursos.dto.DepositoDTO;
import tpi_grupo46.recursos.infrastructure.repository.DepositoRepository;
import tpi_grupo46.recursos.mapper.DepositoMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DepositoService {

    private final DepositoRepository depositoRepository;
    private final DepositoMapper depositoMapper = DepositoMapper.INSTANCE;

    public List<DepositoDTO> obtenerTodos() {
        return depositoRepository.findAll().stream()
                .map(depositoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DepositoDTO obtenerPorId(Integer id) {
        return depositoRepository.findById(id)
                .map(depositoMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Deposito no encontrado con ID: " + id));
    }

    public DepositoDTO crear(DepositoDTO depositoDTO) {
        Deposito deposito = depositoMapper.toEntity(depositoDTO);
        Deposito depositoGuardado = depositoRepository.save(deposito);
        return depositoMapper.toDTO(depositoGuardado);
    }

    public DepositoDTO actualizar(Integer id, DepositoDTO depositoDTO) {
        Deposito deposito = depositoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deposito no encontrado con ID: " + id));
        
        deposito.setLatitudDeposito(depositoDTO.getLatitudDeposito());
        deposito.setLongitudDeposito(depositoDTO.getLongitudDeposito());
        deposito.setCalleDeposito(depositoDTO.getCalleDeposito());
        deposito.setNroDeposito(depositoDTO.getNroDeposito());
        deposito.setCostoDiaTransportista(depositoDTO.getCostoDiaTransportista());
        
        Deposito depositoActualizado = depositoRepository.save(deposito);
        return depositoMapper.toDTO(depositoActualizado);
    }

    public void eliminar(Integer id) {
        if (!depositoRepository.existsById(id)) {
            throw new RuntimeException("Deposito no encontrado con ID: " + id);
        }
        depositoRepository.deleteById(id);
    }

}
