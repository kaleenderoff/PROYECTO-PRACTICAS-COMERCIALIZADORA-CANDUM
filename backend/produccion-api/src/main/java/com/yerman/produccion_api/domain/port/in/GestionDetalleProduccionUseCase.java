package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.domain.model.DetalleProduccion;

import java.util.List;
import java.util.Optional;

public interface GestionDetalleProduccionUseCase {

    DetalleProduccion crearDetalleProduccion(DetalleProduccion detalleProduccion);

    Optional<DetalleProduccion> obtenerPorId(Long id);

    List<DetalleProduccion> listarTodos();

    List<DetalleProduccion> listarPorProduccion(Long idProduccion);

    List<DetalleProduccion> listarPorProducto(Long idProducto);

    Optional<DetalleProduccion> obtenerPorProduccionProductoBatch(Long idProduccion, Long idProducto, Integer numBatch);
}
