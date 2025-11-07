package tpi_grupo46.logistica.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para la página de inicio
 * Redirige a la documentación Swagger UI
 */
@Controller
public class HomeController {

    /**
     * Endpoint raíz que redirige a Swagger UI
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/swagger-ui.html";
    }
}
