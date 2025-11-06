package tpi_grupo46.recursos.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tpi_grupo46.recursos.domain.Deposito;
import tpi_grupo46.recursos.dto.DepositoDTO;

@Mapper
public interface DepositoMapper {

    DepositoMapper INSTANCE = Mappers.getMapper(DepositoMapper.class);

    DepositoDTO toDTO(Deposito deposito);

    Deposito toEntity(DepositoDTO depositoDTO);

}
