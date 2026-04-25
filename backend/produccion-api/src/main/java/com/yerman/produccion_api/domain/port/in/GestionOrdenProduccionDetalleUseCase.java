package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.domain.model.OrdenProduccionDetalle;

import java.util.List;
import java.util.Optional;

public interface GestionOrdenProduccionDetalleUseCase {

    OrdenProduccionDetalle agregarDetalle(OrdenProduccionDetalle detalle);

    Optional<OrdenProduccionDetalle> obtenerPorId(Long id);

    List<OrdenProduccionDetalle> listarPorOrden(Long idOrden);

    void eliminar(Long id);
}