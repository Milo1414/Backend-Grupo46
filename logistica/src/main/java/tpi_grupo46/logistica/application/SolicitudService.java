package tpi_grupo46.logistica.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tpi_grupo46.logistica.domain.model.CambioEstado;
import tpi_grupo46.logistica.domain.model.Estado;
import tpi_grupo46.logistica.domain.model.Solicitud;
import tpi_grupo46.logistica.infrastructure.repository.CambioEstadoRepository;
import tpi_grupo46.logistica.infrastructure.repository.EstadoRepository;
import tpi_grupo46.logistica.infrastructure.repository.SolicitudRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de aplicación para gestionar solicitudes de transporte.
 * 
 * Encapsulación de la lógica de negocio relacionada exclusivamente con
 * solicitudes:
 * creación, programación, consulta por cliente/estado e historial de cambios.
 * Forma parte de la capa de aplicación (Application Layer) y coordina
 * operaciones
 * entre la capa de dominio y la de infraestructura. La lógica de rutas y tramos
 * ha sido delegada a RutaService y TramoService respectivamente.
 */
@Service
@Transactional
public class SolicitudService {

  private final SolicitudRepository solicitudRepository;
  private final CambioEstadoRepository cambioEstadoRepository;
  private final EstadoRepository estadoRepository;

  public SolicitudService(SolicitudRepository solicitudRepository,
      CambioEstadoRepository cambioEstadoRepository,
      EstadoRepository estadoRepository) {
    this.solicitudRepository = solicitudRepository;
    this.cambioEstadoRepository = cambioEstadoRepository;
    this.estadoRepository = estadoRepository;
  }

  /**
   * Crea una nueva solicitud de transporte en estado BORRADOR.
   * Automáticamente registra un CambioEstado inicial para auditoría.
   * 
   * @param clienteId    ID del cliente que hace la solicitud
   * @param contenedorId ID del contenedor a transportar
   * @return Solicitud creada en estado BORRADOR
   */
  public Solicitud crearSolicitud(Long clienteId, Long contenedorId) {
    // Obtener el estado BORRADOR para SOLICITUD
    Estado estadoBorrador = estadoRepository.findByCodigoAndEntidadTipo("BORRADOR", "SOLICITUD")
        .orElseThrow(() -> new IllegalArgumentException("Estado BORRADOR no encontrado para SOLICITUD"));

    Solicitud solicitud = Solicitud.builder()
        .clienteId(clienteId)
        .contenedorId(contenedorId)
        .build();

    var solicitudGuardada = solicitudRepository.save(solicitud);
    guardarCambioEstado(solicitudGuardada, estadoBorrador);

    return solicitudGuardada;
  }

  /**
   * Obtiene todas las solicitudes de un cliente.
   * 
   * @param clienteId ID del cliente
   * @return Lista de solicitudes del cliente
   */
  public List<Solicitud> obtenerSolicitudesPorCliente(Long clienteId) {
    return solicitudRepository.findByClienteId(clienteId);
  }

  /**
   * Obtiene todas las solicitudes con un estado específico.
   * Nota: Ahora Solicitud NO tiene relación directa a Estado.
   * El estado se obtiene del último CambioEstado.
   * Este método es mantenido por compatibilidad pero consultará CambioEstado.
   * 
   * @param estado Estado a filtrar (entidad Estado)
   * @return Lista de solicitudes en ese estado
   */
  public List<Solicitud> obtenerSolicitudesPorEstado(Estado estado) {
    // Buscar todos los CambioEstado con este estado y obtener sus solicitudes
    var cambios = cambioEstadoRepository.findByEstado(estado);
    return cambios.stream()
        .map(CambioEstado::getSolicitud)
        .filter(java.util.Objects::nonNull)
        .distinct()
        .toList();
  }

