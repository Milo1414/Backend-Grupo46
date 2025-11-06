package tpi_grupo46.recursos.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import tpi_grupo46.recursos.domain.TarifaRango;
import tpi_grupo46.recursos.dto.TarifaRangoDTO;

@Mapper
public interface TarifaRangoMapper {

    TarifaRangoMapper INSTANCE = Mappers.getMapper(TarifaRangoMapper.class);

    @Mapping(source = "tarifa.idTarifa", target = "idTarifa")
    TarifaRangoDTO toDTO(TarifaRango tarifaRango);

    @Mapping(source = "idTarifa", target = "tarifa.idTarifa")
    TarifaRango toEntity(TarifaRangoDTO tarifaRangoDTO);

}
