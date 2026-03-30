package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.domain.model.Producto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductoRepositoryPort {

    Producto guardar(Producto producto);

    Optional<Producto> buscarPorId(Long id);

    Optional<Producto> buscarPorNombreGramajeMarca(String nombre, BigDecimal gramajeG, String marca);

    boolean existePorNombreGramajeMarca(String nombre, BigDecimal gramajeG, String marca);

    List<Producto> listarTodos();

    List<Producto> listarActivos();

    List<Producto> listarPorLineaProduccion(Long idLineaProduccion);

    List<Producto> listarActivosPorLineaProduccion(Long idLineaProduccion);
}