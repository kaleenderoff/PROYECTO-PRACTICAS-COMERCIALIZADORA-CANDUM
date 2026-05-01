package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.domain.model.DescremadoRecepcion;

import java.util.List;

public interface GestionDescremadoRecepcionUseCase {

    DescremadoRecepcion registrarDescremado(DescremadoRecepcion descremadoRecepcion);

    DescremadoRecepcion obtenerPorId(Long id);

    List<DescremadoRecepcion> listarTodos();

    List<DescremadoRecepcion> listarPorRecepcion(Long idRecepcionLeche);
}