package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.MedicionCalidadLactea;
import com.yerman.produccion_api.domain.port.in.GestionMedicionCalidadLacteaUseCase;
import com.yerman.produccion_api.domain.port.in.GestionProduccionLacteaUseCase;
import com.yerman.produccion_api.domain.port.out.EjecucionBatchRepositoryPort;
import com.yerman.produccion_api.domain.port.out.MedicionCalidadLacteaRepositoryPort;
import com.yerman.produccion_api.domain.port.out.OrdenProduccionRepositoryPort;
import com.yerman.produccion_api.domain.port.out.ProduccionLacteaBatchRepositoryPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GestionMedicionCalidadLacteaService implements GestionMedicionCalidadLacteaUseCase {

    private final MedicionCalidadLacteaRepositoryPort repository;
    private final GestionProduccionLacteaUseCase produccionLacteaUseCase;
    private final ProduccionLacteaBatchRepositoryPort batchRepositoryPort;
    private final OrdenProduccionRepositoryPort ordenRepositoryPort;
    private final EjecucionBatchRepositoryPort ejecucionBatchRepositoryPort;
    private final ValidacionOrdenProduccionGuardService validacionGuardService;

    public GestionMedicionCalidadLacteaService(
            MedicionCalidadLacteaRepositoryPort repository,
            GestionProduccionLacteaUseCase produccionLacteaUseCase,
            ProduccionLacteaBatchRepositoryPort batchRepositoryPort,
            OrdenProduccionRepositoryPort ordenRepositoryPort,
            EjecucionBatchRepositoryPort ejecucionBatchRepositoryPort,
            ValidacionOrdenProduccionGuardService validacionGuardService) {
        this.repository = repository;
        this.produccionLacteaUseCase = produccionLacteaUseCase;
        this.batchRepositoryPort = batchRepositoryPort;
        this.ordenRepositoryPort = ordenRepositoryPort;
        this.ejecucionBatchRepositoryPort = ejecucionBatchRepositoryPort;
        this.validacionGuardService = validacionGuardService;
    }

    @Override
    @Transactional
    public MedicionCalidadLactea registrar(MedicionCalidadLactea medicion) {
        validarMedicion(medicion);
        return repository.guardar(medicion);
    }

    @Override
    public MedicionCalidadLactea obtenerPorId(Long id) {
        return repository.obtenerPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro la medicion de calidad lactea con ID: " + id));
    }

    @Override
    public List<MedicionCalidadLactea> listarPorProduccion(Long idProduccionLactea) {
        if (idProduccionLactea == null) {
            throw new ReglaNegocioException("La produccion lactea es obligatoria.");
        }

        produccionLacteaUseCase.obtenerPorId(idProduccionLactea);
        return repository.listarPorProduccion(idProduccionLactea);
    }

    @Override
    public List<MedicionCalidadLactea> listarPorOrden(Long idOrdenProduccion) {
        if (idOrdenProduccion == null) {
            throw new ReglaNegocioException("La orden de produccion es obligatoria.");
        }

        ordenRepositoryPort.obtenerPorId(idOrdenProduccion)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una orden de produccion con ID: " + idOrdenProduccion));

        return repository.listarPorOrden(idOrdenProduccion);
    }

    private void validarMedicion(MedicionCalidadLactea medicion) {
        if (medicion == null) {
            throw new ReglaNegocioException("La medicion de calidad es obligatoria.");
        }

        boolean tieneProduccionLactea = medicion.getIdProduccionLactea() != null;
        boolean tieneOrdenProduccion = medicion.getIdOrdenProduccion() != null;

        if (!tieneProduccionLactea && !tieneOrdenProduccion) {
            throw new ReglaNegocioException("Debe asociar la medicion a una produccion lactea o a una orden de produccion.");
        }

        if (tieneProduccionLactea) {
            produccionLacteaUseCase.obtenerPorId(medicion.getIdProduccionLactea());
        }

        if (medicion.getIdProduccionLacteaBatch() != null
                && !batchRepositoryPort.existePorIdYProduccion(
                        medicion.getIdProduccionLacteaBatch(),
                        medicion.getIdProduccionLactea())) {
            throw new ReglaNegocioException("El batch no pertenece a la produccion lactea indicada.");
        }

        if (tieneOrdenProduccion) {
            ordenRepositoryPort.obtenerPorId(medicion.getIdOrdenProduccion())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe una orden de produccion con ID: " + medicion.getIdOrdenProduccion()));
            validacionGuardService.validarOrdenNoAprobada(medicion.getIdOrdenProduccion());
        }

        if (medicion.getIdEjecucionBatch() != null) {
            var batch = ejecucionBatchRepositoryPort.obtenerPorId(medicion.getIdEjecucionBatch())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe un batch de ejecucion con ID: " + medicion.getIdEjecucionBatch()));

            if (tieneOrdenProduccion && !medicion.getIdOrdenProduccion().equals(batch.getIdOrdenProduccion())) {
                throw new ReglaNegocioException("El batch no pertenece a la orden de produccion indicada.");
            }
        }

        if (medicion.getTipoMedicion() == null) {
            throw new ReglaNegocioException("El tipo de medicion es obligatorio.");
        }

        if (medicion.getReferencia() == null || medicion.getReferencia().isBlank()) {
            throw new ReglaNegocioException("La referencia de la medicion es obligatoria.");
        }

        if (medicion.getBrix() == null && medicion.getPh() == null) {
            throw new ReglaNegocioException("Debe registrar Brix, pH o ambos.");
        }

        validarValorNoNegativo(medicion.getBrix(), "Brix");
        validarValorNoNegativo(medicion.getPh(), "pH");

        if (medicion.getIdUsuarioCalidad() == null) {
            throw new ReglaNegocioException("El usuario de calidad es obligatorio.");
        }

        if (medicion.getFechaHoraMedicion() == null) {
            medicion.setFechaHoraMedicion(LocalDateTime.now());
        }
    }

    private void validarValorNoNegativo(BigDecimal valor, String nombreCampo) {
        if (valor != null && valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new ReglaNegocioException(nombreCampo + " no puede ser negativo.");
        }
    }
}
