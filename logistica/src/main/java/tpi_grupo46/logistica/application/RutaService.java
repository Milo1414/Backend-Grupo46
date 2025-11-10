package tpi_grupo46.logistica.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tpi_grupo46.logistica.domain.model.Ruta;
import tpi_grupo46.logistica.domain.model.Solicitud;
import tpi_grupo46.logistica.domain.model.Tramo;
import tpi_grupo46.logistica.infrastructure.repository.RutaRepository;
import tpi_grupo46.logistica.infrastructure.repository.SolicitudRepository;
import tpi_grupo46.logistica.infrastructure.repository.TramoRepository;

import java.util.List;

/**
 * Servicio de aplicación para gestionar rutas de transporte.
 * 
 * Encapsulación de la lógica de negocio relacionada con rutas y tramos,
 * incluyendo creación y consulta de rutas así como la gestión de tramos
 * asignados a ellas. Forma parte de la capa de aplicación (Application Layer)
 * y coordina operaciones entre la capa de dominio y la de infraestructura.
 */
@Service
@Transactional
public class RutaService {

  private final RutaRepository rutaRepository;
  private final SolicitudRepository solicitudRepository;
  private final TramoRepository tramoRepository;

  public RutaService(RutaRepository rutaRepository,
      SolicitudRepository solicitudRepository,
      TramoRepository tramoRepository) {
    this.rutaRepository = rutaRepository;
    this.solicitudRepository = solicitudRepository;
    this.tramoRepository = tramoRepository;
  }

  /**
   * Crea una ruta para una solicitud con los tramos necesarios.
   * Asocia todos los tramos a la ruta.
   *
   * @param solicitudId ID de la solicitud asociada
   * @param tramos      Lista de tramos que componen la ruta
   * @return Ruta creada con todos sus tramos asociados
   * @throws IllegalArgumentException Si la solicitud no existe
   */
  public Ruta crearRuta(Long solicitudId, List<Tramo> tramos) {
    Solicitud solicitud = solicitudRepository.findById(solicitudId)
        .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada: " + solicitudId));

    Ruta ruta = Ruta.builder()
        .solicitud(solicitud)
        .cantidadTramos(tramos.size())
        .cantidadDepositos(0) // Se puede calcular según los tramos
        .build();

    ruta = rutaRepository.save(ruta);

    // Asociar tramos a la ruta
    for (Tramo tramo : tramos) {
      tramo.setRuta(ruta);
      tramoRepository.save(tramo);
    }

    return ruta;
  }

  /**
   * Obtiene todos los tramos de una ruta específica.
   *
   * @param rutaId ID de la ruta
   * @return Lista de tramos ordenados
   */
  public List<Tramo> obtenerTramosPorRuta(Long rutaId) {
    return tramoRepository.findByRutaId(rutaId);
  }

  /**
   * Obtiene una ruta por su ID.
   *
   * @param rutaId ID de la ruta
   * @return Ruta encontrada
   * @throws IllegalArgumentException Si no existe ruta con ese ID
   */
  public Ruta obtenerRuta(Long rutaId) {
    return rutaRepository.findById(rutaId)
        .orElseThrow(() -> new IllegalArgumentException("Ruta no encontrada: " + rutaId));
  }

  /**
   * Obtiene todas las rutas asociadas a una solicitud.
   *
   * @param solicitudId ID de la solicitud
   * @return Ruta asociada (típicamente una por solicitud)
   * @throws IllegalArgumentException Si no existe ruta para esa solicitud
   */
  public Ruta obtenerRutaPorSolicitud(Long solicitudId) {
    return rutaRepository.findBySolicitudId(solicitudId)
        .orElseThrow(() -> new IllegalArgumentException("Ruta no encontrada para solicitud: " + solicitudId));
  }
}
