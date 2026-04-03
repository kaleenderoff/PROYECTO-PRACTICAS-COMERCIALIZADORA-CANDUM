package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.ProduccionRequest;
import com.yerman.produccion_api.application.dto.response.ProduccionResponse;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.mapper.ProduccionMapper;
import com.yerman.produccion_api.domain.model.Produccion;
import com.yerman.produccion_api.domain.port.in.GestionProduccionUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class ProduccionController {

    private final GestionProduccionUseCase gestionProduccionUseCase;

    public ProduccionController(GestionProduccionUseCase gestionProduccionUseCase) {
        this.gestionProduccionUseCase = gestionProduccionUseCase;
    }

    @PostMapping("/producciones")
    public ResponseEntity<ProduccionResponse> crearProduccion(
            @Valid @RequestBody ProduccionRequest request) {

        Produccion produccion = ProduccionMapper.toDomain(request);
        Produccion creada = gestionProduccionUseCase.crearProduccion(produccion);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ProduccionMapper.toResponse(creada));
    }

    @GetMapping("/producciones/{id}")
    public ResponseEntity<ProduccionResponse> obtenerPorId(@PathVariable Long id) {
        Produccion produccion = gestionProduccionUseCase.obtenerPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producción no encontrada con id: " + id));

        return ResponseEntity.ok(ProduccionMapper.toResponse(produccion));
    }

    @GetMapping("/producciones")
    public ResponseEntity<List<ProduccionResponse>> listarTodas() {
        List<ProduccionResponse> response = gestionProduccionUseCase.listarTodas()
                .stream()
                .map(ProduccionMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/producciones/lote/{numeroLote}")
    public ResponseEntity<ProduccionResponse> obtenerPorNumeroLote(@PathVariable String numeroLote) {
        Produccion produccion = gestionProduccionUseCase.obtenerPorNumeroLote(numeroLote)
                .orElseThrow(
                        () -> new RecursoNoEncontradoException("Producción no encontrada con lote: " + numeroLote));

        return ResponseEntity.ok(ProduccionMapper.toResponse(produccion));
    }

    @GetMapping("/producciones/fecha/{fechaProduccion}")
    public ResponseEntity<List<ProduccionResponse>> listarPorFecha(@PathVariable LocalDate fechaProduccion) {
        List<ProduccionResponse> response = gestionProduccionUseCase.listarPorFecha(fechaProduccion)
                .stream()
                .map(ProduccionMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/producciones/estado/{estado}")
    public ResponseEntity<List<ProduccionResponse>> listarPorEstado(@PathVariable String estado) {
        List<ProduccionResponse> response = gestionProduccionUseCase.listarPorEstado(estado)
                .stream()
                .map(ProduccionMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/producciones/linea/{idLineaProduccion}")
    public ResponseEntity<List<ProduccionResponse>> listarPorLineaProduccion(@PathVariable Long idLineaProduccion) {
        List<ProduccionResponse> response = gestionProduccionUseCase.listarPorLineaProduccion(idLineaProduccion)
                .stream()
                .map(ProduccionMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/producciones/operario/{idOperario}")
    public ResponseEntity<List<ProduccionResponse>> listarPorOperario(@PathVariable Long idOperario) {
        List<ProduccionResponse> response = gestionProduccionUseCase.listarPorOperario(idOperario)
                .stream()
                .map(ProduccionMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/producciones/jefe-linea/{idJefeLinea}")
    public ResponseEntity<List<ProduccionResponse>> listarPorJefeLinea(@PathVariable Long idJefeLinea) {
        List<ProduccionResponse> response = gestionProduccionUseCase.listarPorJefeLinea(idJefeLinea)
                .stream()
                .map(ProduccionMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }
}
