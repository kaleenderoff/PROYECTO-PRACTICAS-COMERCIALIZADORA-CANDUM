package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.RecepcionLecheRequest;
import com.yerman.produccion_api.application.dto.response.RecepcionLecheResponse;
import com.yerman.produccion_api.application.mapper.RecepcionLecheRestMapper;
import com.yerman.produccion_api.domain.port.in.GestionRecepcionLecheUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/recepciones-leche")
public class RecepcionLecheController {

    private final GestionRecepcionLecheUseCase useCase;

    public RecepcionLecheController(GestionRecepcionLecheUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecepcionLecheResponse registrar(@Valid @RequestBody RecepcionLecheRequest request) {
        return RecepcionLecheRestMapper.toResponse(
                useCase.registrarRecepcion(
                        RecepcionLecheRestMapper.toDomain(request)));
    }

    @GetMapping("/{id}")
    public RecepcionLecheResponse obtenerPorId(@PathVariable Long id) {
        return RecepcionLecheRestMapper.toResponse(
                useCase.obtenerPorId(id));
    }

    @GetMapping
    public List<RecepcionLecheResponse> listarTodas() {
        return useCase.listarTodas()
                .stream()
                .map(RecepcionLecheRestMapper::toResponse)
                .toList();
    }

    @GetMapping("/fecha/{fecha}")
    public List<RecepcionLecheResponse> listarPorFecha(@PathVariable LocalDate fecha) {
        return useCase.listarPorFecha(fecha)
                .stream()
                .map(RecepcionLecheRestMapper::toResponse)
                .toList();
    }

    @GetMapping("/proveedor")
    public List<RecepcionLecheResponse> listarPorProveedor(@RequestParam String nombre) {
        return useCase.listarPorProveedor(nombre)
                .stream()
                .map(RecepcionLecheRestMapper::toResponse)
                .toList();
    }
}