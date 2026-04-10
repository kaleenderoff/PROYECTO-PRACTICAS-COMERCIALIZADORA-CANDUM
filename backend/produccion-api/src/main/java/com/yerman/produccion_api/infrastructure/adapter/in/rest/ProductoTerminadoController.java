package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.ProductoTerminadoRequest;
import com.yerman.produccion_api.application.dto.response.ProductoTerminadoResponse;
import com.yerman.produccion_api.application.mapper.ProductoTerminadoMapper;
import com.yerman.produccion_api.domain.model.ProductoTerminado;
import com.yerman.produccion_api.domain.port.in.GestionProductoTerminadoUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos-terminados")
public class ProductoTerminadoController {

    private final GestionProductoTerminadoUseCase gestionProductoTerminadoUseCase;

    public ProductoTerminadoController(GestionProductoTerminadoUseCase gestionProductoTerminadoUseCase) {
        this.gestionProductoTerminadoUseCase = gestionProductoTerminadoUseCase;
    }

    // ===============================
    // CREAR
    // ===============================
    @PreAuthorize("hasAnyRole('ADMIN','INGENIERO')")
    @PostMapping
    public ResponseEntity<ProductoTerminadoResponse> crear(@RequestBody ProductoTerminadoRequest request) {
        ProductoTerminado productoTerminado = ProductoTerminadoMapper.toDomain(request);
        ProductoTerminado creado = gestionProductoTerminadoUseCase.crearProductoTerminado(productoTerminado);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductoTerminadoMapper.toResponse(creado));
    }

    // ===============================
    // ACTUALIZAR
    // ===============================
    @PreAuthorize("hasAnyRole('ADMIN','INGENIERO')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductoTerminadoResponse> actualizar(
            @PathVariable Long id,
            @RequestBody ProductoTerminadoRequest request) {

        ProductoTerminado productoTerminado = ProductoTerminadoMapper.toDomain(request);
        ProductoTerminado actualizado = gestionProductoTerminadoUseCase
                .actualizarProductoTerminado(id, productoTerminado);

        return ResponseEntity.ok(ProductoTerminadoMapper.toResponse(actualizado));
    }

    // ===============================
    // OBTENER POR ID
    // ===============================
    @PreAuthorize("hasAnyRole('ADMIN','INGENIERO','JEFE_PLANTA','JEFE_LINEA')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductoTerminadoResponse> obtenerPorId(@PathVariable Long id) {
        return gestionProductoTerminadoUseCase.obtenerPorId(id)
                .map(ProductoTerminadoMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ===============================
    // OBTENER POR SKU
    // ===============================
    @PreAuthorize("hasAnyRole('ADMIN','INGENIERO','JEFE_PLANTA','JEFE_LINEA')")
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductoTerminadoResponse> obtenerPorSku(@PathVariable String sku) {
        return gestionProductoTerminadoUseCase.obtenerPorSku(sku)
                .map(ProductoTerminadoMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ===============================
    // LISTAR TODOS
    // ===============================
    @PreAuthorize("hasAnyRole('ADMIN','INGENIERO','JEFE_PLANTA','JEFE_LINEA')")
    @GetMapping
    public ResponseEntity<List<ProductoTerminadoResponse>> listarTodos() {

        List<ProductoTerminadoResponse> response = gestionProductoTerminadoUseCase
                .listarTodos()
                .stream()
                .map(ProductoTerminadoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    // ===============================
    // LISTAR ACTIVOS
    // ===============================
    @PreAuthorize("hasAnyRole('ADMIN','INGENIERO','JEFE_PLANTA','JEFE_LINEA')")
    @GetMapping("/activos")
    public ResponseEntity<List<ProductoTerminadoResponse>> listarActivos() {

        List<ProductoTerminadoResponse> response = gestionProductoTerminadoUseCase
                .listarActivos()
                .stream()
                .map(ProductoTerminadoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    // ===============================
    // CAMBIAR ESTADO (ACTIVO / INACTIVO)
    // ===============================
    @PreAuthorize("hasAnyRole('ADMIN','INGENIERO')")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(
            @PathVariable Long id,
            @RequestParam boolean activo) {

        gestionProductoTerminadoUseCase.cambiarEstado(id, activo);
        return ResponseEntity.noContent().build();
    }
}