package tpi_grupo46.logistica.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tpi_grupo46.logistica.domain.enums.EstadoSolicitud;
import tpi_grupo46.logistica.domain.model.CambioEstado;
import tpi_grupo46.logistica.domain.model.Ruta;
import tpi_grupo46.logistica.domain.model.Solicitud;
import tpi_grupo46.logistica.domain.model.Tramo;
import tpi_grupo46.logistica.infrastructure.repository.CambioEstadoRepository;
import tpi_grupo46.logistica.infrastructure.repository.RutaRepository;
import tpi_grupo46.logistica.infrastructure.repository.SolicitudRepository;
import tpi_grupo46.logistica.infrastructure.repository.TramoRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de aplicación para gestionar solicitudes de transporte.
 * 
 * Proporciona operaciones de negocio de alto nivel para crear, actualizar
 * y consultar solicitudes de transporte.
 */
@Service
@Transactional
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final RutaRepository rutaRepository;
    private final TramoRepository tramoRepository;
    private final CambioEstadoRepository cambioEstadoRepository;

    public SolicitudService(SolicitudRepository solicitudRepository,
                           RutaRepository rutaRepository,
                           TramoRepository tramoRepository,
                           CambioEstadoRepository cambioEstadoRepository) {
        this.solicitudRepository = solicitudRepository;
        this.rutaRepository = rutaRepository;
        this.tramoRepository = tramoRepository;
        this.cambioEstadoRepository = cambioEstadoRepository;
    }

    /**
     * Crea una nueva solicitud de transporte en estado BORRADOR.
     * Automáticamente registra un CambioEstado inicial.
     * 
     * @param clienteId ID del cliente
     * @param contenedorId ID del contenedor
     * @return Solicitud creada
     */
    public Solicitud crearSolicitud(Long clienteId, Long contenedorId) {
        Solicitud solicitud = Solicitud.builder()
                .clienteId(clienteId)
                .contenedorId(contenedorId)
                .estado(EstadoSolicitud.BORRADOR)
                .build();
        
        return solicitudRepository.save(solicitud);
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
     * 
     * @param estado Estado a filtrar
     * @return Lista de solicitudes en ese estado
     */
    public List<Solicitud> obtenerSolicitudesPorEstado(EstadoSolicitud estado) {
        return solicitudRepository.findByEstado(estado);
    }

    /**
     * Programa una solicitud asignándole costos y tiempos estimados.
     * Cambia el estado de BORRADOR a PROGRAMADA.
     * 
     * @param solicitudId ID de la solicitud
     * @param costoEstimado Costo estimado
     * @param tiempoEstimadoHoras Tiempo estimado en horas
     * @return Solicitud actualizada
     */
    public Solicitud programarSolicitud(Long solicitudId, BigDecimal costoEstimado, Double tiempoEstimadoHoras) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada: " + solicitudId));

        solicitud.setCostoEstimado(costoEstimado);
        solicitud.setTiempoEstimadoHoras(tiempoEstimadoHoras);
        
        cambiarEstadoSolicitud(solicitud, EstadoSolicitud.PROGRAMADA);
        
        return solicitudRepository.save(solicitud);
    }

    /**
     * Crea una ruta para una solicitud con los tramos necesarios.
     * 
     * @param solicitudId ID de la solicitud
     * @param tramos Lista de tramos de la ruta
     * @return Ruta creada
     */
    public Ruta crearRuta(Long solicitudId, List<Tramo> tramos) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada: " + solicitudId));

        Ruta ruta = Ruta.builder()
                .solicitud(solicitud)
                .cantidadTramos(tramos.size())
                .cantidadDepositos(0) // Se puede calcular según los tramos
                .tramos(new ArrayList<>())
                .build();

        ruta = rutaRepository.save(ruta);

        // Asociar tramos a la ruta
        for (Tramo tramo : tramos) {
            tramo.setRuta(ruta);
            tramo.setSolicitud(solicitud);
            tramoRepository.save(tramo);
        }

        solicitud.setRuta(ruta);
        solicitudRepository.save(solicitud);

        return ruta;
    }

    /**
     * Obtiene los tramos de una ruta.
     * 
     * @param rutaId ID de la ruta
     * @return Lista de tramos
     */
    public List<Tramo> obtenerTramosPorRuta(Long rutaId) {
        return tramoRepository.findByRutaId(rutaId);
    }

    /**
     * Obtiene el historial de cambios de estado de una solicitud.
     * 
     * @param solicitudId ID de la solicitud
     * @return Lista de cambios de estado ordenados por fecha
     */
    public List<CambioEstado> obtenerHistorialCambios(Long solicitudId) {
        return cambioEstadoRepository.findBySolicitudIdOrderByFechaCambioAsc(solicitudId);
    }

    /**
     * Cambia el estado de una solicitud y registra el cambio.
     * 
     * @param solicitud Solicitud a actualizar
     * @param nuevoEstado Nuevo estado
     */
    private void cambiarEstadoSolicitud(Solicitud solicitud, EstadoSolicitud nuevoEstado) {
        solicitud.setEstado(nuevoEstado);

        CambioEstado cambio = CambioEstado.builder()
                .solicitud(solicitud)
                .estado(nuevoEstado)
                .fechaCambio(LocalDateTime.now())
                .build();

        cambioEstadoRepository.save(cambio);
    }

    /**
     * Marca una solicitud como entregada.
     * 
     * @param solicitudId ID de la solicitud
     * @param costoFinal Costo final de la entrega
     * @param tiempoRealHoras Tiempo real de entrega
     * @return Solicitud actualizada
     */
    public Solicitud completarEntrega(Long solicitudId, BigDecimal costoFinal, Double tiempoRealHoras) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada: " + solicitudId));

        solicitud.setCostoFinal(costoFinal);
        solicitud.setTiempoRealHoras(tiempoRealHoras);

        cambiarEstadoSolicitud(solicitud, EstadoSolicitud.ENTREGADA);

        return solicitudRepository.save(solicitud);
    }
}
