package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.RegistroInsumoLacteo;
import com.yerman.produccion_api.domain.port.in.GestionProduccionLacteaUseCase;
import com.yerman.produccion_api.domain.port.in.GestionRegistroInsumoLacteoUseCase;
import com.yerman.produccion_api.domain.port.out.InsumoRepositoryPort;
import com.yerman.produccion_api.domain.port.out.ProduccionLacteaBatchRepositoryPort;
import com.yerman.produccion_api.domain.port.out.RegistroInsumoLacteoRepositoryPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GestionRegistroInsumoLacteoService implements GestionRegistroInsumoLacteoUseCase {

    private final RegistroInsumoLacteoRepositoryPort repository;
    private final GestionProduccionLacteaUseCase produccionLacteaUseCase;
    private final ProduccionLacteaBatchRepositoryPort batchRepositoryPort;
    private final InsumoRepositoryPort insumoRepositoryPort;

    public GestionRegistroInsumoLacteoService(
            RegistroInsumoLacteoRepositoryPort repository,
            GestionProduccionLacteaUseCase produccionLacteaUseCase,
            ProduccionLacteaBatchRepositoryPort batchRepositoryPort,
            InsumoRepositoryPort insumoRepositoryPort) {
        this.repository = repository;
        this.produccionLacteaUseCase = produccionLacteaUseCase;
        this.batchRepositoryPort = batchRepositoryPort;
        this.insumoRepositoryPort = insumoRepositoryPort;
    }

    @Override
    @Transactional
    public RegistroInsumoLacteo registrar(RegistroInsumoLacteo registro) {
        validarRegistro(registro);
        return repository.guardar(registro);
    }

    @Override
    public RegistroInsumoLacteo obtenerPorId(Long id) {
        return repository.obtenerPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro el registro de insumo lacteo con ID: " + id));
    }

    @Override
    public List<RegistroInsumoLacteo> listarPorProduccion(Long idProduccionLactea) {
        produccionLacteaUseCase.obtenerPorId(idProduccionLactea);
        return repository.listarPorProduccion(idProduccionLactea);
    }

    @Override
    public List<RegistroInsumoLacteo> listarPorBatch(Long idProduccionLacteaBatch) {
        if (!batchRepositoryPort.existePorId(idProduccionLacteaBatch)) {
            throw new RecursoNoEncontradoException("Batch de produccion lactea no encontrado");
        }

        return repository.listarPorBatch(idProduccionLacteaBatch);
    }

    private void validarRegistro(RegistroInsumoLacteo registro) {
        if (registro == null) {
            throw new ReglaNegocioException("El registro de insumo es obligatorio.");
        }

        if (registro.getIdProduccionLactea() == null) {
            throw new ReglaNegocioException("La produccion lactea es obligatoria.");
        }

        produccionLacteaUseCase.obtenerPorId(registro.getIdProduccionLactea());

        if (registro.getIdProduccionLacteaBatch() != null
                && !batchRepositoryPort.existePorIdYProduccion(
                        registro.getIdProduccionLacteaBatch(),
                        registro.getIdProduccionLactea())) {
            throw new ReglaNegocioException("El batch no pertenece a la produccion lactea indicada.");
        }

        if (!insumoRepositoryPort.existePorId(registro.getIdInsumo())) {
            throw new RecursoNoEncontradoException("Insumo no encontrado");
        }

        if (registro.getCantidadUsada() == null
                || registro.getCantidadUsada().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("La cantidad usada debe ser mayor que cero.");
        }

        if (registro.getCantidadRequerida() != null
                && registro.getCantidadRequerida().compareTo(BigDecimal.ZERO) < 0) {
            throw new ReglaNegocioException("La cantidad requerida no puede ser negativa.");
        }

        if (registro.getUnidadMedida() == null || registro.getUnidadMedida().isBlank()) {
            throw new ReglaNegocioException("La unidad de medida es obligatoria.");
        }

        if (registro.getIdUsuario() == null) {
            throw new ReglaNegocioException("El usuario que registra es obligatorio.");
        }

        if (registro.getFechaHoraRegistro() == null) {
            registro.setFechaHoraRegistro(LocalDateTime.now());
        }
    }
}
