package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.domain.model.RegistroInsumoLacteo;

import java.util.List;
import java.util.Optional;

public interface RegistroInsumoLacteoRepositoryPort {

    RegistroInsumoLacteo guardar(RegistroInsumoLacteo registro);

    Optional<RegistroInsumoLacteo> obtenerPorId(Long id);

    List<RegistroInsumoLacteo> listarPorProduccion(Long idProduccionLactea);

    List<RegistroInsumoLacteo> listarPorBatch(Long idProduccionLacteaBatch);
}
