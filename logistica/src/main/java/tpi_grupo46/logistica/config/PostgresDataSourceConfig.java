package tpi_grupo46.logistica.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class PostgresDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public HikariConfig hikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/logistica_db");
        config.setUsername("postgres");
        config.setPassword("1234");
        config.setDriverClassName("org.postgresql.Driver");
        
        // Configurar propiedades del DataSource para PostgreSQL
        Properties props = new Properties();
        props.setProperty("assumeMinServerVersion", "9.0");
        // Deshabilitar explícitamente el envío de timezone
        props.setProperty("options", "-c TimeZone=UTC");
        config.setDataSourceProperties(props);
        
        return config;
    }

    @Bean
    public DataSource dataSource() {
        return new HikariDataSource(hikariConfig());
    }
}
