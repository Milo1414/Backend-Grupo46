package tpi_grupo46.logistica.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tpi_grupo46.logistica.domain.model.Solicitud;
import tpi_grupo46.logistica.domain.model.Ruta;
import tpi_grupo46.logistica.domain.model.Tramo;
import tpi_grupo46.logistica.domain.model.CambioEstado;
import tpi_grupo46.logistica.dto.SolicitudDTO;
import tpi_grupo46.logistica.dto.RutaDTO;
import tpi_grupo46.logistica.dto.TramoDTO;
import tpi_grupo46.logistica.dto.CambioEstadoDTO;

/**
 * Mapper para convertir entre entidades y DTOs usando MapStruct
 */
@Mapper(componentModel = "spring")
public interface LogisticaMapper {

  // Solicitud mappings
  SolicitudDTO solicitudToDto(Solicitud solicitud);

  Solicitud dtoToSolicitud(SolicitudDTO dto);

  // Ruta mappings
  RutaDTO rutaToDto(Ruta ruta);

  Ruta dtoToRuta(RutaDTO dto);

  // Tramo mappings
  TramoDTO tramoToDto(Tramo tramo);

  Tramo dtoToTramo(TramoDTO dto);

  // CambioEstado mappings
  CambioEstadoDTO cambioEstadoToDto(CambioEstado cambioEstado);

  CambioEstado dtoToCambioEstado(CambioEstadoDTO dto);
}
