package tpi_grupo46.recursos.api;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tpi_grupo46.recursos.application.TarifaRangoService;
import tpi_grupo46.recursos.dto.TarifaRangoDTO;

import java.util.List;

@RestController
@RequestMapping("/api/tarifa-rangos")
@AllArgsConstructor
public class TarifaRangoController {

    private final TarifaRangoService tarifaRangoService;

    @GetMapping
    public ResponseEntity<List<TarifaRangoDTO>> obtenerTodos() {
        List<TarifaRangoDTO> tarifaRangos = tarifaRangoService.obtenerTodos();
        return ResponseEntity.ok(tarifaRangos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarifaRangoDTO> obtenerPorId(@PathVariable Integer id) {
        TarifaRangoDTO tarifaRango = tarifaRangoService.obtenerPorId(id);
        return ResponseEntity.ok(tarifaRango);
    }

    @PostMapping
    public ResponseEntity<TarifaRangoDTO> crear(@RequestBody TarifaRangoDTO tarifaRangoDTO) {
        TarifaRangoDTO tarifaRangoCreado = tarifaRangoService.crear(tarifaRangoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(tarifaRangoCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarifaRangoDTO> actualizar(@PathVariable Integer id, @RequestBody TarifaRangoDTO tarifaRangoDTO) {
        TarifaRangoDTO tarifaRangoActualizado = tarifaRangoService.actualizar(id, tarifaRangoDTO);
        return ResponseEntity.ok(tarifaRangoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        tarifaRangoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
