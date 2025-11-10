package tpi_grupo46.logistica.domain.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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

  private static final Map<String, Set<String>> TRANSICIONES_VALIDAS;
  
  static {
    Map<String, Set<String>> map = new HashMap<>();
    map.put("BORRADOR", new HashSet<>(Collections.singletonList("PROGRAMADA")));
    map.put("PROGRAMADA", new HashSet<>(Collections.singletonList("EN_TRANSITO")));
    map.put("EN_TRANSITO", new HashSet<>(Collections.singletonList("ENTREGADA")));
    TRANSICIONES_VALIDAS = Collections.unmodifiableMap(map);
  }

  private EstadoSolicitudValidator() {
    // Clase utilitaria: no debe ser instanciada
  }

  public static boolean esTransicionValida(String origen, String destino) {
    return TRANSICIONES_VALIDAS
        .getOrDefault(origen, Collections.emptySet())
        .contains(destino);
  }

  public static Set<String> obtenerTransicionesValidas(String estado) {
    return TRANSICIONES_VALIDAS.getOrDefault(estado, Collections.emptySet());
  }
}
