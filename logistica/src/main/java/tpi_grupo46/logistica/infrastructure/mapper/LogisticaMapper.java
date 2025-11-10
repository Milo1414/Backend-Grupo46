package tpi_grupo46.logistica.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
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

  @Mapping(target = "cambiosEstado", ignore = true)
  Solicitud dtoToSolicitud(SolicitudDTO dto);

  // Ruta mappings
  RutaDTO rutaToDto(Ruta ruta);

  @Mapping(target = "solicitud", ignore = true)
  Ruta dtoToRuta(RutaDTO dto);

  // Tramo mappings
  TramoDTO tramoToDto(Tramo tramo);

  @Mapping(target = "ruta", ignore = true)
  @Mapping(target = "camionId", ignore = true)
  Tramo dtoToTramo(TramoDTO dto);

  // CambioEstado mappings
  @Mapping(source = "estado.codigo", target = "estadoCodigo")
  CambioEstadoDTO cambioEstadoToDto(CambioEstado cambioEstado);

  @Mapping(target = "estado", ignore = true)
  @Mapping(target = "solicitud", ignore = true)
  @Mapping(target = "tramo", ignore = true)
  @Mapping(target = "contenedorId", ignore = true)
  CambioEstado dtoToCambioEstado(CambioEstadoDTO dto);

  /**
   * Obtiene el estado actual de una solicitud a partir del CambioEstado con fechaFin == null.
   * Si no hay cambios activos, retorna "SIN_ESTADO".
   */
  @Named("obtenerEstadoActual")
  default String obtenerEstadoActualMethod(Solicitud solicitud) {
    if (solicitud == null || solicitud.getCambiosEstado() == null || solicitud.getCambiosEstado().isEmpty()) {
      return "SIN_ESTADO";
    }
    // Busca el CambioEstado con fechaFin == null (estado actual)
    return solicitud.getCambiosEstado()
        .stream()
        .filter(cambio -> cambio.getFechaFin() == null)
        .findFirst()
        .map(cambio -> cambio.getEstado().getCodigo())
        .orElse("SIN_ESTADO");
  }
}

  }
}
