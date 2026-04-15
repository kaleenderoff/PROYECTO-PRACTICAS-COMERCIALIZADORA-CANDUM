package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.ValidacionRequest;
import com.yerman.produccion_api.application.dto.response.ValidacionResponse;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.application.mapper.ValidacionMapper;
import com.yerman.produccion_api.domain.model.EstadoValidacion;
import com.yerman.produccion_api.domain.model.Validacion;
import com.yerman.produccion_api.domain.port.in.GestionValidacionUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/validaciones")
public class ValidacionController {

        private final GestionValidacionUseCase gestionValidacionUseCase;

        public ValidacionController(GestionValidacionUseCase gestionValidacionUseCase) {
                this.gestionValidacionUseCase = gestionValidacionUseCase;
        }

        @PostMapping
        public ResponseEntity<ValidacionResponse> crearValidacion(
                        @Valid @RequestBody ValidacionRequest request) {

                Validacion validacion = ValidacionMapper.toDomain(request);
                Validacion creada = gestionValidacionUseCase.crearValidacion(validacion);

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ValidacionMapper.toResponse(creada));
        }

        @GetMapping("/{id}")
        public ResponseEntity<ValidacionResponse> obtenerPorId(@PathVariable Long id) {
                Validacion validacion = gestionValidacionUseCase.obtenerPorId(id)
                                .orElseThrow(() -> new RecursoNoEncontradoException(
                                                "Validación no encontrada con id: " + id));

                return ResponseEntity.ok(ValidacionMapper.toResponse(validacion));
        }

        @GetMapping
        public ResponseEntity<List<ValidacionResponse>> listarTodas() {
                List<ValidacionResponse> response = gestionValidacionUseCase.listarTodas()
                                .stream()
                                .map(ValidacionMapper::toResponse)
                                .toList();

                return ResponseEntity.ok(response);
        }

        @GetMapping("/detalle/{idDetalleProduccion}")
        public ResponseEntity<ValidacionResponse> obtenerPorDetalleProduccion(
                        @PathVariable Long idDetalleProduccion) {

                Validacion validacion = gestionValidacionUseCase
                                .obtenerPorDetalleProduccion(idDetalleProduccion)
                                .orElseThrow(() -> new RecursoNoEncontradoException(
                                                "Validación no encontrada para el detalle de producción con id: "
                                                                + idDetalleProduccion));

                return ResponseEntity.ok(ValidacionMapper.toResponse(validacion));
        }

        @GetMapping("/estado/{estado}")
        public ResponseEntity<List<ValidacionResponse>> listarPorEstado(@PathVariable String estado) {
                EstadoValidacion estadoValidacion;

                try {
                        estadoValidacion = EstadoValidacion.valueOf(estado.trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                        throw new ReglaNegocioException("Estado de validación inválido");
                }

                List<ValidacionResponse> response = gestionValidacionUseCase
                                .listarPorEstado(estadoValidacion)
                                .stream()
                                .map(ValidacionMapper::toResponse)
                                .toList();

                return ResponseEntity.ok(response);
        }

        @GetMapping("/validador/{idValidador}")
        public ResponseEntity<List<ValidacionResponse>> listarPorValidador(
                        @PathVariable Long idValidador) {

                List<ValidacionResponse> response = gestionValidacionUseCase
                                .listarPorValidador(idValidador)
                                .stream()
                                .map(ValidacionMapper::toResponse)
                                .toList();

                return ResponseEntity.ok(response);
        }
}