package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.InsumoRequest;
import com.yerman.produccion_api.application.dto.response.InsumoResponse;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.mapper.InsumoMapper;
import com.yerman.produccion_api.domain.model.Insumo;
import com.yerman.produccion_api.domain.port.in.GestionInsumoUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/insumos")
public class InsumoController {

    private final GestionInsumoUseCase gestionInsumoUseCase;

    public InsumoController(GestionInsumoUseCase gestionInsumoUseCase) {
        this.gestionInsumoUseCase = gestionInsumoUseCase;
    }

    @PostMapping
    public ResponseEntity<InsumoResponse> crearInsumo(
            @Valid @RequestBody InsumoRequest request) {

        Insumo insumo = InsumoMapper.toDomain(request);
        Insumo creado = gestionInsumoUseCase.crearInsumo(insumo);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(InsumoMapper.toResponse(creado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsumoResponse> obtenerPorId(@PathVariable Long id) {
        Insumo insumo = gestionInsumoUseCase.obtenerPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Insumo no encontrado con id: " + id));

        return ResponseEntity.ok(InsumoMapper.toResponse(insumo));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<InsumoResponse> obtenerPorNombre(@PathVariable String nombre) {
        Insumo insumo = gestionInsumoUseCase.obtenerPorNombre(nombre)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Insumo no encontrado con nombre: " + nombre));

        return ResponseEntity.ok(InsumoMapper.toResponse(insumo));
    }

    @GetMapping
    public ResponseEntity<List<InsumoResponse>> listarTodos() {
        List<InsumoResponse> response = gestionInsumoUseCase.listarTodos()
                .stream()
                .map(InsumoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<InsumoResponse>> listarActivos() {
        List<InsumoResponse> response = gestionInsumoUseCase.listarActivos()
                .stream()
                .map(InsumoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }
}
