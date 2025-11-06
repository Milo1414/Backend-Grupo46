package tpi_grupo46.recursos.api;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tpi_grupo46.recursos.application.CamionService;
import tpi_grupo46.recursos.dto.CamionDTO;

import java.util.List;

@RestController
@RequestMapping("/api/camiones")
@AllArgsConstructor
public class CamionController {

    private final CamionService camionService;

    @GetMapping
    public ResponseEntity<List<CamionDTO>> obtenerTodos() {
        List<CamionDTO> camiones = camionService.obtenerTodos();
        return ResponseEntity.ok(camiones);
    }

    @GetMapping("/{dominio}")
    public ResponseEntity<CamionDTO> obtenerPorDominio(@PathVariable String dominio) {
        CamionDTO camion = camionService.obtenerPorId(dominio);
        return ResponseEntity.ok(camion);
    }

    @PostMapping
    public ResponseEntity<CamionDTO> crear(@RequestBody CamionDTO camionDTO) {
        CamionDTO camionCreado = camionService.crear(camionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(camionCreado);
    }

    @PutMapping("/{dominio}")
    public ResponseEntity<CamionDTO> actualizar(@PathVariable String dominio, @RequestBody CamionDTO camionDTO) {
        CamionDTO camionActualizado = camionService.actualizar(dominio, camionDTO);
        return ResponseEntity.ok(camionActualizado);
    }

    @DeleteMapping("/{dominio}")
    public ResponseEntity<Void> eliminar(@PathVariable String dominio) {
        camionService.eliminar(dominio);
        return ResponseEntity.noContent().build();
    }

}
