package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.domain.model.LineaProduccion;

import java.util.List;
import java.util.Optional;

public interface GestionLineaProduccionUseCase {

    LineaProduccion crearLineaProduccion(LineaProduccion lineaProduccion);

    Optional<LineaProduccion> obtenerPorId(Long id);

    Optional<LineaProduccion> obtenerPorNombre(String nombre);

    List<LineaProduccion> listarTodas();

    List<LineaProduccion> listarActivas();
}
