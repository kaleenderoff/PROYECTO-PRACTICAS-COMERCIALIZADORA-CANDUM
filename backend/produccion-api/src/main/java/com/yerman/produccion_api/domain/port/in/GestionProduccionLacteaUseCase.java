package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.domain.model.Produccion;

import java.time.LocalDate;
import java.util.List;

public interface GestionProduccionLacteaUseCase {

    Produccion registrarProduccion(Produccion produccion);

    Produccion obtenerPorId(Long id);

    List<Produccion> listarTodas();

    List<Produccion> listarPorFecha(LocalDate fechaProduccion);
}