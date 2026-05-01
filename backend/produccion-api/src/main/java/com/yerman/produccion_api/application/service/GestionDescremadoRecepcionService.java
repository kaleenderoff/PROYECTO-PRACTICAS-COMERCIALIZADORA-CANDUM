package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.DescremadoRecepcion;
import com.yerman.produccion_api.domain.port.in.GestionDescremadoRecepcionUseCase;
import com.yerman.produccion_api.domain.port.in.GestionRecepcionLecheUseCase;
import com.yerman.produccion_api.domain.port.out.DescremadoRecepcionRepositoryPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GestionDescremadoRecepcionService implements GestionDescremadoRecepcionUseCase {

    private final DescremadoRecepcionRepositoryPort repository;
    private final GestionRecepcionLecheUseCase recepcionLecheUseCase;

    public GestionDescremadoRecepcionService(
            DescremadoRecepcionRepositoryPort repository,
            GestionRecepcionLecheUseCase recepcionLecheUseCase) {
        this.repository = repository;
        this.recepcionLecheUseCase = recepcionLecheUseCase;
    }

    @Override
    @Transactional
    public DescremadoRecepcion registrarDescremado(DescremadoRecepcion descremadoRecepcion) {
        validarDescremado(descremadoRecepcion);

        // Valida que la recepción exista
        recepcionLecheUseCase.obtenerPorId(descremadoRecepcion.getIdRecepcionLeche());

        return repository.guardar(descremadoRecepcion);
    }

    @Override
    public DescremadoRecepcion obtenerPorId(Long id) {
        return repository.obtenerPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el registro de descremado con ID: " + id));
    }

    @Override
    public List<DescremadoRecepcion> listarTodos() {
        return repository.listarTodos();
    }

    @Override
    public List<DescremadoRecepcion> listarPorRecepcion(Long idRecepcionLeche) {
        return repository.listarPorRecepcion(idRecepcionLeche);
    }

    private void validarDescremado(DescremadoRecepcion descremadoRecepcion) {
        if (descremadoRecepcion == null) {
            throw new ReglaNegocioException("El registro de descremado es obligatorio.");
        }

        if (descremadoRecepcion.getIdRecepcionLeche() == null) {
            throw new ReglaNegocioException("La recepción de leche es obligatoria.");
        }

        if (descremadoRecepcion.getLitrosDescremados() == null
                || descremadoRecepcion.getLitrosDescremados().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("Los litros descremados deben ser mayores que cero.");
        }

        if (descremadoRecepcion.getCremaObtenidaKg() != null
                && descremadoRecepcion.getCremaObtenidaKg().compareTo(BigDecimal.ZERO) < 0) {
            throw new ReglaNegocioException("La crema obtenida no puede ser negativa.");
        }
    }
}