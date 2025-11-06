package tpi_grupo46.recursos.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tpi_grupo46.recursos.domain.Tarifa;
import tpi_grupo46.recursos.dto.TarifaDTO;

@Mapper
public interface TarifaMapper {

    TarifaMapper INSTANCE = Mappers.getMapper(TarifaMapper.class);

    TarifaDTO toDTO(Tarifa tarifa);

    Tarifa toEntity(TarifaDTO tarifaDTO);

}
