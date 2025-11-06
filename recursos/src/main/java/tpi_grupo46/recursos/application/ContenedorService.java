package tpi_grupo46.recursos.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tpi_grupo46.recursos.domain.Cliente;
import tpi_grupo46.recursos.domain.Contenedor;
import tpi_grupo46.recursos.dto.ContenedorDTO;
import tpi_grupo46.recursos.infrastructure.repository.ClienteRepository;
import tpi_grupo46.recursos.infrastructure.repository.ContenedorRepository;
import tpi_grupo46.recursos.mapper.ContenedorMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ContenedorService {

    private final ContenedorRepository contenedorRepository;
    private final ClienteRepository clienteRepository;
    private final ContenedorMapper contenedorMapper = ContenedorMapper.INSTANCE;

    public List<ContenedorDTO> obtenerTodos() {
        return contenedorRepository.findAll().stream()
                .map(contenedorMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ContenedorDTO obtenerPorId(Integer id) {
        return contenedorRepository.findById(id)
                .map(contenedorMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Contenedor no encontrado con ID: " + id));
    }

    public ContenedorDTO crear(ContenedorDTO contenedorDTO) {
        Cliente cliente = clienteRepository.findById(contenedorDTO.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + contenedorDTO.getIdCliente()));
        
        Contenedor contenedor = contenedorMapper.toEntity(contenedorDTO);
        contenedor.setCliente(cliente);
        Contenedor contenedorGuardado = contenedorRepository.save(contenedor);
        return contenedorMapper.toDTO(contenedorGuardado);
    }

    public ContenedorDTO actualizar(Integer id, ContenedorDTO contenedorDTO) {
        Contenedor contenedor = contenedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contenedor no encontrado con ID: " + id));
        
        contenedor.setPeso(contenedorDTO.getPeso());
        contenedor.setVolumen(contenedorDTO.getVolumen());
        contenedor.setIdEstadoContenedor(contenedorDTO.getIdEstadoContenedor());
        contenedor.setClienteAsociado(contenedorDTO.getClienteAsociado());
        contenedor.setFechaCreacion(contenedorDTO.getFechaCreacion());
        
        if (contenedorDTO.getIdCliente() != null) {
            Cliente cliente = clienteRepository.findById(contenedorDTO.getIdCliente())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + contenedorDTO.getIdCliente()));
            contenedor.setCliente(cliente);
        }
        
        Contenedor contenedorActualizado = contenedorRepository.save(contenedor);
        return contenedorMapper.toDTO(contenedorActualizado);
    }

    public void eliminar(Integer id) {
        if (!contenedorRepository.existsById(id)) {
            throw new RuntimeException("Contenedor no encontrado con ID: " + id);
        }
        contenedorRepository.deleteById(id);
    }

}
