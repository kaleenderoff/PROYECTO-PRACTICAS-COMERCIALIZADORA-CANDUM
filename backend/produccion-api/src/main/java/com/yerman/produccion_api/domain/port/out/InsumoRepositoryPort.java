package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.domain.model.Insumo;

import java.util.List;
import java.util.Optional;

public interface InsumoRepositoryPort {

    Insumo guardar(Insumo insumo);

    Optional<Insumo> buscarPorId(Long id);

    Optional<Insumo> buscarPorNombre(String nombre);

    boolean existePorNombre(String nombre);

    List<Insumo> listarTodos();

    List<Insumo> listarActivos();
}
