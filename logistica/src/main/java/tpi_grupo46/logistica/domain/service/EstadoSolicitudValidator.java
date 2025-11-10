package tpi_grupo46.logistica.domain.service;

import java.util.Map;
import java.util.Set;

/**
 * Servicio de dominio responsable de validar las transiciones
 * de estado permitidas entre instancias de Solicitud.
 *
 * Forma parte de la capa de dominio y define las reglas del negocio
 * asociadas al ciclo de vida de una solicitud.
 * 
 * Encapsula la lógica de negocio que define qué transiciones entre estados
 * son permitidas en el ciclo de vida de una solicitud de transporte.
 * 
 * Estados válidos y transiciones permitidas (códigos de la tabla ESTADO):
 * - BORRADOR → PROGRAMADA (asignación de costos/tiempos estimados)
 * - PROGRAMADA → EN_TRANSITO (inicio de transporte)
 * - EN_TRANSITO → ENTREGADA (finalización de entrega)
 */
public class EstadoSolicitudValidator {

  private static final Map<String, Set<String>> TRANSICIONES_VALIDAS = Map.of(
      "BORRADOR", Set.of("PROGRAMADA"),
      "PROGRAMADA", Set.of("EN_TRANSITO"),
      "EN_TRANSITO", Set.of("ENTREGADA"));

  private EstadoSolicitudValidator() {
    // Clase utilitaria: no debe ser instanciada
  }

  /**
   * Valida si una transición de estado es permitida.
   *
   * @param origen  Código del estado actual de la solicitud
   * @param destino Código del estado hacia el cual se intenta transicionar
   * @return true si la transición es válida, false en caso contrario
   */
  public static boolean esTransicionValida(String origen, String destino) {
    return TRANSICIONES_VALIDAS
        .getOrDefault(origen, Set.of())
        .contains(destino);
  }

  /**
   * Obtiene el conjunto de estados válidos hacia los que se puede transicionar
   * desde un estado dado.
   *
   * @param estado Código del estado actual
   * @return Conjunto de códigos de estados permitidos (nunca nulo, puede ser vacío)
   */
  public static Set<String> obtenerTransicionesValidas(String estado) {
    return TRANSICIONES_VALIDAS.getOrDefault(estado, Set.of());
  }
}
