package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.LineaProduccionRequest;
import com.yerman.produccion_api.application.dto.response.LineaProduccionResponse;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.mapper.LineaProduccionMapper;
import com.yerman.produccion_api.domain.model.LineaProduccion;
import com.yerman.produccion_api.domain.port.in.GestionLineaProduccionUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lineas-produccion")
public class LineaProduccionController {

    private final GestionLineaProduccionUseCase gestionLineaProduccionUseCase;

    public LineaProduccionController(GestionLineaProduccionUseCase gestionLineaProduccionUseCase) {
        this.gestionLineaProduccionUseCase = gestionLineaProduccionUseCase;
    }

    @PostMapping
    public ResponseEntity<LineaProduccionResponse> crearLineaProduccion(
            @Valid @RequestBody LineaProduccionRequest request) {

        LineaProduccion linea = LineaProduccionMapper.toDomain(request);
        LineaProduccion creada = gestionLineaProduccionUseCase.crearLineaProduccion(linea);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(LineaProduccionMapper.toResponse(creada));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineaProduccionResponse> obtenerPorId(@PathVariable Long id) {
        LineaProduccion linea = gestionLineaProduccionUseCase.obtenerPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Línea de producción no encontrada con id: " + id));

        return ResponseEntity.ok(LineaProduccionMapper.toResponse(linea));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<LineaProduccionResponse> obtenerPorNombre(@PathVariable String nombre) {
        LineaProduccion linea = gestionLineaProduccionUseCase.obtenerPorNombre(nombre)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Línea de producción no encontrada con nombre: " + nombre));

        return ResponseEntity.ok(LineaProduccionMapper.toResponse(linea));
    }

    @GetMapping
    public ResponseEntity<List<LineaProduccionResponse>> listarTodas() {
        List<LineaProduccionResponse> response = gestionLineaProduccionUseCase.listarTodas()
                .stream()
                .map(LineaProduccionMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/activas")
    public ResponseEntity<List<LineaProduccionResponse>> listarActivas() {
        List<LineaProduccionResponse> response = gestionLineaProduccionUseCase.listarActivas()
                .stream()
                .map(LineaProduccionMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }
}
