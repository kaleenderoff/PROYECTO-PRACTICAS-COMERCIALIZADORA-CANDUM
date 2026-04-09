package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.domain.model.ProductoTerminado;

import java.util.List;
import java.util.Optional;

public interface GestionProductoTerminadoUseCase {

    ProductoTerminado crearProductoTerminado(ProductoTerminado productoTerminado);

    ProductoTerminado actualizarProductoTerminado(Long id, ProductoTerminado productoTerminado);

    Optional<ProductoTerminado> obtenerPorId(Long id);

    Optional<ProductoTerminado> obtenerPorSku(String sku);

    List<ProductoTerminado> listarActivos();

    List<ProductoTerminado> listarTodos();

    void cambiarEstado(Long id, boolean activo);
}