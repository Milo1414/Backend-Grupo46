package tpi_grupo46.logistica.config;

/**
 * Configuración de DataSource para PostgreSQL/Supabase.
 * 
 * NOTA: Esta clase está aquí por compatibilidad, pero toda la configuración
 * real se encuentra en application.yml (spring.datasource.url, username, password).
 * 
 * Spring Boot 3.x maneja automáticamente la creación del DataSource y HikariCP
 * a partir de las propiedades en application.yml, por lo que no necesitamos
 * configuración manual aquí.
 * 
 * Para cambiar la conexión a la BD, modifica application.yml, no este archivo.
 */
public class PostgresDataSourceConfig {
    // La configuración se carga automáticamente desde application.yml
    // No es necesario crear beans adicionales
}
