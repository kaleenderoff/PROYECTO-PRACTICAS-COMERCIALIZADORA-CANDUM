package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.DetalleProduccionRequest;
import com.yerman.produccion_api.application.dto.response.DetalleProduccionResponse;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.mapper.DetalleProduccionMapper;
import com.yerman.produccion_api.domain.model.DetalleProduccion;
import com.yerman.produccion_api.domain.port.in.GestionDetalleProduccionUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DetalleProduccionController {

    private final GestionDetalleProduccionUseCase gestionDetalleProduccionUseCase;

    public DetalleProduccionController(GestionDetalleProduccionUseCase gestionDetalleProduccionUseCase) {
        this.gestionDetalleProduccionUseCase = gestionDetalleProduccionUseCase;
    }

    @PostMapping("/detalle-produccion")
    public ResponseEntity<DetalleProduccionResponse> crearDetalleProduccion(
            @Valid @RequestBody DetalleProduccionRequest request) {

        DetalleProduccion detalle = DetalleProduccionMapper.toDomain(request);
        DetalleProduccion creado = gestionDetalleProduccionUseCase.crearDetalleProduccion(detalle);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DetalleProduccionMapper.toResponse(creado));
    }

    @GetMapping("/detalle-produccion/{id}")
    public ResponseEntity<DetalleProduccionResponse> obtenerPorId(@PathVariable Long id) {
        DetalleProduccion detalle = gestionDetalleProduccionUseCase.obtenerPorId(id)
                .orElseThrow(
                        () -> new RecursoNoEncontradoException("Detalle de producción no encontrado con id: " + id));

        return ResponseEntity.ok(DetalleProduccionMapper.toResponse(detalle));
    }

    @GetMapping("/detalle-produccion")
    public ResponseEntity<List<DetalleProduccionResponse>> listarTodos() {
        List<DetalleProduccionResponse> response = gestionDetalleProduccionUseCase.listarTodos()
                .stream()
                .map(DetalleProduccionMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/detalle-produccion/produccion/{idProduccion}")
    public ResponseEntity<List<DetalleProduccionResponse>> listarPorProduccion(@PathVariable Long idProduccion) {
        List<DetalleProduccionResponse> response = gestionDetalleProduccionUseCase.listarPorProduccion(idProduccion)
                .stream()
                .map(DetalleProduccionMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/detalle-produccion/producto/{idProducto}")
    public ResponseEntity<List<DetalleProduccionResponse>> listarPorProducto(@PathVariable Long idProducto) {
        List<DetalleProduccionResponse> response = gestionDetalleProduccionUseCase.listarPorProducto(idProducto)
                .stream()
                .map(DetalleProduccionMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/detalle-produccion/buscar")
    public ResponseEntity<DetalleProduccionResponse> obtenerPorProduccionProductoBatch(
            @RequestParam Long idProduccion,
            @RequestParam Long idProducto,
            @RequestParam Integer numBatch) {

        DetalleProduccion detalle = gestionDetalleProduccionUseCase
                .obtenerPorProduccionProductoBatch(idProduccion, idProducto, numBatch)
                .orElseThrow(() -> new RecursoNoEncontradoException("Detalle de producción no encontrado"));

        return ResponseEntity.ok(DetalleProduccionMapper.toResponse(detalle));
    }
}
