package tpi_grupo46.recursos.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import tpi_grupo46.recursos.domain.Contenedor;
import tpi_grupo46.recursos.dto.ContenedorDTO;

@Mapper
public interface ContenedorMapper {

    ContenedorMapper INSTANCE = Mappers.getMapper(ContenedorMapper.class);

    @Mapping(source = "cliente.idCliente", target = "idCliente")
    ContenedorDTO toDTO(Contenedor contenedor);

    @Mapping(source = "idCliente", target = "cliente.idCliente")
    Contenedor toEntity(ContenedorDTO contenedorDTO);

}
