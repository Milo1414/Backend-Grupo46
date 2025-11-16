package tpi_grupo46.logistica.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tpi_grupo46.logistica.domain.model.Tramo;
import tpi_grupo46.logistica.domain.model.Estado;
import tpi_grupo46.logistica.domain.model.CambioEstado;
import tpi_grupo46.logistica.infrastructure.repository.TramoRepository;
import tpi_grupo46.logistica.infrastructure.repository.EstadoRepository;
import tpi_grupo46.logistica.infrastructure.repository.CambioEstadoRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de aplicación para gestionar tramos de rutas.
 * 
 * Encapsulación de la lógica de negocio relacionada con tramos,
 * incluyendo asignación de camiones, inicio y finalización de tramos.
 * Forma parte de la capa de aplicación (Application Layer) y coordina
 * operaciones entre la capa de dominio y la de infraestructura.
 */
@Service
@Transactional
public class TramoService {

  private final TramoRepository tramoRepository;
  private final EstadoRepository estadoRepository;
  private final CambioEstadoRepository cambioEstadoRepository;

  public TramoService(TramoRepository tramoRepository,
                     EstadoRepository estadoRepository,
                     CambioEstadoRepository cambioEstadoRepository) {
    this.tramoRepository = tramoRepository;
    this.estadoRepository = estadoRepository;
    this.cambioEstadoRepository = cambioEstadoRepository;
  }

  /**
   * Asigna un camión a un tramo y cambia su estado a ASIGNADO.
   * 
   * @param tramoId  ID del tramo
   * @param camionId ID del camión a asignar (dominio/patente)
   * @return Tramo actualizado
   * @throws IllegalArgumentException Si el tramo no existe
   */
  public Tramo asignarCamion(Long tramoId, String camionId) {
    Tramo tramo = tramoRepository.findById(tramoId)
        .orElseThrow(() -> new IllegalArgumentException("Tramo no encontrado: " + tramoId));

    tramo.setCamionId(camionId);
    tramo = tramoRepository.save(tramo);

    // Cambiar estado a ASIGNADO
    cambiarEstadoTramo(tramo, "ASIGNADO");

    return tramo;
  }

  /**
   * Inicia el recorrido de un tramo registrando la fecha y hora real de inicio
   * y cambiando el estado a INICIADO.
   *
   * @param tramoId         ID del tramo
   * @param fechaHoraInicio Fecha y hora de inicio del tramo
   * @return Tramo actualizado
   * @throws IllegalArgumentException Si el tramo no existe
   */
  public Tramo iniciarTramo(Long tramoId, LocalDateTime fechaHoraInicio) {
    Tramo tramo = tramoRepository.findById(tramoId)
        .orElseThrow(() -> new IllegalArgumentException("Tramo no encontrado: " + tramoId));

    tramo.setFechaHoraInicioReal(fechaHoraInicio);
    tramo = tramoRepository.save(tramo);

    // Cambiar estado a INICIADO
    cambiarEstadoTramo(tramo, "INICIADO");

    return tramo;
  }

  /**
   * Finaliza el recorrido de un tramo registrando fecha/hora final, costo real
   * y cambiando el estado a FINALIZADO.
   *
   * @param tramoId      ID del tramo
   * @param fechaHoraFin Fecha y hora final del tramo
   * @param costoReal    Costo real del tramo
   * @return Tramo actualizado
   * @throws IllegalArgumentException Si el tramo no existe
   */
  public Tramo finalizarTramo(Long tramoId, LocalDateTime fechaHoraFin, BigDecimal costoReal) {
    Tramo tramo = tramoRepository.findById(tramoId)
        .orElseThrow(() -> new IllegalArgumentException("Tramo no encontrado: " + tramoId));

    tramo.setFechaHoraFinReal(fechaHoraFin);
    tramo.setCostoReal(costoReal);
    tramo = tramoRepository.save(tramo);

    // Cambiar estado a FINALIZADO
    cambiarEstadoTramo(tramo, "FINALIZADO");

    return tramo;
  }

  /**
   * Obtiene todos los tramos asignados a un camión específico.
   *
   * @param camionId ID del camión (dominio/patente)
   * @return Lista de tramos asignados al camión
   */
  public List<Tramo> obtenerTramosPorCamion(String camionId) {
    return tramoRepository.findByCamionId(camionId);
  }

  /**
   * Obtiene un tramo por su ID.
   *
   * @param tramoId ID del tramo
   * @return Tramo encontrado
   * @throws IllegalArgumentException Si el tramo no existe
   */
  public Tramo obtenerTramo(Long tramoId) {
    return tramoRepository.findById(tramoId)
        .orElseThrow(() -> new IllegalArgumentException("Tramo no encontrado: " + tramoId));
  }

  /**
   * Cambia el estado de un tramo y registra el cambio en CambioEstado.
   * Cierra el estado anterior (fechaFin) y abre el nuevo (fechaFin = null).
   * 
   * @param tramo        Tramo a actualizar
   * @param codigoEstado Código del nuevo estado (ESTIMADO, ASIGNADO, INICIADO, FINALIZADO)
   */
  private void cambiarEstadoTramo(Tramo tramo, String codigoEstado) {
    // Obtener el estado por código y tipo
    Estado nuevoEstado = estadoRepository.findByCodigoAndEntidadTipo(codigoEstado, "TRAMO")
        .orElseThrow(() -> new IllegalArgumentException("Estado " + codigoEstado + " no encontrado para TRAMO"));

    // Cerrar el estado anterior (si existe) estableciendo fechaFin
    cambioEstadoRepository.findByTramoIdOrderByFechaInicioAsc(tramo.getId())
        .stream()
        .filter(cambio -> cambio.getFechaFin() == null) // Estado actual (activo)
        .forEach(cambioActivo -> {
          cambioActivo.setFechaFin(LocalDateTime.now());
          cambioEstadoRepository.save(cambioActivo);
        });

    // Crear el nuevo estado (fechaFin = null indica que es el estado actual)
    CambioEstado cambio = CambioEstado.builder()
        .tramo(tramo)
        .estado(nuevoEstado)
        .fechaInicio(LocalDateTime.now())
        .fechaFin(null) // null = estado actual
        .build();

    cambioEstadoRepository.save(cambio);
  }
}
