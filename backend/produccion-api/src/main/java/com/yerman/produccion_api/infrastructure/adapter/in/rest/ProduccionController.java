package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.ProduccionRequest;
import com.yerman.produccion_api.application.dto.response.PageResponse;
import com.yerman.produccion_api.application.dto.response.ProduccionResponse;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.mapper.ProduccionMapper;
import com.yerman.produccion_api.domain.model.Produccion;
import com.yerman.produccion_api.domain.model.ProduccionFiltro;
import com.yerman.produccion_api.domain.port.in.GestionProduccionUseCase;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/producciones")
public class ProduccionController {

        private final GestionProduccionUseCase gestionProduccionUseCase;

        public ProduccionController(GestionProduccionUseCase gestionProduccionUseCase) {
                this.gestionProduccionUseCase = gestionProduccionUseCase;
        }

        @PostMapping
        public ResponseEntity<ProduccionResponse> crearProduccion(@Valid @RequestBody ProduccionRequest request) {
                Produccion produccion = ProduccionMapper.toDomain(request);
                Produccion creada = gestionProduccionUseCase.crearProduccion(produccion);

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ProduccionMapper.toResponse(creada));
        }

        @GetMapping("/{id}")
        public ResponseEntity<ProduccionResponse> obtenerPorId(@PathVariable Long id) {
                Produccion produccion = gestionProduccionUseCase.obtenerPorId(id)
                                .orElseThrow(() -> new RecursoNoEncontradoException(
                                                "Producción no encontrada con id: " + id));

                return ResponseEntity.ok(ProduccionMapper.toResponse(produccion));
        }

        @GetMapping
        public ResponseEntity<List<ProduccionResponse>> listarTodas() {
                List<ProduccionResponse> response = gestionProduccionUseCase.listarTodas()
                                .stream()
                                .map(ProduccionMapper::toResponse)
                                .toList();

                return ResponseEntity.ok(response);
        }

        @GetMapping("/paginado")
        public ResponseEntity<PageResponse<ProduccionResponse>> listarPaginado(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "idProduccion") String sortBy,
                        @RequestParam(defaultValue = "desc") String direction) {

                Sort sort = direction.equalsIgnoreCase("asc")
                                ? Sort.by(sortBy).ascending()
                                : Sort.by(sortBy).descending();

                Pageable pageable = PageRequest.of(page, size, sort);

                Page<ProduccionResponse> result = gestionProduccionUseCase.listarPaginado(pageable)
                                .map(ProduccionMapper::toResponse);

                PageResponse<ProduccionResponse> response = new PageResponse<>(
                                result.getContent(),
                                result.getNumber(),
                                result.getSize(),
                                result.getTotalElements(),
                                result.getTotalPages(),
                                result.isLast());

                return ResponseEntity.ok(response);
        }

        @GetMapping("/filtros")
        public ResponseEntity<PageResponse<ProduccionResponse>> filtrar(
                        @RequestParam(required = false) String numeroLote,
                        @RequestParam(required = false) String estado,
                        @RequestParam(required = false) LocalDate fechaDesde,
                        @RequestParam(required = false) LocalDate fechaHasta,
                        @RequestParam(required = false) Long idLineaProduccion,
                        @RequestParam(required = false) Long idOperario,
                        @RequestParam(required = false) Long idJefeLinea,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "idProduccion") String sortBy,
                        @RequestParam(defaultValue = "desc") String direction) {

                Sort sort = direction.equalsIgnoreCase("asc")
                                ? Sort.by(sortBy).ascending()
                                : Sort.by(sortBy).descending();

                Pageable pageable = PageRequest.of(page, size, sort);

                ProduccionFiltro filtro = new ProduccionFiltro();
                filtro.setNumeroLote(numeroLote);
                filtro.setEstado(estado);
                filtro.setFechaDesde(fechaDesde);
                filtro.setFechaHasta(fechaHasta);
                filtro.setIdLineaProduccion(idLineaProduccion);
                filtro.setIdOperario(idOperario);
                filtro.setIdJefeLinea(idJefeLinea);

                Page<ProduccionResponse> result = gestionProduccionUseCase
                                .filtrar(filtro, pageable)
                                .map(ProduccionMapper::toResponse);

                PageResponse<ProduccionResponse> response = new PageResponse<>(
                                result.getContent(),
                                result.getNumber(),
                                result.getSize(),
                                result.getTotalElements(),
                                result.getTotalPages(),
                                result.isLast());

                return ResponseEntity.ok(response);
        }

        @GetMapping("/lote/{numeroLote}")
        public ResponseEntity<ProduccionResponse> obtenerPorNumeroLote(@PathVariable String numeroLote) {
                Produccion produccion = gestionProduccionUseCase.obtenerPorNumeroLote(numeroLote)
                                .orElseThrow(() -> new RecursoNoEncontradoException(
                                                "Producción no encontrada con lote: " + numeroLote));

                return ResponseEntity.ok(ProduccionMapper.toResponse(produccion));
        }

        @GetMapping("/fecha/{fechaProduccion}")
        public ResponseEntity<List<ProduccionResponse>> listarPorFecha(@PathVariable LocalDate fechaProduccion) {
                List<ProduccionResponse> response = gestionProduccionUseCase.listarPorFecha(fechaProduccion)
                                .stream()
                                .map(ProduccionMapper::toResponse)
                                .toList();

                return ResponseEntity.ok(response);
        }

        @GetMapping("/estado/{estado}")
        public ResponseEntity<List<ProduccionResponse>> listarPorEstado(@PathVariable String estado) {
                List<ProduccionResponse> response = gestionProduccionUseCase.listarPorEstado(estado)
                                .stream()
                                .map(ProduccionMapper::toResponse)
                                .toList();

                return ResponseEntity.ok(response);
        }

        @GetMapping("/linea/{idLineaProduccion}")
        public ResponseEntity<List<ProduccionResponse>> listarPorLineaProduccion(@PathVariable Long idLineaProduccion) {
                List<ProduccionResponse> response = gestionProduccionUseCase.listarPorLineaProduccion(idLineaProduccion)
                                .stream()
                                .map(ProduccionMapper::toResponse)
                                .toList();

                return ResponseEntity.ok(response);
        }

        @GetMapping("/operario/{idOperario}")
        public ResponseEntity<List<ProduccionResponse>> listarPorOperario(@PathVariable Long idOperario) {
                List<ProduccionResponse> response = gestionProduccionUseCase.listarPorOperario(idOperario)
                                .stream()
                                .map(ProduccionMapper::toResponse)
                                .toList();

                return ResponseEntity.ok(response);
        }

        @GetMapping("/jefe-linea/{idJefeLinea}")
        public ResponseEntity<List<ProduccionResponse>> listarPorJefeLinea(@PathVariable Long idJefeLinea) {
                List<ProduccionResponse> response = gestionProduccionUseCase.listarPorJefeLinea(idJefeLinea)
                                .stream()
                                .map(ProduccionMapper::toResponse)
                                .toList();

                return ResponseEntity.ok(response);
        }
}