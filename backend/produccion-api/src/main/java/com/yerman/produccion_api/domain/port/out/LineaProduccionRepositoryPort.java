package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.domain.model.LineaProduccion;

import java.util.List;
import java.util.Optional;

public interface LineaProduccionRepositoryPort {

    LineaProduccion guardar(LineaProduccion lineaProduccion);

    Optional<LineaProduccion> buscarPorId(Long id);

    Optional<LineaProduccion> buscarPorNombre(String nombre);

    boolean existePorNombre(String nombre);

    List<LineaProduccion> listarTodas();

    List<LineaProduccion> listarActivas();
}
