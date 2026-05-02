package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.ProductoTerminadoLacteoRequest;
import com.yerman.produccion_api.application.dto.response.ProductoTerminadoLacteoResponse;
import com.yerman.produccion_api.application.mapper.ProductoTerminadoLacteoRestMapper;
import com.yerman.produccion_api.domain.model.EstadoProductoTerminadoLacteo;
import com.yerman.produccion_api.domain.port.in.GestionProductoTerminadoLacteoUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos-terminados-lacteos")
public class ProductoTerminadoLacteoController {

    private final GestionProductoTerminadoLacteoUseCase useCase;

    public ProductoTerminadoLacteoController(GestionProductoTerminadoLacteoUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoTerminadoLacteoResponse registrar(@Valid @RequestBody ProductoTerminadoLacteoRequest request) {
        return ProductoTerminadoLacteoRestMapper.toResponse(
                useCase.registrarProductoTerminado(
                        ProductoTerminadoLacteoRestMapper.toDomain(request)));
    }

    @GetMapping("/{id}")
    public ProductoTerminadoLacteoResponse obtenerPorId(@PathVariable Long id) {
        return ProductoTerminadoLacteoRestMapper.toResponse(
                useCase.obtenerPorId(id));
    }

    @GetMapping
    public List<ProductoTerminadoLacteoResponse> listarTodos() {
        return useCase.listarTodos()
                .stream()
                .map(ProductoTerminadoLacteoRestMapper::toResponse)
                .toList();
    }

    @GetMapping("/estado/{estado}")
    public List<ProductoTerminadoLacteoResponse> listarPorEstado(@PathVariable EstadoProductoTerminadoLacteo estado) {
        return useCase.listarPorEstado(estado)
                .stream()
                .map(ProductoTerminadoLacteoRestMapper::toResponse)
                .toList();
    }

    @GetMapping("/producto")
    public List<ProductoTerminadoLacteoResponse> listarPorProducto(@RequestParam String nombre) {
        return useCase.listarPorProducto(nombre)
                .stream()
                .map(ProductoTerminadoLacteoRestMapper::toResponse)
                .toList();
    }
}