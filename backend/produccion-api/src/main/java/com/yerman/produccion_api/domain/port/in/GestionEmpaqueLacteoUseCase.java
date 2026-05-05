package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.domain.model.EmpaqueLacteo;

import java.util.List;
import java.util.Optional;

public interface GestionEmpaqueLacteoUseCase {

    EmpaqueLacteo crear(EmpaqueLacteo empaqueLacteo);

    Optional<EmpaqueLacteo> obtenerPorId(Long id);

    List<EmpaqueLacteo> listarTodos();

    List<EmpaqueLacteo> listarPorProductoTerminadoLacteo(Long productoTerminadoLacteoId);

    List<EmpaqueLacteo> listarPorLoteEmpaque(String loteEmpaque);

    EmpaqueLacteo anular(Long id);
}