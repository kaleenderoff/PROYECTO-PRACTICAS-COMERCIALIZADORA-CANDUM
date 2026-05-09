package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.ProgramacionSkuRequest;
import com.yerman.produccion_api.application.dto.response.ProgramacionSkuResponse;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.mapper.ProgramacionSkuMapper;
import com.yerman.produccion_api.domain.model.ProgramacionSku;
import com.yerman.produccion_api.domain.port.in.GestionProgramacionSkuUseCase;
import com.yerman.produccion_api.infrastructure.entity.ProgramacionSkuEntity;
import com.yerman.produccion_api.infrastructure.repository.ProgramacionSkuJpaRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/programacion-skus")
public class ProgramacionSkuController {

        private final GestionProgramacionSkuUseCase useCase;
        private final ProgramacionSkuJpaRepository programacionSkuRepository;

        public ProgramacionSkuController(
                        GestionProgramacionSkuUseCase useCase,
                        ProgramacionSkuJpaRepository programacionSkuRepository) {
                this.useCase = useCase;
                this.programacionSkuRepository = programacionSkuRepository;
        }

        @PostMapping
        public ResponseEntity<ProgramacionSkuResponse> agregarSku(
                        @Valid @RequestBody ProgramacionSkuRequest request) {

                ProgramacionSku programacionSku = toDomain(request);

                ProgramacionSku creada = useCase.agregarSku(programacionSku);

                ProgramacionSkuEntity entity = buscarEntity(creada.getId());

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(ProgramacionSkuMapper.toResponse(entity));
        }

        @PutMapping("/{id}")
        public ResponseEntity<ProgramacionSkuResponse> actualizarSku(
                        @PathVariable Long id,
                        @Valid @RequestBody ProgramacionSkuRequest request) {

                ProgramacionSku programacionSku = toDomain(request);

                ProgramacionSku actualizada = useCase.actualizarSku(id, programacionSku);

                ProgramacionSkuEntity entity = buscarEntity(actualizada.getId());

                return ResponseEntity.ok(ProgramacionSkuMapper.toResponse(entity));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> eliminarSku(@PathVariable Long id) {
                useCase.eliminarSku(id);
                return ResponseEntity.noContent().build();
        }

        @GetMapping("/{id}")
        public ResponseEntity<ProgramacionSkuResponse> obtenerPorId(@PathVariable Long id) {
                return programacionSkuRepository.findById(id)
                                .map(ProgramacionSkuMapper::toResponse)
                                .map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build());
        }

        @GetMapping("/programacion/{idProgramacion}")
        public ResponseEntity<List<ProgramacionSkuResponse>> listarPorProgramacion(@PathVariable Long idProgramacion) {
                List<ProgramacionSkuEntity> entities = programacionSkuRepository
                                .findByProgramacionIdOrderByIdAsc(idProgramacion);

                List<ProgramacionSkuResponse> response = entities.stream()
                                .map(ProgramacionSkuMapper::toResponse)
                                .toList();

                return ResponseEntity.ok(response);
        }

        private ProgramacionSku toDomain(ProgramacionSkuRequest request) {
                ProgramacionSku programacionSku = new ProgramacionSku();
                programacionSku.setIdProgramacion(request.getIdProgramacion());
                programacionSku.setIdSku(request.getIdSku());
                programacionSku.setUnidadesObjetivo(request.getUnidadesObjetivo());
                programacionSku.setObservaciones(request.getObservaciones());
                return programacionSku;
        }

        private ProgramacionSkuEntity buscarEntity(Long id) {
                return programacionSkuRepository.findById(id)
                                .orElseThrow(() -> new RecursoNoEncontradoException(
                                                "Programación SKU no encontrada con id: " + id));
        }
}