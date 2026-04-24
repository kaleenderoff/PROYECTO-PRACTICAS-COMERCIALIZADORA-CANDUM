package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.domain.model.ProgramacionSku;

import java.util.List;
import java.util.Optional;

public interface GestionProgramacionSkuUseCase {

    ProgramacionSku agregarSku(ProgramacionSku programacionSku);

    Optional<ProgramacionSku> obtenerPorId(Long id);

    List<ProgramacionSku> listarPorProgramacion(Long idProgramacion);
}