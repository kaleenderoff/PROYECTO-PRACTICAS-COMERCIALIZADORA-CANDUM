package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.domain.model.EstadoValidacion;
import com.yerman.produccion_api.domain.model.Validacion;

import java.util.List;
import java.util.Optional;

public interface ValidacionRepositoryPort {

    Validacion guardar(Validacion validacion);

    Optional<Validacion> buscarPorId(Long id);

    Optional<Validacion> buscarPorDetalleProduccion(Long idDetalleProduccion);

    boolean existePorDetalleProduccion(Long idDetalleProduccion);

    List<Validacion> listarTodas();

    List<Validacion> listarPorEstado(EstadoValidacion estado);

    List<Validacion> listarPorValidador(Long idValidador);
}