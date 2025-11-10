package tpi_grupo46.logistica.infrastructure.mapper;

import org.mapstruct.Mapper;
import tpi_grupo46.logistica.domain.model.Solicitud;
import tpi_grupo46.logistica.domain.model.Ruta;
import tpi_grupo46.logistica.domain.model.Tramo;
import tpi_grupo46.logistica.domain.model.CambioEstado;
import tpi_grupo46.logistica.dto.solicitud.SolicitudDTO;
import tpi_grupo46.logistica.dto.ruta.RutaDTO;
import tpi_grupo46.logistica.dto.tramo.TramoDTO;
import tpi_grupo46.logistica.dto.cambioestado.CambioEstadoDTO;

/**
 * Mapper de infraestructura que realiza la conversión entre
 * entidades del dominio y DTOs de la capa API.
 * Utiliza MapStruct para simplificar el mapeo.
 * 
 * Centraliza la lógica de transformación entre la capa de dominio y la capa de
 * presentación, asegurando consistencia en el mapeo de datos.
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
