package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.domain.model.DetalleProduccion;

import java.util.List;
import java.util.Optional;

public interface DetalleProduccionRepositoryPort {

    DetalleProduccion guardar(DetalleProduccion detalleProduccion);

    Optional<DetalleProduccion> buscarPorId(Long id);

    List<DetalleProduccion> listarTodos();

    List<DetalleProduccion> listarPorProduccion(Long idProduccion);

    List<DetalleProduccion> listarPorProducto(Long idProducto);

    Optional<DetalleProduccion> buscarPorProduccionProductoBatch(Long idProduccion, Long idProducto, Integer numBatch);

    boolean existePorProduccionProductoBatch(Long idProduccion, Long idProducto, Integer numBatch);
}