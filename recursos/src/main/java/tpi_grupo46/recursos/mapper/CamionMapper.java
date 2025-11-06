package tpi_grupo46.recursos.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tpi_grupo46.recursos.domain.Camion;
import tpi_grupo46.recursos.dto.CamionDTO;

@Mapper
public interface CamionMapper {

    CamionMapper INSTANCE = Mappers.getMapper(CamionMapper.class);

    CamionDTO toDTO(Camion camion);

    Camion toEntity(CamionDTO camionDTO);

}
