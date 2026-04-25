package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoDuplicadoException;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.EstadoOrdenProduccion;
import com.yerman.produccion_api.domain.model.OrdenProduccion;
import com.yerman.produccion_api.domain.model.OrdenProduccionDetalle;
import com.yerman.produccion_api.domain.port.in.GestionOrdenProduccionDetalleUseCase;
import com.yerman.produccion_api.domain.port.out.OrdenProduccionDetalleRepositoryPort;
import com.yerman.produccion_api.domain.port.out.OrdenProduccionRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class GestionOrdenProduccionDetalleService implements GestionOrdenProduccionDetalleUseCase {

    private final OrdenProduccionDetalleRepositoryPort detalleRepository;
    private final OrdenProduccionRepositoryPort ordenRepository;

    public GestionOrdenProduccionDetalleService(
            OrdenProduccionDetalleRepositoryPort detalleRepository,
            OrdenProduccionRepositoryPort ordenRepository) {
        this.detalleRepository = detalleRepository;
        this.ordenRepository = ordenRepository;
    }

    @Override
    public OrdenProduccionDetalle agregarDetalle(OrdenProduccionDetalle detalle) {
        OrdenProduccion orden = ordenRepository.obtenerPorId(detalle.getIdOrden())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una orden de producción con ID: " + detalle.getIdOrden()));

        if (orden.getEstado() != EstadoOrdenProduccion.PROGRAMADA) {
            throw new ReglaNegocioException("Solo se pueden agregar detalles a una orden en estado PROGRAMADA.");
        }

        if (detalleRepository.existePorOrdenYSku(detalle.getIdOrden(), detalle.getIdSku())) {
            throw new RecursoDuplicadoException(
                    "Ya existe un detalle para el SKU " + detalle.getIdSku()
                            + " en la orden " + detalle.getIdOrden());
        }

        if (detalle.getCantidadProgramada() == null
                || detalle.getCantidadProgramada().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("La cantidad programada debe ser mayor que cero.");
        }

        if (detalle.getUnidadProgramada() == null || detalle.getUnidadProgramada().isBlank()) {
            detalle.setUnidadProgramada("UNIDADES");
        }

        if (detalle.getPrioridad() == null || detalle.getPrioridad() <= 0) {
            detalle.setPrioridad(1);
        }

        return detalleRepository.guardar(detalle);
    }

    @Override
    public Optional<OrdenProduccionDetalle> obtenerPorId(Long id) {
        return detalleRepository.obtenerPorId(id);
    }

    @Override
    public List<OrdenProduccionDetalle> listarPorOrden(Long idOrden) {
        return detalleRepository.listarPorOrden(idOrden);
    }

    @Override
    public void eliminar(Long id) {
        OrdenProduccionDetalle detalle = detalleRepository.obtenerPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un detalle de orden de producción con ID: " + id));

        OrdenProduccion orden = ordenRepository.obtenerPorId(detalle.getIdOrden())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una orden de producción con ID: " + detalle.getIdOrden()));

        if (orden.getEstado() != EstadoOrdenProduccion.PROGRAMADA) {
            throw new ReglaNegocioException("Solo se pueden eliminar detalles de una orden en estado PROGRAMADA.");
        }

        detalleRepository.eliminarPorId(id);
    }
}