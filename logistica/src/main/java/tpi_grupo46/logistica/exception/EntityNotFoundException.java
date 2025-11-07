package tpi_grupo46.logistica.exception;

/**
 * Excepción lanzada cuando no se encuentra una entidad en la base de datos
 */
public class EntityNotFoundException extends RuntimeException {

  public EntityNotFoundException(String message) {
    super(message);
  }

  public EntityNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public static EntityNotFoundException solicitudNotFound(Long id) {
    return new EntityNotFoundException("Solicitud no encontrada con ID: " + id);
  }

  public static EntityNotFoundException rutaNotFound(Long id) {
    return new EntityNotFoundException("Ruta no encontrada con ID: " + id);
  }

  public static EntityNotFoundException tramoNotFound(Long id) {
    return new EntityNotFoundException("Tramo no encontrado con ID: " + id);
  }

  public static EntityNotFoundException cambioEstadoNotFound(Long id) {
    return new EntityNotFoundException("CambioEstado no encontrado con ID: " + id);
  }
}
