package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.EmpaqueRequest;
import com.yerman.produccion_api.application.dto.response.EmpaqueResponse;
import com.yerman.produccion_api.application.mapper.EmpaqueMapper;
import com.yerman.produccion_api.domain.model.Empaque;
import com.yerman.produccion_api.domain.port.in.GestionEmpaqueUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/empaques")
public class EmpaqueController {

    private final GestionEmpaqueUseCase gestionEmpaqueUseCase;

    public EmpaqueController(GestionEmpaqueUseCase gestionEmpaqueUseCase) {
        this.gestionEmpaqueUseCase = gestionEmpaqueUseCase;
    }

    @PreAuthorize("hasAnyRole('ADMIN','INGENIERO')")
    @PostMapping
    public ResponseEntity<EmpaqueResponse> registrar(@RequestBody EmpaqueRequest request) {
        Empaque empaque = EmpaqueMapper.toDomain(request);
        Empaque creado = gestionEmpaqueUseCase.registrarEmpaque(empaque);
        return ResponseEntity.status(HttpStatus.CREATED).body(EmpaqueMapper.toResponse(creado));
    }

    @PreAuthorize("hasAnyRole('ADMIN','INGENIERO')")
    @PutMapping("/{id}")
    public ResponseEntity<EmpaqueResponse> actualizar(@PathVariable Long id,
            @RequestBody EmpaqueRequest request) {
        Empaque empaque = EmpaqueMapper.toDomain(request);
        Empaque actualizado = gestionEmpaqueUseCase.actualizarEmpaque(id, empaque);
        return ResponseEntity.ok(EmpaqueMapper.toResponse(actualizado));
    }

    @PreAuthorize("hasAnyRole('ADMIN','INGENIERO','JEFE_PLANTA','JEFE_LINEA')")
    @GetMapping("/{id}")
    public ResponseEntity<EmpaqueResponse> obtenerPorId(@PathVariable Long id) {
        return gestionEmpaqueUseCase.obtenerPorId(id)
                .map(EmpaqueMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('ADMIN','INGENIERO','JEFE_PLANTA','JEFE_LINEA')")
    @GetMapping
    public ResponseEntity<List<EmpaqueResponse>> listarTodos() {
        List<EmpaqueResponse> response = gestionEmpaqueUseCase.listarTodos()
                .stream()
                .map(EmpaqueMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','INGENIERO','JEFE_PLANTA','JEFE_LINEA')")
    @GetMapping("/detalle-produccion/{idDetalleProduccion}")
    public ResponseEntity<List<EmpaqueResponse>> listarPorDetalleProduccion(@PathVariable Long idDetalleProduccion) {
        List<EmpaqueResponse> response = gestionEmpaqueUseCase.listarPorDetalleProduccion(idDetalleProduccion)
                .stream()
                .map(EmpaqueMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','INGENIERO','JEFE_PLANTA','JEFE_LINEA')")
    @GetMapping("/producto-terminado/{idProductoTerminado}")
    public ResponseEntity<List<EmpaqueResponse>> listarPorProductoTerminado(@PathVariable Long idProductoTerminado) {
        List<EmpaqueResponse> response = gestionEmpaqueUseCase.listarPorProductoTerminado(idProductoTerminado)
                .stream()
                .map(EmpaqueMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','INGENIERO','JEFE_PLANTA','JEFE_LINEA')")
    @GetMapping("/lote/{loteEmpaque}")
    public ResponseEntity<List<EmpaqueResponse>> listarPorLoteEmpaque(@PathVariable String loteEmpaque) {
        List<EmpaqueResponse> response = gestionEmpaqueUseCase.listarPorLoteEmpaque(loteEmpaque)
                .stream()
                .map(EmpaqueMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','INGENIERO','JEFE_PLANTA','JEFE_LINEA')")
    @GetMapping("/rango-fecha")
    public ResponseEntity<List<EmpaqueResponse>> listarPorRangoFecha(@RequestParam LocalDateTime fechaInicio,
            @RequestParam LocalDateTime fechaFin) {
        List<EmpaqueResponse> response = gestionEmpaqueUseCase.listarPorRangoFecha(fechaInicio, fechaFin)
                .stream()
                .map(EmpaqueMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }
}