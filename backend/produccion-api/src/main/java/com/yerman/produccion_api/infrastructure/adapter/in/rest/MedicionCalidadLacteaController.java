package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.MedicionCalidadLacteaRequest;
import com.yerman.produccion_api.application.dto.response.MedicionCalidadLacteaResponse;
import com.yerman.produccion_api.application.mapper.MedicionCalidadLacteaRestMapper;
import com.yerman.produccion_api.domain.port.in.GestionMedicionCalidadLacteaUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mediciones-calidad-lactea")
public class MedicionCalidadLacteaController {

    private final GestionMedicionCalidadLacteaUseCase useCase;

    public MedicionCalidadLacteaController(GestionMedicionCalidadLacteaUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedicionCalidadLacteaResponse registrar(@Valid @RequestBody MedicionCalidadLacteaRequest request) {
        return MedicionCalidadLacteaRestMapper.toResponse(
                useCase.registrar(
                        MedicionCalidadLacteaRestMapper.toDomain(request)));
    }

    @GetMapping("/{id}")
    public MedicionCalidadLacteaResponse obtenerPorId(@PathVariable Long id) {
        return MedicionCalidadLacteaRestMapper.toResponse(
                useCase.obtenerPorId(id));
    }

    @GetMapping("/produccion/{idProduccionLactea}")
    public List<MedicionCalidadLacteaResponse> listarPorProduccion(@PathVariable Long idProduccionLactea) {
        return useCase.listarPorProduccion(idProduccionLactea)
                .stream()
                .map(MedicionCalidadLacteaRestMapper::toResponse)
                .toList();
    }

    @GetMapping("/orden/{idOrdenProduccion}")
    public List<MedicionCalidadLacteaResponse> listarPorOrden(@PathVariable Long idOrdenProduccion) {
        return useCase.listarPorOrden(idOrdenProduccion)
                .stream()
                .map(MedicionCalidadLacteaRestMapper::toResponse)
                .toList();
    }
}
