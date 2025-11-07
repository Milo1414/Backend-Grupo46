package tpi_grupo46.logistica.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tpi_grupo46.logistica.application.SolicitudService;
import tpi_grupo46.logistica.domain.enums.EstadoSolicitud;
import tpi_grupo46.logistica.dto.*;
import tpi_grupo46.logistica.exception.ErrorResponse;
import tpi_grupo46.logistica.mapper.LogisticaMapper;
import tpi_grupo46.logistica.infrastructure.repository.SolicitudRepository;

import java.util.List;

/**
 * Controlador REST para la gestión de solicitudes de transporte.
 * Proporciona endpoints para crear, consultar y actualizar solicitudes.
 */
@RestController
@RequestMapping("/api/v1/solicitudes")
@RequiredArgsConstructor
@Tag(name = "Solicitudes", description = "API para la gestión de solicitudes de transporte")
public class SolicitudController {

  private final SolicitudService solicitudService;
  private final SolicitudRepository solicitudRepository;
  private final LogisticaMapper mapper;

  /**
   * Crea una nueva solicitud de transporte.
   *
   * @param solicitudDTO Datos de la solicitud a crear
   * @return SolicitudDTO creada con HTTP 201
   */
  @PostMapping
  @Operation(summary = "Crear nueva solicitud", description = "Crea una nueva solicitud de transporte en estado BORRADOR")
  @ApiResponse(responseCode = "201", description = "Solicitud creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SolicitudDTO.class)))
  @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  public ResponseEntity<SolicitudDTO> crearSolicitud(@RequestBody CrearSolicitudDTO solicitudDTO) {
    var solicitud = solicitudService.crearSolicitud(solicitudDTO.clienteId(), solicitudDTO.contenedorId());
    return ResponseEntity.status(HttpStatus.CREATED).body(mapper.solicitudToDto(solicitud));
  }

  /**
   * Obtiene una solicitud por su ID.
   *
   * @param id ID de la solicitud
   * @return SolicitudDTO con HTTP 200 o 404 si no existe
   */
  @GetMapping("/{id}")
  @Operation(summary = "Obtener solicitud por ID", description = "Recupera los detalles de una solicitud específica")
  @ApiResponse(responseCode = "200", description = "Solicitud encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SolicitudDTO.class)))
  @ApiResponse(responseCode = "404", description = "Solicitud no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  public ResponseEntity<SolicitudDTO> obtenerSolicitud(
      @PathVariable @Parameter(description = "ID de la solicitud", example = "1") Long id) {
    var solicitud = solicitudRepository.findById(id)
        .orElseThrow(() -> new tpi_grupo46.logistica.exception.EntityNotFoundException(
            "Solicitud con ID " + id + " no encontrada"));
    return ResponseEntity.ok(mapper.solicitudToDto(solicitud));
  }

  /**
   * Obtiene todas las solicitudes de un cliente.
   *
   * @param clienteId ID del cliente
   * @return Lista de SolicitudDTO
   */
  @GetMapping("/cliente/{clienteId}")
  @Operation(summary = "Obtener solicitudes por cliente", description = "Recupera todas las solicitudes de un cliente específico")
  @ApiResponse(responseCode = "200", description = "Solicitudes encontradas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SolicitudDTO.class)))
  public ResponseEntity<List<SolicitudDTO>> obtenerSolicitudesPorCliente(
      @PathVariable @Parameter(description = "ID del cliente", example = "1") Long clienteId) {
    var solicitudes = solicitudService.obtenerSolicitudesPorCliente(clienteId);
    return ResponseEntity.ok(solicitudes.stream()
        .map(mapper::solicitudToDto)
        .toList());
  }

  /**
   * Obtiene solicitudes filtradas por estado.
   *
   * @param estado Estado de la solicitud (BORRADOR, PROGRAMADA, EN_TRANSITO,
   *               ENTREGADA, CANCELADA)
   * @return Lista de SolicitudDTO
   */
  @GetMapping("/estado/{estado}")
  @Operation(summary = "Obtener solicitudes por estado", description = "Recupera todas las solicitudes con un estado específico")
  @ApiResponse(responseCode = "200", description = "Solicitudes encontradas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SolicitudDTO.class)))
  public ResponseEntity<List<SolicitudDTO>> obtenerSolicitudesPorEstado(
      @PathVariable @Parameter(description = "Estado de la solicitud", example = "PROGRAMADA") EstadoSolicitud estado) {
    var solicitudes = solicitudService.obtenerSolicitudesPorEstado(estado);
    return ResponseEntity.ok(solicitudes.stream()
        .map(mapper::solicitudToDto)
        .toList());
  }

  /**
   * Obtiene el historial de cambios de estado de una solicitud.
   *
   * @param id ID de la solicitud
   * @return Lista de CambioEstadoDTO ordenados cronológicamente
   */
  @GetMapping("/{id}/historial")
  @Operation(summary = "Obtener historial de cambios de estado", description = "Recupera el historial completo de cambios de estado de una solicitud")
  @ApiResponse(responseCode = "200", description = "Historial encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CambioEstadoDTO.class)))
  @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
  public ResponseEntity<List<CambioEstadoDTO>> obtenerHistorialCambios(
      @PathVariable @Parameter(description = "ID de la solicitud", example = "1") Long id) {
    var cambios = solicitudService.obtenerHistorialCambios(id);
    return ResponseEntity.ok(cambios.stream()
        .map(mapper::cambioEstadoToDto)
        .toList());
  }

  /**
   * Programa una solicitud (transición a estado PROGRAMADA).
   * Esta operación asigna el costo estimado y tiempo estimado.
   *
   * @param id              ID de la solicitud
   * @param programacionDTO Datos de programación
   * @return SolicitudDTO actualizada
   */
  @PutMapping("/{id}/programar")
  @Operation(summary = "Programar solicitud", description = "Cambia el estado de la solicitud a PROGRAMADA y asigna costos y tiempos estimados")
  @ApiResponse(responseCode = "200", description = "Solicitud programada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SolicitudDTO.class)))
  @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
  @ApiResponse(responseCode = "400", description = "Solicitud en estado inválido")
  public ResponseEntity<SolicitudDTO> programarSolicitud(
      @PathVariable @Parameter(description = "ID de la solicitud", example = "1") Long id,
      @RequestBody ProgramacionDTO programacionDTO) {
    var solicitud = solicitudRepository.findById(id)
        .orElseThrow(() -> new tpi_grupo46.logistica.exception.EntityNotFoundException(
            "Solicitud con ID " + id + " no encontrada"));

    solicitud.setCostoEstimado(programacionDTO.costoEstimado());
    solicitud.setTiempoEstimadoHoras(programacionDTO.tiempoEstimadoHoras());
    solicitud.setEstado(EstadoSolicitud.PROGRAMADA);

    solicitudService.guardarCambioEstado(solicitud, EstadoSolicitud.PROGRAMADA);
    var solicitudGuardada = solicitudRepository.save(solicitud);

    return ResponseEntity.ok(mapper.solicitudToDto(solicitudGuardada));
  }

  /**
   * Marca una solicitud como entregada (transición a estado ENTREGADA).
   * Esta operación finaliza la entrega y registra costos y tiempos reales.
   *
   * @param id              ID de la solicitud
   * @param finalizacionDTO Datos de finalización
   * @return SolicitudDTO actualizada
   */
  @PutMapping("/{id}/entregar")
  @Operation(summary = "Finalizar entrega de solicitud", description = "Cambia el estado de la solicitud a ENTREGADA y registra los costos y tiempos reales")
  @ApiResponse(responseCode = "200", description = "Solicitud entregada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SolicitudDTO.class)))
  @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
  @ApiResponse(responseCode = "400", description = "Solicitud en estado inválido")
  public ResponseEntity<SolicitudDTO> entregarSolicitud(
      @PathVariable @Parameter(description = "ID de la solicitud", example = "1") Long id,
      @RequestBody FinalizacionDTO finalizacionDTO) {
    var solicitud = solicitudRepository.findById(id)
        .orElseThrow(() -> new tpi_grupo46.logistica.exception.EntityNotFoundException(
            "Solicitud con ID " + id + " no encontrada"));

    solicitud.setCostoFinal(finalizacionDTO.costoFinal());
    solicitud.setTiempoRealHoras(finalizacionDTO.tiempoRealHoras());
    solicitud.setEstado(EstadoSolicitud.ENTREGADA);

    solicitudService.guardarCambioEstado(solicitud, EstadoSolicitud.ENTREGADA);
    var solicitudGuardada = solicitudRepository.save(solicitud);

    return ResponseEntity.ok(mapper.solicitudToDto(solicitudGuardada));
  }
}
