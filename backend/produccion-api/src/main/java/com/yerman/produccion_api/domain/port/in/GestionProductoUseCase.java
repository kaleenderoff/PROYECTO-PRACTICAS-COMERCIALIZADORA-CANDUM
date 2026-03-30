package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.domain.model.Producto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface GestionProductoUseCase {

    Producto crearProducto(Producto producto);

    Optional<Producto> obtenerPorId(Long id);

    Optional<Producto> obtenerPorNombreGramajeMarca(String nombre, BigDecimal gramajeG, String marca);

    List<Producto> listarTodos();

    List<Producto> listarActivos();

    List<Producto> listarPorLineaProduccion(Long idLineaProduccion);

    List<Producto> listarActivosPorLineaProduccion(Long idLineaProduccion);
}
