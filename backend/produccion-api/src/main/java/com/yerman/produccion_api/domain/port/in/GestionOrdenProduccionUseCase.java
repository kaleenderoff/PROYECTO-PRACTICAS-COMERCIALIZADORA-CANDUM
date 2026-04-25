package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.domain.model.OrdenProduccion;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GestionOrdenProduccionUseCase {

    OrdenProduccion crearDesdeProgramacion(Long idProgramacion, Long idCreadaPor, String observaciones);

    Optional<OrdenProduccion> obtenerPorId(Long id);

    List<OrdenProduccion> listarTodas();

    List<OrdenProduccion> listarPorFecha(LocalDate fechaProduccion);

    OrdenProduccion iniciar(Long idOrden, Long idJefeLineaEjecutor);

    OrdenProduccion finalizar(Long idOrden);

    OrdenProduccion cancelar(Long idOrden, String observaciones);
}