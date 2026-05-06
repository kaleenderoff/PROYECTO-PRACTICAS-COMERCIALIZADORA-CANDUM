package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.domain.model.RegistroInsumoLacteo;

import java.util.List;

public interface GestionRegistroInsumoLacteoUseCase {

    RegistroInsumoLacteo registrar(RegistroInsumoLacteo registro);

    RegistroInsumoLacteo obtenerPorId(Long id);

    List<RegistroInsumoLacteo> listarPorProduccion(Long idProduccionLactea);

    List<RegistroInsumoLacteo> listarPorBatch(Long idProduccionLacteaBatch);
}
