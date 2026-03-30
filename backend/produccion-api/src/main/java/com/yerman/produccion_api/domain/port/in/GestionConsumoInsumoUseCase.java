package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.domain.model.ConsumoInsumo;

import java.util.List;
import java.util.Optional;

public interface GestionConsumoInsumoUseCase {

    ConsumoInsumo crearConsumoInsumo(ConsumoInsumo consumoInsumo);

    Optional<ConsumoInsumo> obtenerPorId(Long id);

    List<ConsumoInsumo> listarTodos();

    List<ConsumoInsumo> listarPorProduccion(Long idProduccion);

    List<ConsumoInsumo> listarPorDetalleProduccion(Long idDetalleProduccion);

    List<ConsumoInsumo> listarPorInsumo(Long idInsumo);

    Optional<ConsumoInsumo> obtenerPorProduccionInsumoDetalle(Long idProduccion, Long idInsumo,
            Long idDetalleProduccion);
}
