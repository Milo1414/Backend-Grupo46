package tpi_grupo46.logistica.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tpi_grupo46.logistica.domain.model.Tramo;
import tpi_grupo46.logistica.infrastructure.repository.TramoRepository;

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

  public TramoService(TramoRepository tramoRepository) {
    this.tramoRepository = tramoRepository;
  }

  /**
   * Asigna un camión a un tramo.
   * 
   * @param tramoId  ID del tramo
   * @param camionId ID del camión a asignar
   * @return Tramo actualizado
   * @throws IllegalArgumentException Si el tramo no existe
   */
  public Tramo asignarCamion(Long tramoId, Long camionId) {
    Tramo tramo = tramoRepository.findById(tramoId)
        .orElseThrow(() -> new IllegalArgumentException("Tramo no encontrado: " + tramoId));

    tramo.setCamionId(camionId);
    return tramoRepository.save(tramo);
  }

  /**
   * Inicia el recorrido de un tramo registrando la fecha y hora real de inicio.
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
    return tramoRepository.save(tramo);
  }

  /**
   * Finaliza el recorrido de un tramo registrando fecha/hora final y costo real.
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
    return tramoRepository.save(tramo);
  }

  /**
   * Obtiene todos los tramos asignados a un camión específico.
   *
   * @param camionId ID del camión
   * @return Lista de tramos asignados al camión
   */
  public List<Tramo> obtenerTramosPorCamion(Long camionId) {
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
}
