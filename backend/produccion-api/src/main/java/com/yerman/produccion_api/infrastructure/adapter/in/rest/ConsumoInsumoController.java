package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.ConsumoInsumoRequest;
import com.yerman.produccion_api.application.dto.response.ConsumoInsumoResponse;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.mapper.ConsumoInsumoMapper;
import com.yerman.produccion_api.domain.model.ConsumoInsumo;
import com.yerman.produccion_api.domain.port.in.GestionConsumoInsumoUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ConsumoInsumoController {

    private final GestionConsumoInsumoUseCase gestionConsumoInsumoUseCase;

    public ConsumoInsumoController(GestionConsumoInsumoUseCase gestionConsumoInsumoUseCase) {
        this.gestionConsumoInsumoUseCase = gestionConsumoInsumoUseCase;
    }

    @PostMapping("/consumo-insumo")
    public ResponseEntity<ConsumoInsumoResponse> crearConsumoInsumo(
            @Valid @RequestBody ConsumoInsumoRequest request) {

        ConsumoInsumo consumo = ConsumoInsumoMapper.toDomain(request);
        ConsumoInsumo creado = gestionConsumoInsumoUseCase.crearConsumoInsumo(consumo);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ConsumoInsumoMapper.toResponse(creado));
    }

    @GetMapping("/consumo-insumo/{id}")
    public ResponseEntity<ConsumoInsumoResponse> obtenerPorId(@PathVariable Long id) {
        ConsumoInsumo consumo = gestionConsumoInsumoUseCase.obtenerPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Consumo de insumo no encontrado con id: " + id));

        return ResponseEntity.ok(ConsumoInsumoMapper.toResponse(consumo));
    }

    @GetMapping("/consumo-insumo")
    public ResponseEntity<List<ConsumoInsumoResponse>> listarTodos() {
        List<ConsumoInsumoResponse> response = gestionConsumoInsumoUseCase.listarTodos()
                .stream()
                .map(ConsumoInsumoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/consumo-insumo/produccion/{idProduccion}")
    public ResponseEntity<List<ConsumoInsumoResponse>> listarPorProduccion(@PathVariable Long idProduccion) {
        List<ConsumoInsumoResponse> response = gestionConsumoInsumoUseCase.listarPorProduccion(idProduccion)
                .stream()
                .map(ConsumoInsumoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/consumo-insumo/detalle/{idDetalleProduccion}")
    public ResponseEntity<List<ConsumoInsumoResponse>> listarPorDetalleProduccion(
            @PathVariable Long idDetalleProduccion) {
        List<ConsumoInsumoResponse> response = gestionConsumoInsumoUseCase
                .listarPorDetalleProduccion(idDetalleProduccion)
                .stream()
                .map(ConsumoInsumoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/consumo-insumo/insumo/{idInsumo}")
    public ResponseEntity<List<ConsumoInsumoResponse>> listarPorInsumo(@PathVariable Long idInsumo) {
        List<ConsumoInsumoResponse> response = gestionConsumoInsumoUseCase.listarPorInsumo(idInsumo)
                .stream()
                .map(ConsumoInsumoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/consumo-insumo/buscar")
    public ResponseEntity<ConsumoInsumoResponse> obtenerPorProduccionInsumoDetalle(
            @RequestParam Long idProduccion,
            @RequestParam Long idInsumo,
            @RequestParam Long idDetalleProduccion) {

        ConsumoInsumo consumo = gestionConsumoInsumoUseCase
                .obtenerPorProduccionInsumoDetalle(idProduccion, idInsumo, idDetalleProduccion)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Consumo de insumo no encontrado para la combinación producción, insumo y detalle"));

        return ResponseEntity.ok(ConsumoInsumoMapper.toResponse(consumo));
    }
}