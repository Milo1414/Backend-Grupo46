package tpi_grupo46.recursos.api;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tpi_grupo46.recursos.application.ContenedorService;
import tpi_grupo46.recursos.dto.ContenedorDTO;

import java.util.List;

@RestController
@RequestMapping("/api/contenedores")
@AllArgsConstructor
public class ContenedorController {

    private final ContenedorService contenedorService;

    @GetMapping
    public ResponseEntity<List<ContenedorDTO>> obtenerTodos() {
        List<ContenedorDTO> contenedores = contenedorService.obtenerTodos();
        return ResponseEntity.ok(contenedores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContenedorDTO> obtenerPorId(@PathVariable Integer id) {
        ContenedorDTO contenedor = contenedorService.obtenerPorId(id);
        return ResponseEntity.ok(contenedor);
    }

    @PostMapping
    public ResponseEntity<ContenedorDTO> crear(@RequestBody ContenedorDTO contenedorDTO) {
        ContenedorDTO contenedorCreado = contenedorService.crear(contenedorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(contenedorCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContenedorDTO> actualizar(@PathVariable Integer id, @RequestBody ContenedorDTO contenedorDTO) {
        ContenedorDTO contenedorActualizado = contenedorService.actualizar(id, contenedorDTO);
        return ResponseEntity.ok(contenedorActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        contenedorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
