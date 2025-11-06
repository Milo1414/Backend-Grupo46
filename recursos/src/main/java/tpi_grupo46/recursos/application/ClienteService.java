package tpi_grupo46.recursos.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tpi_grupo46.recursos.domain.Cliente;
import tpi_grupo46.recursos.dto.ClienteDTO;
import tpi_grupo46.recursos.infrastructure.repository.ClienteRepository;
import tpi_grupo46.recursos.mapper.ClienteMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper = ClienteMapper.INSTANCE;

    public List<ClienteDTO> obtenerTodos() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ClienteDTO obtenerPorId(Integer id) {
        return clienteRepository.findById(id)
                .map(clienteMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
    }

    public ClienteDTO crear(ClienteDTO clienteDTO) {
        Cliente cliente = clienteMapper.toEntity(clienteDTO);
        Cliente clienteGuardado = clienteRepository.save(cliente);
        return clienteMapper.toDTO(clienteGuardado);
    }

    public ClienteDTO actualizar(Integer id, ClienteDTO clienteDTO) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        
        cliente.setNombre(clienteDTO.getNombre());
        cliente.setApellido(clienteDTO.getApellido());
        cliente.setTipoDoc(clienteDTO.getTipoDoc());
        cliente.setNroDoc(clienteDTO.getNroDoc());
        cliente.setTelefono(clienteDTO.getTelefono());
        cliente.setMail(clienteDTO.getMail());
        
        Cliente clienteActualizado = clienteRepository.save(cliente);
        return clienteMapper.toDTO(clienteActualizado);
    }

    public void eliminar(Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado con ID: " + id);
        }
        clienteRepository.deleteById(id);
    }

}
