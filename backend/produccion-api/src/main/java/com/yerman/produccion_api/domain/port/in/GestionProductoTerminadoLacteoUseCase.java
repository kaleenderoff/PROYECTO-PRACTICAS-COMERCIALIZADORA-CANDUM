package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.domain.model.EstadoProductoTerminadoLacteo;
import com.yerman.produccion_api.domain.model.ProductoTerminadoLacteo;

import java.util.List;

public interface GestionProductoTerminadoLacteoUseCase {

    ProductoTerminadoLacteo registrarProductoTerminado(ProductoTerminadoLacteo productoTerminado);

    ProductoTerminadoLacteo obtenerPorId(Long id);

    List<ProductoTerminadoLacteo> listarTodos();

    List<ProductoTerminadoLacteo> listarPorEstado(EstadoProductoTerminadoLacteo estado);

    List<ProductoTerminadoLacteo> listarPorProducto(String producto);
}