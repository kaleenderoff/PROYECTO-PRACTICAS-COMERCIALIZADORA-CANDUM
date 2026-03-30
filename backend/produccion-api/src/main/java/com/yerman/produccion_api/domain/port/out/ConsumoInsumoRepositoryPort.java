package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.domain.model.ConsumoInsumo;

import java.util.List;
import java.util.Optional;

public interface ConsumoInsumoRepositoryPort {

    ConsumoInsumo guardar(ConsumoInsumo consumoInsumo);

    Optional<ConsumoInsumo> buscarPorId(Long id);

    List<ConsumoInsumo> listarTodos();

    List<ConsumoInsumo> listarPorProduccion(Long idProduccion);

    List<ConsumoInsumo> listarPorDetalleProduccion(Long idDetalleProduccion);

    List<ConsumoInsumo> listarPorInsumo(Long idInsumo);

    Optional<ConsumoInsumo> buscarPorProduccionInsumoDetalle(Long idProduccion, Long idInsumo,
            Long idDetalleProduccion);

    boolean existePorProduccionInsumoDetalle(Long idProduccion, Long idInsumo, Long idDetalleProduccion);
}