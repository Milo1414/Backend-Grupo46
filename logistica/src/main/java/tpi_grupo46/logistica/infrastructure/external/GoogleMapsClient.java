package tpi_grupo46.logistica.infrastructure.external;

import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cliente de infraestructura encargado de la comunicación
 * con el servicio externo Google Maps Directions API.
 * 
 * Encapsula las llamadas HTTP y abstrae los detalles de conexión,
 * manteniendo bajo acoplamiento con la capa de aplicación.
 * 
 * Esta clase es responsable de:
 * - Construir las solicitudes HTTP hacia Google Maps Directions API
 * - Procesar y parsear las respuestas
 * - Manejar errores de conexión y timeouts
 * - Convertir los datos en formato utilizable por la capa de aplicación
 * 
 * @since 2.0
 */
@Component
public class GoogleMapsClient {

  private static final Logger logger = LoggerFactory.getLogger(GoogleMapsClient.class);

  // TODO: Inyectar RestTemplate o WebClient para realizar llamadas HTTP
  // TODO: Obtener API key desde configuración (application.yml)
  // TODO: Implementar métodos para consumir Directions API

  /**
   * Obtiene información de ruta entre dos puntos geograficos.
   * 
   * @param origen  Coordenadas de origen (latitud,longitud)
   * @param destino Coordenadas de destino (latitud,longitud)
   * @return Información de la ruta incluida distancia y duración estimada
   * @throws RuntimeException si la llamada a Google Maps falla
   */
  public String obtenerInformacionRuta(String origen, String destino) {
    logger.info("Consultando Google Maps: origen={}, destino={}", origen, destino);

    // TODO: Implementar lógica de consumo de API
    throw new UnsupportedOperationException("Método no implementado en esta versión");
  }

  /**
   * Calcula la distancia en metros entre dos puntos.
   * 
   * @param origen  Coordenadas de origen
   * @param destino Coordenadas de destino
   * @return Distancia en metros
   */
  public long calcularDistancia(String origen, String destino) {
    logger.debug("Calculando distancia: {} -> {}", origen, destino);

    // TODO: Implementar lógica de cálculo
    throw new UnsupportedOperationException("Método no implementado en esta versión");
  }

  /**
   * Calcula el tiempo estimado de viaje entre dos puntos.
   * 
   * @param origen  Coordenadas de origen
   * @param destino Coordenadas de destino
   * @return Tiempo en segundos
   */
  public long calcularTiempoEstimado(String origen, String destino) {
    logger.debug("Calculando tiempo estimado: {} -> {}", origen, destino);

    // TODO: Implementar lógica de cálculo
    throw new UnsupportedOperationException("Método no implementado en esta versión");
  }
}
