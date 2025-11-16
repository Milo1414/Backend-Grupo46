package tpi_grupo46.logistica.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tpi_grupo46.logistica.application.TramoService;
import tpi_grupo46.logistica.dto.tramo.*;
import tpi_grupo46.logistica.exception.ErrorResponse;
import tpi_grupo46.logistica.infrastructure.mapper.LogisticaMapper;
import tpi_grupo46.logistica.infrastructure.repository.TramoRepository;

import java.util.List;

/**
 * Controlador REST para la gestión de tramos de rutas.
 * Forma parte de la capa API y coordina solicitudes de gestión de tramos,
 * delegando la lógica de negocio a TramoService. Proporciona endpoints
 * para asignar recursos y gestionar el progreso de tramos con validación.
 */
@RestController
@RequestMapping("/api/v1/tramos")
@RequiredArgsConstructor
@Tag(name = "Tramos", description = "API para la gestión de tramos de rutas")
public class TramoController {

  private final TramoService tramoService;
  private final TramoRepository tramoRepository;
  private final LogisticaMapper mapper;

  /**
   * Obtiene un tramo por su ID.
   *
   * @param id ID del tramo
   * @return TramoDTO con HTTP 200 o 404 si no existe
   */
  @GetMapping("/{id}")
  @Operation(summary = "Obtener tramo por ID", description = "Recupera los detalles de un tramo específico")
  @ApiResponse(responseCode = "200", description = "Tramo encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TramoDTO.class)))
  @ApiResponse(responseCode = "404", description = "Tramo no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  public ResponseEntity<TramoDTO> obtenerTramo(
      @PathVariable @Parameter(description = "ID del tramo", example = "1") Long id) {
    var tramo = tramoRepository.findById(id)
        .orElseThrow(() -> new tpi_grupo46.logistica.exception.EntityNotFoundException(
            "Tramo con ID " + id + " no encontrado"));
    return ResponseEntity.ok(mapper.tramoToDto(tramo));
  }

  /**
   * Obtiene todos los tramos de una ruta específica.
   *
   * @param rutaId ID de la ruta
   * @return Lista de TramoDTO
   */
  @GetMapping("/ruta/{rutaId}")
  @Operation(summary = "Obtener tramos por ruta", description = "Recupera todos los tramos de una ruta específica")
  @ApiResponse(responseCode = "200", description = "Tramos encontrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TramoDTO.class)))
  public ResponseEntity<List<TramoDTO>> obtenerTramosPorRuta(
      @PathVariable @Parameter(description = "ID de la ruta", example = "1") Long rutaId) {
    var tramos = tramoRepository.findByRutaId(rutaId);
    return ResponseEntity.ok(tramos.stream()
        .map(mapper::tramoToDto)
        .toList());
  }

  /**
   * Obtiene todos los tramos asignados a un camión.
   *
   * @param camionId ID del camión (dominio/patente)
   * @return Lista de TramoDTO
   */
  @GetMapping("/camion/{camionId}")
  @Operation(summary = "Obtener tramos por camión", description = "Recupera todos los tramos asignados a un camión específico")
  @ApiResponse(responseCode = "200", description = "Tramos encontrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TramoDTO.class)))
  public ResponseEntity<List<TramoDTO>> obtenerTramosPorCamion(
      @PathVariable @Parameter(description = "ID del camión (dominio/patente)", example = "ABC-123") String camionId) {
    var tramos = tramoRepository.findByCamionId(camionId);
    return ResponseEntity.ok(tramos.stream()
        .map(mapper::tramoToDto)
        .toList());
  }

  /**
   * Asigna un camión a un tramo.
   * Valida que el camionId sea válido y positivo.
   *
   * @param id               ID del tramo
   * @param asignarCamionDTO Datos validados de asignación (camionId)
   * @return TramoDTO actualizado
   */
  @PutMapping("/{id}/camion")
  @Operation(summary = "Asignar camión a tramo", description = "Asigna un camión específico a un tramo de ruta")
  @ApiResponse(responseCode = "200", description = "Camión asignado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TramoDTO.class)))
  @ApiResponse(responseCode = "404", description = "Tramo no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  public ResponseEntity<TramoDTO> asignarCamion(
      @PathVariable @Parameter(description = "ID del tramo", example = "1") Long id,
      @Valid @RequestBody AsignarCamionDTO asignarCamionDTO) {
    var tramoActualizado = tramoService.asignarCamion(id, asignarCamionDTO.camionId());
    return ResponseEntity.ok(mapper.tramoToDto(tramoActualizado));
  }

  /**
   * Inicia el recorrido de un tramo.
   * Valida que la fecha/hora de inicio sea proporcionada.
   *
   * @param id        ID del tramo
   * @param inicioDTO Datos validados de inicio (fecha y hora)
   * @return TramoDTO actualizado
   */
  @PutMapping("/{id}/inicio")
  @Operation(summary = "Iniciar tramo", description = "Registra el inicio del recorrido de un tramo")
  @ApiResponse(responseCode = "200", description = "Tramo iniciado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TramoDTO.class)))
  @ApiResponse(responseCode = "404", description = "Tramo no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  public ResponseEntity<TramoDTO> iniciarTramo(
      @PathVariable @Parameter(description = "ID del tramo", example = "1") Long id,
      @Valid @RequestBody InicioTramoDTO inicioDTO) {
    var tramoActualizado = tramoService.iniciarTramo(id, inicioDTO.fechaHoraInicio());
    return ResponseEntity.ok(mapper.tramoToDto(tramoActualizado));
  }

  /**
   * Finaliza el recorrido de un tramo.
   * Valida que los datos de finalización sean válidos (fechaHoraFin, costoReal).
   *
   * @param id     ID del tramo
   * @param finDTO Datos validados de finalización (fecha/hora final y costo real)
   * @return TramoDTO actualizado
   */
  @PutMapping("/{id}/fin")
  @Operation(summary = "Finalizar tramo", description = "Registra el fin del recorrido de un tramo incluyendo datos reales de costo")
  @ApiResponse(responseCode = "200", description = "Tramo finalizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TramoDTO.class)))
  @ApiResponse(responseCode = "404", description = "Tramo no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  public ResponseEntity<TramoDTO> finalizarTramo(
      @PathVariable @Parameter(description = "ID del tramo", example = "1") Long id,
      @Valid @RequestBody FinTramoDTO finDTO) {
    var tramoActualizado = tramoService.finalizarTramo(id, finDTO.fechaHoraFin(), finDTO.costoReal());
    return ResponseEntity.ok(mapper.tramoToDto(tramoActualizado));
  }

  // ============================================================================
  // ENDPOINTS LEGACY (DEPRECATED) - Mantener para compatibilidad hacia atrás
  // TODO: Eliminar en versión 2.0
  // ============================================================================

  /**
   * @deprecated Usar PUT /api/v1/tramos/{id}/camion
   */
  @PutMapping("/{id}/asignar-camion")
  @Deprecated(forRemoval = true)
  public ResponseEntity<TramoDTO> asignarCamionLegacy(
      @PathVariable Long id,
      @Valid @RequestBody AsignarCamionDTO asignarCamionDTO) {
    return asignarCamion(id, asignarCamionDTO);
  }

  /**
   * @deprecated Usar PUT /api/v1/tramos/{id}/inicio
   */
  @PutMapping("/{id}/iniciar")
  @Deprecated(forRemoval = true)
  public ResponseEntity<TramoDTO> iniciarTramoLegacy(
      @PathVariable Long id,
      @Valid @RequestBody InicioTramoDTO inicioDTO) {
    return iniciarTramo(id, inicioDTO);
  }

  /**
   * @deprecated Usar PUT /api/v1/tramos/{id}/fin
   */
  @PutMapping("/{id}/finalizar")
  @Deprecated(forRemoval = true)
  public ResponseEntity<TramoDTO> finalizarTramoLegacy(
      @PathVariable Long id,
      @Valid @RequestBody FinTramoDTO finDTO) {
    return finalizarTramo(id, finDTO);
  }
}
