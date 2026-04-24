package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.domain.model.ProgramacionProduccion;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GestionProgramacionProduccionUseCase {

    ProgramacionProduccion crear(ProgramacionProduccion programacion);

    Optional<ProgramacionProduccion> obtenerPorId(Long id);

    List<ProgramacionProduccion> listarPorFecha(LocalDate fecha);

    List<ProgramacionProduccion> listarTodas();

    ProgramacionProduccion cambiarEstado(Long id, String estado);
}