  /**
   * Programa una solicitud asignándole costos y tiempos estimados.
   * Cambia el estado de BORRADOR a PROGRAMADA.
   * 
   * @param solicitudId         ID de la solicitud
   * @param costoEstimado       Costo estimado
   * @param tiempoEstimadoHoras Tiempo estimado en horas
   * @return Solicitud actualizada
   */
  public Solicitud programarSolicitud(Long solicitudId, BigDecimal costoEstimado, Double tiempoEstimadoHoras) {
    Solicitud solicitud = solicitudRepository.findById(solicitudId)
        .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada: " + solicitudId));

    solicitud.setCostoEstimado(costoEstimado);
    solicitud.setTiempoEstimadoHoras(tiempoEstimadoHoras);

    // Obtener el estado PROGRAMADA
    Estado estadoProgramada = estadoRepository.findByCodigoAndEntidadTipo("PROGRAMADA", "SOLICITUD")
        .orElseThrow(() -> new IllegalArgumentException("Estado PROGRAMADA no encontrado para SOLICITUD"));

    cambiarEstadoSolicitud(solicitud, estadoProgramada);

    return solicitudRepository.save(solicitud);
  }

  /**
   * Obtiene el historial de cambios de estado de una solicitud.
   * 
   * @param solicitudId ID de la solicitud
   * @return Lista de cambios de estado ordenados por fecha
   */
  public List<CambioEstado> obtenerHistorialCambios(Long solicitudId) {
    return cambioEstadoRepository.findBySolicitudIdOrderByFechaInicioAsc(solicitudId);
  }

  /**
   * Cambia el estado de una solicitud y registra el cambio.
   * 
   * @param solicitud   Solicitud a actualizar
   * @param nuevoEstado Nuevo estado
   */
  public void guardarCambioEstado(Solicitud solicitud, Estado nuevoEstado) {
    cambiarEstadoSolicitud(solicitud, nuevoEstado);
  }

  /**
   * Cambia el estado de una solicitud y registra el cambio.
   * Valida que el nuevo estado pertenezca a SOLICITUD.
   * Cierra el estado anterior (establece fechaFin) y abre el nuevo.
   * 
   * @param solicitud   Solicitud a actualizar
   * @param nuevoEstado Nuevo estado
   * @throws IllegalStateException Si el nuevo estado no es de tipo SOLICITUD
   */
  private void cambiarEstadoSolicitud(Solicitud solicitud, Estado nuevoEstado) {
    // Validar que el nuevo estado sea para SOLICITUD
    if (!nuevoEstado.getEntidadTipo().equals("SOLICITUD")) {
      throw new IllegalStateException(
          "El estado " + nuevoEstado.getCodigo() + " no es aplicable a SOLICITUD");
    }

    // Cerrar el estado anterior (si existe) estableciendo fechaFin
    cambioEstadoRepository.findBySolicitudIdOrderByFechaInicioAsc(solicitud.getId())
        .stream()
        .filter(cambio -> cambio.getFechaFin() == null) // Estado actual (activo)
        .forEach(cambioActivo -> {
          cambioActivo.setFechaFin(LocalDateTime.now());
          cambioEstadoRepository.save(cambioActivo);
        });

    // Crear el nuevo estado (fechaFin = null indica que es el estado actual)
    CambioEstado cambio = CambioEstado.builder()
        .solicitud(solicitud)
        .estado(nuevoEstado)
        .fechaInicio(LocalDateTime.now())
        .fechaFin(null) // null = estado actual
        .build();

    cambioEstadoRepository.save(cambio);
  }

  /**
   * Marca una solicitud como entregada.
   * 
   * @param solicitudId     ID de la solicitud
   * @param costoFinal      Costo final de la entrega
   * @param tiempoRealHoras Tiempo real de entrega
   * @return Solicitud actualizada
   */
  public Solicitud completarEntrega(Long solicitudId, BigDecimal costoFinal, Double tiempoRealHoras) {
    Solicitud solicitud = solicitudRepository.findById(solicitudId)
        .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada: " + solicitudId));

    solicitud.setCostoFinal(costoFinal);
    solicitud.setTiempoRealHoras(tiempoRealHoras);

    // Obtener el estado ENTREGADA
    Estado estadoEntregada = estadoRepository.findByCodigoAndEntidadTipo("ENTREGADA", "SOLICITUD")
        .orElseThrow(() -> new IllegalArgumentException("Estado ENTREGADA no encontrado para SOLICITUD"));

    cambiarEstadoSolicitud(solicitud, estadoEntregada);

    return solicitudRepository.save(solicitud);
  }
}
