package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.ProgramacionProduccionRequest;
import com.yerman.produccion_api.application.dto.response.ProgramacionProduccionResponse;
import com.yerman.produccion_api.application.mapper.ProgramacionProduccionMapper;
import com.yerman.produccion_api.domain.model.ProgramacionProduccion;
import com.yerman.produccion_api.domain.port.in.GestionProgramacionProduccionUseCase;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/programaciones")
public class ProgramacionProduccionController {

    private final GestionProgramacionProduccionUseCase useCase;

    public ProgramacionProduccionController(GestionProgramacionProduccionUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<ProgramacionProduccionResponse> crear(
            @Valid @RequestBody ProgramacionProduccionRequest request) {
        ProgramacionProduccion programacion = new ProgramacionProduccion();
        programacion.setFechaProduccion(request.getFechaProduccion());
        programacion.setIdLinea(request.getIdLinea());
        programacion.setIdProducto(request.getIdProducto());
        programacion.setIdTurno(request.getIdTurno());
        programacion.setNumBachesPlan(request.getNumBachesPlan());
        programacion.setKgBachePlan(request.getKgBachePlan());
        programacion.setIdFormulaVersion(request.getIdFormulaVersion());
        programacion.setIdJefeProduccion(request.getIdJefeProduccion());
        programacion.setObservaciones(request.getObservaciones());

        ProgramacionProduccion creada = useCase.crear(programacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProgramacionProduccionMapper.toResponse(creada));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramacionProduccionResponse> obtenerPorId(@PathVariable Long id) {
        return useCase.obtenerPorId(id)
                .map(ProgramacionProduccionMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ProgramacionProduccionResponse>> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<ProgramacionProduccion> programaciones = (fecha != null)
                ? useCase.listarPorFecha(fecha)
                : useCase.listarTodas();

        List<ProgramacionProduccionResponse> response = programaciones.stream()
                .map(ProgramacionProduccionMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ProgramacionProduccionResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        ProgramacionProduccion actualizada = useCase.cambiarEstado(id, estado);
        return ResponseEntity.ok(ProgramacionProduccionMapper.toResponse(actualizada));
    }
}