package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.EmpaqueLacteoRequest;
import com.yerman.produccion_api.application.dto.response.EmpaqueLacteoResponse;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.mapper.EmpaqueLacteoRestMapper;
import com.yerman.produccion_api.domain.model.EmpaqueLacteo;
import com.yerman.produccion_api.domain.port.in.GestionEmpaqueLacteoUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.yerman.produccion_api.application.mapper.EmpaqueLacteoRestMapper.toResponse;

@RestController
@RequestMapping("/empaques-lacteos")
public class EmpaqueLacteoController {

    private final GestionEmpaqueLacteoUseCase gestionEmpaqueLacteoUseCase;

    public EmpaqueLacteoController(GestionEmpaqueLacteoUseCase gestionEmpaqueLacteoUseCase) {
        this.gestionEmpaqueLacteoUseCase = gestionEmpaqueLacteoUseCase;
    }

    @PostMapping
    public ResponseEntity<EmpaqueLacteoResponse> crear(@Valid @RequestBody EmpaqueLacteoRequest request) {
        EmpaqueLacteo empaqueLacteo = EmpaqueLacteoRestMapper.toDomain(request);
        EmpaqueLacteo creado = gestionEmpaqueLacteoUseCase.crear(empaqueLacteo);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(creado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpaqueLacteoResponse> obtenerPorId(@PathVariable Long id) {
        EmpaqueLacteo empaqueLacteo = gestionEmpaqueLacteoUseCase.obtenerPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Empaque lácteo no encontrado"));

        return ResponseEntity.ok(toResponse(empaqueLacteo));
    }

    @GetMapping
    public ResponseEntity<List<EmpaqueLacteoResponse>> listarTodos() {
        List<EmpaqueLacteoResponse> response = gestionEmpaqueLacteoUseCase.listarTodos()
                .stream()
                .map(EmpaqueLacteoRestMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/producto-terminado/{productoTerminadoLacteoId}")
    public ResponseEntity<List<EmpaqueLacteoResponse>> listarPorProductoTerminadoLacteo(
            @PathVariable Long productoTerminadoLacteoId) {
        List<EmpaqueLacteoResponse> response = gestionEmpaqueLacteoUseCase
                .listarPorProductoTerminadoLacteo(productoTerminadoLacteoId)
                .stream()
                .map(EmpaqueLacteoRestMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/lote/{loteEmpaque}")
    public ResponseEntity<List<EmpaqueLacteoResponse>> listarPorLoteEmpaque(
            @PathVariable String loteEmpaque) {
        List<EmpaqueLacteoResponse> response = gestionEmpaqueLacteoUseCase
                .listarPorLoteEmpaque(loteEmpaque)
                .stream()
                .map(EmpaqueLacteoRestMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/anular")
    public ResponseEntity<EmpaqueLacteoResponse> anular(@PathVariable Long id) {
        EmpaqueLacteo anulado = gestionEmpaqueLacteoUseCase.anular(id);

        return ResponseEntity.ok(toResponse(anulado));
    }
}