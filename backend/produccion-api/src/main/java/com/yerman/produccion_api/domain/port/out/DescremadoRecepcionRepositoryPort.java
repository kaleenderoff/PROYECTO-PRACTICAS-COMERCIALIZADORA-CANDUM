package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.domain.model.DescremadoRecepcion;

import java.util.List;
import java.util.Optional;

public interface DescremadoRecepcionRepositoryPort {

    DescremadoRecepcion guardar(DescremadoRecepcion descremadoRecepcion);

    Optional<DescremadoRecepcion> obtenerPorId(Long id);

    List<DescremadoRecepcion> listarTodos();

    List<DescremadoRecepcion> listarPorRecepcion(Long idRecepcionLeche);
}