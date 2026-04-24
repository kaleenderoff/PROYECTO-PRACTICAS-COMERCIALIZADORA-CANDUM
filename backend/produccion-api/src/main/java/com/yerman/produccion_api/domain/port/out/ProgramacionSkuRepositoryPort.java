package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.domain.model.ProgramacionSku;

import java.util.List;
import java.util.Optional;

public interface ProgramacionSkuRepositoryPort {

    ProgramacionSku guardar(ProgramacionSku programacionSku);

    Optional<ProgramacionSku> obtenerPorId(Long id);

    List<ProgramacionSku> listarPorProgramacion(Long idProgramacion);

    boolean existePorProgramacionYSku(Long idProgramacion, Long idSku);
}