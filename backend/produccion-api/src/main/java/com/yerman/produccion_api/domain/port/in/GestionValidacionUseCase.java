package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.domain.model.EstadoValidacion;
import com.yerman.produccion_api.domain.model.Validacion;

import java.util.List;
import java.util.Optional;

public interface GestionValidacionUseCase {

    Validacion crearValidacion(Validacion validacion);

    Optional<Validacion> obtenerPorId(Long id);

    Optional<Validacion> obtenerPorDetalleProduccion(Long idDetalleProduccion);

    List<Validacion> listarTodas();

    List<Validacion> listarPorEstado(EstadoValidacion estado);

    List<Validacion> listarPorValidador(Long idValidador);
}
