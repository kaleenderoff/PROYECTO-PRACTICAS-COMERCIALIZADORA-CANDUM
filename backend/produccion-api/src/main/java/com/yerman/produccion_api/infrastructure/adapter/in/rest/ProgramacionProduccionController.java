package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.ProgramacionProduccionRequest;
import com.yerman.produccion_api.application.dto.request.SimularProgramacionRequest;
import com.yerman.produccion_api.application.dto.response.ProgramacionProduccionResponse;
import com.yerman.produccion_api.application.dto.response.SimularProgramacionResponse;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.mapper.ProgramacionProduccionMapper;
import com.yerman.produccion_api.application.service.SimulacionProgramacionService;
import com.yerman.produccion_api.domain.model.ProgramacionProduccion;
import com.yerman.produccion_api.domain.port.in.GestionProgramacionProduccionUseCase;
import com.yerman.produccion_api.infrastructure.entity.ProgramacionProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.ProgramacionSkuEntity;
import com.yerman.produccion_api.infrastructure.repository.ProgramacionProduccionJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.ProgramacionSkuJpaRepository;
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
    private final ProgramacionProduccionJpaRepository programacionRepository;
    private final ProgramacionSkuJpaRepository programacionSkuRepository;
    private final SimulacionProgramacionService simulacionService;

    public ProgramacionProduccionController(
            GestionProgramacionProduccionUseCase useCase,
            ProgramacionProduccionJpaRepository programacionRepository,
            ProgramacionSkuJpaRepository programacionSkuRepository,
            SimulacionProgramacionService simulacionService) {
        this.useCase = useCase;
        this.programacionRepository = programacionRepository;
        this.programacionSkuRepository = programacionSkuRepository;
        this.simulacionService = simulacionService;
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
        programacion.setIdJefeProduccion(request.getIdJefeProduccion());
        programacion.setObservaciones(request.getObservaciones());

        ProgramacionProduccion creada = useCase.crear(programacion);

        ProgramacionProduccionEntity entity = buscarProgramacionEntity(creada.getId());
        List<ProgramacionSkuEntity> skus = buscarSkus(entity.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ProgramacionProduccionMapper.toResponse(entity, skus));
    }

    @PostMapping("/simular")
    public ResponseEntity<SimularProgramacionResponse> simular(
            @Valid @RequestBody SimularProgramacionRequest request) {

        SimularProgramacionResponse response = simulacionService.simular(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramacionProduccionResponse> obtenerPorId(@PathVariable Long id) {

        ProgramacionProduccionEntity entity = buscarProgramacionEntity(id);
        List<ProgramacionSkuEntity> skus = buscarSkus(id);

        return ResponseEntity.ok(
                ProgramacionProduccionMapper.toResponse(entity, skus));
    }

    @GetMapping
    public ResponseEntity<List<ProgramacionProduccionResponse>> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        List<ProgramacionProduccion> programaciones = fecha != null
                ? useCase.listarPorFecha(fecha)
                : useCase.listarTodas();

        List<ProgramacionProduccionResponse> response = programaciones.stream()
                .map(programacion -> {
                    ProgramacionProduccionEntity entity = buscarProgramacionEntity(programacion.getId());

                    List<ProgramacionSkuEntity> skus = buscarSkus(entity.getId());

                    return ProgramacionProduccionMapper.toResponse(entity, skus);
                })
                .toList();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ProgramacionProduccionResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {

        ProgramacionProduccion actualizada = useCase.cambiarEstado(id, estado);

        ProgramacionProduccionEntity entity = buscarProgramacionEntity(actualizada.getId());

        List<ProgramacionSkuEntity> skus = buscarSkus(entity.getId());

        return ResponseEntity.ok(
                ProgramacionProduccionMapper.toResponse(entity, skus));
    }

    private ProgramacionProduccionEntity buscarProgramacionEntity(Long id) {
        return programacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Programación no encontrada con id: " + id));
    }

    private List<ProgramacionSkuEntity> buscarSkus(Long idProgramacion) {
        return programacionSkuRepository
                .findByProgramacionIdOrderByIdAsc(idProgramacion);
    }
}