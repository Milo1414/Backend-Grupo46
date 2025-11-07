package tpi_grupo46.logistica.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI/Swagger para la documentación de la API
 */
@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .servers(List.of(
            new Server().url("http://localhost:8081").description("Servidor local"),
            new Server().url("http://localhost:8080").description("Servidor alternativo")))
        .info(new Info()
            .title("API de Logística - TPI Backend Grupo 46")
            .description(
                "Microservicio de Logística para gestión de solicitudes de transporte de contenedores. " +
                    "Proporciona endpoints para:\n" +
                    "- Crear y gestionar solicitudes de transporte\n" +
                    "- Asignar y gestionar rutas\n" +
                    "- Gestionar tramos y camiones\n" +
                    "- Consultar historiales de estado\n\n" +
                    "Parte del sistema de transporte de contenedores (TPI 2025)")
            .version("1.0.0")
            .contact(new Contact()
                .name("Grupo 46")
                .email("grupo46@utnfrc.edu.ar")
                .url("https://github.com/Milo1414/Backend-Grupo46"))
            .license(new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
  }
}
