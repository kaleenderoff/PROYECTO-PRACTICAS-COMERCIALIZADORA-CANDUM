package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.domain.model.Insumo;

import java.util.List;
import java.util.Optional;

public interface GestionInsumoUseCase {

    Insumo crearInsumo(Insumo insumo);

    Optional<Insumo> obtenerPorId(Long id);

    Optional<Insumo> obtenerPorNombre(String nombre);

    List<Insumo> listarTodos();

    List<Insumo> listarActivos();
}
