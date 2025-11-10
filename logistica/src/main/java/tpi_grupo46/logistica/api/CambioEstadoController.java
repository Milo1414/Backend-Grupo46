package tpi_grupo46.logistica.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tpi_grupo46.logistica.domain.enums.EstadoSolicitud;
import tpi_grupo46.logistica.dto.cambioestado.CambioEstadoDTO;
import tpi_grupo46.logistica.exception.ErrorResponse;
import tpi_grupo46.logistica.infrastructure.mapper.LogisticaMapper;
import tpi_grupo46.logistica.infrastructure.repository.CambioEstadoRepository;

import java.util.List;

/**
 * Controlador REST para consultar el historial de cambios de estado.
 * Forma parte de la capa API y proporciona endpoints de solo lectura
 * para auditoría y trazabilidad del ciclo de vida de las solicitudes.
 */
@RestController
@RequestMapping("/api/v1/cambios-estado")
@RequiredArgsConstructor
@Tag(name = "Cambios de Estado", description = "API para consultar el historial de cambios de estado")
public class CambioEstadoController {

  private final CambioEstadoRepository cambioEstadoRepository;
  private final LogisticaMapper mapper;

  /**
   * Obtiene un cambio de estado por su ID.
   *
   * @param id ID del cambio de estado
   * @return CambioEstadoDTO con HTTP 200 o 404 si no existe
   */
  @GetMapping("/{id}")
  @Operation(summary = "Obtener cambio de estado por ID", description = "Recupera los detalles de un cambio de estado específico")
  @ApiResponse(responseCode = "200", description = "Cambio de estado encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CambioEstadoDTO.class)))
  @ApiResponse(responseCode = "404", description = "Cambio de estado no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  public ResponseEntity<CambioEstadoDTO> obtenerCambioEstado(
      @PathVariable @Parameter(description = "ID del cambio de estado", example = "1") Long id) {
    var cambio = cambioEstadoRepository.findById(id)
        .orElseThrow(() -> new tpi_grupo46.logistica.exception.EntityNotFoundException(
            "Cambio de estado con ID " + id + " no encontrado"));
    return ResponseEntity.ok(mapper.cambioEstadoToDto(cambio));
  }

  /**
   * Obtiene todos los cambios de estado de un estado específico.
   *
   * @param estado Estado a filtrar
   * @return Lista de CambioEstadoDTO
   */
  @GetMapping("/estado/{estado}")
  @Operation(summary = "Obtener cambios de estado por estado", description = "Recupera todos los cambios que resultan en un estado específico")
  @ApiResponse(responseCode = "200", description = "Cambios encontrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CambioEstadoDTO.class)))
  public ResponseEntity<List<CambioEstadoDTO>> obtenerCambiosPorEstado(
      @PathVariable @Parameter(description = "Estado a filtrar", example = "ENTREGADA") EstadoSolicitud estado) {
    var cambios = cambioEstadoRepository.findByEstado(estado);
    return ResponseEntity.ok(cambios.stream()
        .map(mapper::cambioEstadoToDto)
        .toList());
  }
}
