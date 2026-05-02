package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.domain.model.EstadoProductoTerminadoLacteo;
import com.yerman.produccion_api.domain.model.ProductoTerminadoLacteo;

import java.util.List;
import java.util.Optional;

public interface ProductoTerminadoLacteoRepositoryPort {

    ProductoTerminadoLacteo guardar(ProductoTerminadoLacteo productoTerminado);

    Optional<ProductoTerminadoLacteo> obtenerPorId(Long id);

    List<ProductoTerminadoLacteo> listarTodos();

    List<ProductoTerminadoLacteo> listarPorEstado(EstadoProductoTerminadoLacteo estado);

    List<ProductoTerminadoLacteo> listarPorProducto(String producto);
}