package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.domain.model.Produccion;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProduccionLacteaRepositoryPort {

    Produccion guardar(Produccion produccion);

    Optional<Produccion> obtenerPorId(Long id);

    List<Produccion> listarTodas();

    List<Produccion> listarPorFecha(LocalDate fechaProduccion);
}