package tpi_grupo46.recursos.api;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tpi_grupo46.recursos.application.TarifaService;
import tpi_grupo46.recursos.dto.TarifaDTO;

import java.util.List;

@RestController
@RequestMapping("/api/tarifas")
@AllArgsConstructor
public class TarifaController {

    private final TarifaService tarifaService;

    @GetMapping
    public ResponseEntity<List<TarifaDTO>> obtenerTodos() {
        List<TarifaDTO> tarifas = tarifaService.obtenerTodos();
        return ResponseEntity.ok(tarifas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarifaDTO> obtenerPorId(@PathVariable Integer id) {
        TarifaDTO tarifa = tarifaService.obtenerPorId(id);
        return ResponseEntity.ok(tarifa);
    }

    @PostMapping
    public ResponseEntity<TarifaDTO> crear(@RequestBody TarifaDTO tarifaDTO) {
        TarifaDTO tarifaCreada = tarifaService.crear(tarifaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(tarifaCreada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarifaDTO> actualizar(@PathVariable Integer id, @RequestBody TarifaDTO tarifaDTO) {
        TarifaDTO tarifaActualizada = tarifaService.actualizar(id, tarifaDTO);
        return ResponseEntity.ok(tarifaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        tarifaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
