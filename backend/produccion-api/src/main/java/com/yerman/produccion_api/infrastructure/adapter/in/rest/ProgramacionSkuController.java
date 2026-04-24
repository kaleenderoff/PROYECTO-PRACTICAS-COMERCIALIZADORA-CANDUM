package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.ProgramacionSkuRequest;
import com.yerman.produccion_api.application.dto.response.ProgramacionSkuResponse;
import com.yerman.produccion_api.application.mapper.ProgramacionSkuMapper;
import com.yerman.produccion_api.domain.model.ProgramacionSku;
import com.yerman.produccion_api.domain.port.in.GestionProgramacionSkuUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/programacion-skus")
public class ProgramacionSkuController {

    private final GestionProgramacionSkuUseCase useCase;

    public ProgramacionSkuController(GestionProgramacionSkuUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<ProgramacionSkuResponse> agregarSku(
            @Valid @RequestBody ProgramacionSkuRequest request) {
        ProgramacionSku programacionSku = new ProgramacionSku();
        programacionSku.setIdProgramacion(request.getIdProgramacion());
        programacionSku.setIdSku(request.getIdSku());
        programacionSku.setUnidadesObjetivo(request.getUnidadesObjetivo());
        programacionSku.setObservaciones(request.getObservaciones());

        ProgramacionSku creada = useCase.agregarSku(programacionSku);

        return ResponseEntity.status(HttpStatus.CREATED).body(ProgramacionSkuMapper.toResponse(creada));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramacionSkuResponse> obtenerPorId(@PathVariable Long id) {
        return useCase.obtenerPorId(id)
                .map(ProgramacionSkuMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/programacion/{idProgramacion}")
    public ResponseEntity<List<ProgramacionSkuResponse>> listarPorProgramacion(@PathVariable Long idProgramacion) {
        List<ProgramacionSkuResponse> response = useCase.listarPorProgramacion(idProgramacion)
                .stream()
                .map(ProgramacionSkuMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }
}