package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.domain.model.OrdenProduccion;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrdenProduccionRepositoryPort {

    OrdenProduccion guardar(OrdenProduccion ordenProduccion);

    Optional<OrdenProduccion> obtenerPorId(Long id);

    List<OrdenProduccion> listarTodas();

    List<OrdenProduccion> listarPorFecha(LocalDate fechaProduccion);

    boolean existePorProgramacion(Long idProgramacion);

    Optional<OrdenProduccion> obtenerPorNumeroOrden(String numeroOrden);
}