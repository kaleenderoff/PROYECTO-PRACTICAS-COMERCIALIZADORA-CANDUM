package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.domain.model.ProgramacionProduccion;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProgramacionProduccionRepositoryPort {

    ProgramacionProduccion guardar(ProgramacionProduccion programacion);

    Optional<ProgramacionProduccion> obtenerPorId(Long id);

    List<ProgramacionProduccion> listarPorFecha(LocalDate fecha);

    List<ProgramacionProduccion> listarTodas();
}