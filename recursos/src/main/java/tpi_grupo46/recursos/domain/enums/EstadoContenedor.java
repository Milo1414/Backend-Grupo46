package tpi_grupo46.recursos.domain.enums;

/**
 * Estados aplicables a un Contenedor
 * Mapeados a la tabla ESTADO en la base de datos
 */
public enum EstadoContenedor {
    OCUPADO(1, "Ocupado"),
    VACIO(2, "Vacío"),
    EN_MANTENIMIENTO(3, "En Mantenimiento"),
    EN_TRANSITO(4, "En Tránsito");

    private final Integer id;
    private final String descripcion;

    EstadoContenedor(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Convierte un Integer a EstadoContenedor
     * @param value el ID del estado
     * @return EstadoContenedor correspondiente
     * @throws IllegalArgumentException si el valor no es válido
     */
    public static EstadoContenedor fromInteger(Integer value) {
        if (value == null) {
            return null;
        }
        switch (value) {
            case 1: return OCUPADO;
            case 2: return VACIO;
            case 3: return EN_MANTENIMIENTO;
            case 4: return EN_TRANSITO;
            default: throw new IllegalArgumentException("ID de estado inválido: " + value);
        }
    }

    /**
     * Convierte un String (código) a EstadoContenedor
     * @param codigo el código del estado
     * @return EstadoContenedor correspondiente
     */
    public static EstadoContenedor fromCodigo(String codigo) {
        if (codigo == null) {
            return null;
        }
        switch (codigo.toUpperCase()) {
            case "OCUPADO": return OCUPADO;
            case "VACIO": return VACIO;
            case "EN_MANTENIMIENTO": return EN_MANTENIMIENTO;
            case "EN_TRANSITO": return EN_TRANSITO;
            default: throw new IllegalArgumentException("Código de estado inválido: " + codigo);
        }
    }
}
