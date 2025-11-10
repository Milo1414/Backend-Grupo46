package tpi_grupo46.logistica.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tpi_grupo46.logistica.application.RutaService;
import tpi_grupo46.logistica.domain.model.Tramo;
import tpi_grupo46.logistica.dto.ruta.CrearRutaDTO;
import tpi_grupo46.logistica.dto.ruta.RutaDTO;
import tpi_grupo46.logistica.dto.tramo.CrearTramoDTO;
import tpi_grupo46.logistica.exception.ErrorResponse;
import tpi_grupo46.logistica.infrastructure.mapper.LogisticaMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador REST para la gestión de rutas de transporte.
 * Forma parte de la capa API y coordina solicitudes de gestión de rutas,
 * delegando la lógica de negocio a los servicios correspondientes.
 * Proporciona endpoints para crear y consultar rutas con validación de entrada.
 */
@RestController
@RequestMapping("/api/v1/rutas")
@RequiredArgsConstructor
@Tag(name = "Rutas", description = "API para la gestión de rutas de transporte")
public class RutaController {

  private final RutaService rutaService;
  private final LogisticaMapper mapper;

  /**
   * Obtiene una ruta por su ID.
   *
   * @param id ID de la ruta
   * @return RutaDTO con HTTP 200 o 404 si no existe
   */
  @GetMapping("/{id}")
  @Operation(summary = "Obtener ruta por ID", description = "Recupera los detalles de una ruta específica incluyendo sus tramos")
  @ApiResponse(responseCode = "200", description = "Ruta encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RutaDTO.class)))
  @ApiResponse(responseCode = "404", description = "Ruta no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  public ResponseEntity<RutaDTO> obtenerRuta(
      @PathVariable @Parameter(description = "ID de la ruta", example = "1") Long id) {
    var ruta = rutaService.obtenerRuta(id);
    return ResponseEntity.ok(mapper.rutaToDto(ruta));
  }

  /**
   * Crea una nueva ruta para una solicitud.
   * Valida todos los datos de entrada incluyendo los tramos anidados.
   *
   * @param crearRutaDTO Datos validados de la ruta a crear con sus tramos
   * @return RutaDTO creada con HTTP 201
   */
  @PostMapping
  @Operation(summary = "Crear nueva ruta", description = "Crea una nueva ruta con sus tramos para una solicitud específica")
  @ApiResponse(responseCode = "201", description = "Ruta creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RutaDTO.class)))
  @ApiResponse(responseCode = "404", description = "Solicitud no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  public ResponseEntity<RutaDTO> crearRuta(@Valid @RequestBody CrearRutaDTO crearRutaDTO) {
    // Convertir DTOs de tramo a entidades Tramo
    List<Tramo> tramos = new ArrayList<>();
    for (CrearTramoDTO tramoDTO : crearRutaDTO.tramos()) {
      Tramo tramo = Tramo.builder()
          .origen(tramoDTO.origen())
          .destino(tramoDTO.destino())
          .tipo(tramoDTO.tipo())
          .costoAproximado(tramoDTO.costoAproximado())
          .distanciaKm(tramoDTO.distanciaKm())
          .tiempoEstimadoHoras(tramoDTO.tiempoEstimadoHoras())
          .build();
      tramos.add(tramo);
    }

    var ruta = rutaService.crearRuta(crearRutaDTO.solicitudId(), tramos);
    return ResponseEntity.status(HttpStatus.CREATED).body(mapper.rutaToDto(ruta));
  }
}
