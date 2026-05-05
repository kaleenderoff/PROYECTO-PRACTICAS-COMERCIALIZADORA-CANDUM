package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.domain.model.EmpaqueLacteo;

import java.util.List;
import java.util.Optional;

public interface EmpaqueLacteoRepositoryPort {

    EmpaqueLacteo guardar(EmpaqueLacteo empaqueLacteo);

    Optional<EmpaqueLacteo> buscarPorId(Long id);

    List<EmpaqueLacteo> listarTodos();

    List<EmpaqueLacteo> listarPorProductoTerminadoLacteo(Long productoTerminadoLacteoId);

    List<EmpaqueLacteo> listarPorLoteEmpaque(String loteEmpaque);

    boolean existePorLoteEmpaque(String loteEmpaque);
}