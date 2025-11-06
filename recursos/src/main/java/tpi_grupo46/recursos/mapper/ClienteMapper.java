package tpi_grupo46.recursos.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tpi_grupo46.recursos.domain.Cliente;
import tpi_grupo46.recursos.dto.ClienteDTO;

@Mapper
public interface ClienteMapper {

    ClienteMapper INSTANCE = Mappers.getMapper(ClienteMapper.class);

    ClienteDTO toDTO(Cliente cliente);

    Cliente toEntity(ClienteDTO clienteDTO);

}
