package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.domain.model.OrdenProduccionDetalle;

import java.util.List;
import java.util.Optional;

public interface OrdenProduccionDetalleRepositoryPort {

    OrdenProduccionDetalle guardar(OrdenProduccionDetalle detalle);

    Optional<OrdenProduccionDetalle> obtenerPorId(Long id);

    List<OrdenProduccionDetalle> listarPorOrden(Long idOrden);

    boolean existePorOrdenYSku(Long idOrden, Long idSku);

    void eliminarPorId(Long id);
}