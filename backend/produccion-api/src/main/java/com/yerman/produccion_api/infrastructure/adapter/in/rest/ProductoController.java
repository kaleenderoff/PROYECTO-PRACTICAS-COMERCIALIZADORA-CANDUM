package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.ProductoRequest;
import com.yerman.produccion_api.application.dto.response.ProductoResponse;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.mapper.ProductoMapper;
import com.yerman.produccion_api.domain.model.Producto;
import com.yerman.produccion_api.domain.port.in.GestionProductoUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final GestionProductoUseCase gestionProductoUseCase;

    public ProductoController(GestionProductoUseCase gestionProductoUseCase) {
        this.gestionProductoUseCase = gestionProductoUseCase;
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crearProducto(
            @Valid @RequestBody ProductoRequest request) {

        Producto producto = ProductoMapper.toDomain(request);
        Producto creado = gestionProductoUseCase.crearProducto(producto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ProductoMapper.toResponse(creado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable Long id) {
        Producto producto = gestionProductoUseCase.obtenerPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Producto no encontrado con id: " + id));

        return ResponseEntity.ok(ProductoMapper.toResponse(producto));
    }

    @GetMapping("/buscar")
    public ResponseEntity<ProductoResponse> obtenerPorNombreGramajeMarca(
            @RequestParam String nombre,
            @RequestParam BigDecimal gramajeG,
            @RequestParam(required = false) String marca) {

        Producto producto = gestionProductoUseCase.obtenerPorNombreGramajeMarca(nombre, gramajeG, marca)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Producto no encontrado con los criterios suministrados"));

        return ResponseEntity.ok(ProductoMapper.toResponse(producto));
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listarTodos() {
        List<ProductoResponse> response = gestionProductoUseCase.listarTodos()
                .stream()
                .map(ProductoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ProductoResponse>> listarActivos() {
        List<ProductoResponse> response = gestionProductoUseCase.listarActivos()
                .stream()
                .map(ProductoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/linea/{idLineaProduccion}")
    public ResponseEntity<List<ProductoResponse>> listarPorLineaProduccion(
            @PathVariable Long idLineaProduccion) {

        List<ProductoResponse> response = gestionProductoUseCase.listarPorLineaProduccion(idLineaProduccion)
                .stream()
                .map(ProductoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/linea/{idLineaProduccion}/activos")
    public ResponseEntity<List<ProductoResponse>> listarActivosPorLineaProduccion(
            @PathVariable Long idLineaProduccion) {

        List<ProductoResponse> response = gestionProductoUseCase
                .listarActivosPorLineaProduccion(idLineaProduccion)
                .stream()
                .map(ProductoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }
}