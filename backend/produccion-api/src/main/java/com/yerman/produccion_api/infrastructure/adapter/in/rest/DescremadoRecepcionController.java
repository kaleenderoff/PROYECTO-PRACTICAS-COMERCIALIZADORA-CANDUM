package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.DescremadoRecepcionRequest;
import com.yerman.produccion_api.application.dto.response.DescremadoRecepcionResponse;
import com.yerman.produccion_api.application.mapper.DescremadoRecepcionRestMapper;
import com.yerman.produccion_api.domain.port.in.GestionDescremadoRecepcionUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/descremados-recepcion")
public class DescremadoRecepcionController {

    private final GestionDescremadoRecepcionUseCase useCase;

    public DescremadoRecepcionController(GestionDescremadoRecepcionUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DescremadoRecepcionResponse registrar(@Valid @RequestBody DescremadoRecepcionRequest request) {
        return DescremadoRecepcionRestMapper.toResponse(
                useCase.registrarDescremado(
                        DescremadoRecepcionRestMapper.toDomain(request)));
    }

    @GetMapping("/{id}")
    public DescremadoRecepcionResponse obtenerPorId(@PathVariable Long id) {
        return DescremadoRecepcionRestMapper.toResponse(
                useCase.obtenerPorId(id));
    }

    @GetMapping
    public List<DescremadoRecepcionResponse> listarTodos() {
        return useCase.listarTodos()
                .stream()
                .map(DescremadoRecepcionRestMapper::toResponse)
                .toList();
    }

    @GetMapping("/recepcion/{idRecepcionLeche}")
    public List<DescremadoRecepcionResponse> listarPorRecepcion(@PathVariable Long idRecepcionLeche) {
        return useCase.listarPorRecepcion(idRecepcionLeche)
                .stream()
                .map(DescremadoRecepcionRestMapper::toResponse)
                .toList();
    }
}