package tpi_grupo46.logistica.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de Spring Security para la aplicación de logística.
 * 
 * Forma parte de la capa de infraestructura (Infrastructure Layer) y
 * proporciona
 * la configuración básica de seguridad. Actualmente, permite todas las
 * solicitudes
 * sin autenticación (CSRF deshabilitado), siendo una base para futura
 * implementación
 * de autenticación y autorización.
 * 
 * NOTA IMPORTANTE: Esta configuración es únicamente para desarrollo y pruebas.
 * Para producción, debe implementarse autenticación con JWT, OAuth2 u otro
 * mecanismo
 * seguro, junto con validación de CSRF y autorización basada en roles.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  /**
   * Configura la cadena de filtros de seguridad HTTP.
   * 
   * Configuración actual:
   * - CSRF deshabilitado (considerado para APIs REST con token-based auth)
   * - Todas las solicitudes permitidas (authorizeHttpRequests().permitAll())
   * - Sin autenticación requerida (preparación para fase 2)
   * 
   * @param http Builder de configuración HTTP Security
   * @return SecurityFilterChain configurada
   * @throws Exception Si hay error en la configuración
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authz -> authz
            .anyRequest().permitAll());

    return http.build();
  }
}
