package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.domain.model.Empaque;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GestionEmpaqueUseCase {

    Empaque registrarEmpaque(Empaque empaque);

    Empaque actualizarEmpaque(Long id, Empaque empaque);

    Optional<Empaque> obtenerPorId(Long id);

    List<Empaque> listarTodos();

    List<Empaque> listarPorDetalleProduccion(Long idDetalleProduccion);

    List<Empaque> listarPorProductoTerminado(Long idProductoTerminado);

    List<Empaque> listarPorLoteEmpaque(String loteEmpaque);

    List<Empaque> listarPorRangoFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
