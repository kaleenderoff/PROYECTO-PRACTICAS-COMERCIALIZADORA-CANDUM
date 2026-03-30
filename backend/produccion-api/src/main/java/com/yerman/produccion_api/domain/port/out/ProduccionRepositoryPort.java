package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.domain.model.Produccion;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProduccionRepositoryPort {

    Produccion guardar(Produccion produccion);

    Optional<Produccion> buscarPorId(Long id);

    Optional<Produccion> buscarPorNumeroLote(String numeroLote);

    boolean existePorNumeroLote(String numeroLote);

    List<Produccion> listarTodas();

    List<Produccion> listarPorFecha(LocalDate fechaProduccion);

    List<Produccion> listarPorEstado(String estado);

    List<Produccion> listarPorLineaProduccion(Long idLineaProduccion);

    List<Produccion> listarPorOperario(Long idOperario);

    List<Produccion> listarPorJefeLinea(Long idJefeLinea);
}