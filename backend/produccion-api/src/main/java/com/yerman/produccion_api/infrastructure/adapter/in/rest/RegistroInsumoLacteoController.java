package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.RegistroInsumoLacteoRequest;
import com.yerman.produccion_api.application.dto.response.RegistroInsumoLacteoResponse;
import com.yerman.produccion_api.application.mapper.RegistroInsumoLacteoRestMapper;
import com.yerman.produccion_api.domain.port.in.GestionRegistroInsumoLacteoUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/registros-insumo-lacteo")
public class RegistroInsumoLacteoController {

    private final GestionRegistroInsumoLacteoUseCase useCase;

    public RegistroInsumoLacteoController(GestionRegistroInsumoLacteoUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegistroInsumoLacteoResponse registrar(@Valid @RequestBody RegistroInsumoLacteoRequest request) {
        return RegistroInsumoLacteoRestMapper.toResponse(
                useCase.registrar(
                        RegistroInsumoLacteoRestMapper.toDomain(request)));
    }

    @GetMapping("/{id}")
    public RegistroInsumoLacteoResponse obtenerPorId(@PathVariable Long id) {
        return RegistroInsumoLacteoRestMapper.toResponse(
                useCase.obtenerPorId(id));
    }

    @GetMapping("/produccion/{idProduccionLactea}")
    public List<RegistroInsumoLacteoResponse> listarPorProduccion(@PathVariable Long idProduccionLactea) {
        return useCase.listarPorProduccion(idProduccionLactea)
                .stream()
                .map(RegistroInsumoLacteoRestMapper::toResponse)
                .toList();
    }

    @GetMapping("/batch/{idProduccionLacteaBatch}")
    public List<RegistroInsumoLacteoResponse> listarPorBatch(@PathVariable Long idProduccionLacteaBatch) {
        return useCase.listarPorBatch(idProduccionLacteaBatch)
                .stream()
                .map(RegistroInsumoLacteoRestMapper::toResponse)
                .toList();
    }
}
