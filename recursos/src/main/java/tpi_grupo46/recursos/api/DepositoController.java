package tpi_grupo46.recursos.api;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tpi_grupo46.recursos.application.DepositoService;
import tpi_grupo46.recursos.dto.DepositoDTO;

import java.util.List;

@RestController
@RequestMapping("/api/depositos")
@AllArgsConstructor
public class DepositoController {

    private final DepositoService depositoService;

    @GetMapping
    public ResponseEntity<List<DepositoDTO>> obtenerTodos() {
        List<DepositoDTO> depositos = depositoService.obtenerTodos();
        return ResponseEntity.ok(depositos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepositoDTO> obtenerPorId(@PathVariable Integer id) {
        DepositoDTO deposito = depositoService.obtenerPorId(id);
        return ResponseEntity.ok(deposito);
    }

    @PostMapping
    public ResponseEntity<DepositoDTO> crear(@RequestBody DepositoDTO depositoDTO) {
        DepositoDTO depositoCreado = depositoService.crear(depositoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(depositoCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepositoDTO> actualizar(@PathVariable Integer id, @RequestBody DepositoDTO depositoDTO) {
        DepositoDTO depositoActualizado = depositoService.actualizar(id, depositoDTO);
        return ResponseEntity.ok(depositoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        depositoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
