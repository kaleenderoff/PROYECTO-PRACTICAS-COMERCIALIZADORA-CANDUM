package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.domain.model.Produccion;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GestionProduccionUseCase {

    Produccion crearProduccion(Produccion produccion);

    Optional<Produccion> obtenerPorId(Long id);

    Optional<Produccion> obtenerPorNumeroLote(String numeroLote);

    List<Produccion> listarTodas();

    List<Produccion> listarPorFecha(LocalDate fechaProduccion);

    List<Produccion> listarPorEstado(String estado);

    List<Produccion> listarPorLineaProduccion(Long idLineaProduccion);

    List<Produccion> listarPorOperario(Long idOperario);

    List<Produccion> listarPorJefeLinea(Long idJefeLinea);
}
