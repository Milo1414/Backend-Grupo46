package tpi_grupo46.logistica.domain.util;

import tpi_grupo46.logistica.domain.enums.EstadoSolicitud;

import java.util.Map;
import java.util.Set;

/**
 * Validador de transiciones de estado para Solicitudes.
 * 
 * Encapsula la lógica de negocio que define qué transiciones entre estados
 * son permitidas en el ciclo de vida de una solicitud de transporte.
 * 
 * Estados válidos y transiciones permitidas:
 * - BORRADOR → PROGRAMADA (asignación de costos/tiempos estimados)
 * - PROGRAMADA → EN_TRANSITO (inicio de transporte)
 * - EN_TRANSITO → ENTREGADA (finalización de entrega)
 */
public class EstadoSolicitudValidator {

  private static final Map<EstadoSolicitud, Set<EstadoSolicitud>> TRANSICIONES_VALIDAS = Map.of(
      EstadoSolicitud.BORRADOR, Set.of(EstadoSolicitud.PROGRAMADA),
      EstadoSolicitud.PROGRAMADA, Set.of(EstadoSolicitud.EN_TRANSITO),
      EstadoSolicitud.EN_TRANSITO, Set.of(EstadoSolicitud.ENTREGADA));

  private EstadoSolicitudValidator() {
    // Clase utilitaria: no debe ser instanciada
  }

  /**
   * Valida si una transición de estado es permitida.
   *
   * @param origen  Estado actual de la solicitud
   * @param destino Estado hacia el cual se intenta transicionar
   * @return true si la transición es válida, false en caso contrario
   */
  public static boolean esTransicionValida(EstadoSolicitud origen, EstadoSolicitud destino) {
    return TRANSICIONES_VALIDAS
        .getOrDefault(origen, Set.of())
        .contains(destino);
  }

  /**
   * Obtiene el conjunto de estados válidos hacia los que se puede transicionar
   * desde un estado dado.
   *
   * @param estado Estado actual
   * @return Conjunto de estados permitidos (nunca nulo, puede ser vacío)
   */
  public static Set<EstadoSolicitud> obtenerTransicionesValidas(EstadoSolicitud estado) {
    return TRANSICIONES_VALIDAS.getOrDefault(estado, Set.of());
  }
}
