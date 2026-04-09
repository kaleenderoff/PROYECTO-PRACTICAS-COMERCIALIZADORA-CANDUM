package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.domain.model.ProductoTerminado;

import java.util.List;
import java.util.Optional;

public interface ProductoTerminadoRepositoryPort {

    ProductoTerminado guardar(ProductoTerminado productoTerminado);

    Optional<ProductoTerminado> obtenerPorId(Long id);

    Optional<ProductoTerminado> obtenerPorSku(String sku);

    List<ProductoTerminado> listarActivos();

    List<ProductoTerminado> listarTodos();

    boolean existePorSku(String sku);
}
