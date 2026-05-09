package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.DescremadoRecepcion;
import com.yerman.produccion_api.domain.model.MovimientoLeche;
import com.yerman.produccion_api.domain.model.RecepcionLeche;
import com.yerman.produccion_api.domain.model.TipoMovimientoLeche;
import com.yerman.produccion_api.domain.port.in.GestionDescremadoRecepcionUseCase;
import com.yerman.produccion_api.domain.port.in.GestionMovimientoLecheUseCase;
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
    private final GestionMovimientoLecheUseCase movimientoLecheUseCase;

    public GestionDescremadoRecepcionService(
            DescremadoRecepcionRepositoryPort repository,
            GestionRecepcionLecheUseCase recepcionLecheUseCase,
            GestionMovimientoLecheUseCase movimientoLecheUseCase) {
        this.repository = repository;
        this.recepcionLecheUseCase = recepcionLecheUseCase;
        this.movimientoLecheUseCase = movimientoLecheUseCase;
    }

    @Override
    @Transactional
    public DescremadoRecepcion registrarDescremado(DescremadoRecepcion descremadoRecepcion) {
        validarDescremado(descremadoRecepcion);

        RecepcionLeche recepcion = recepcionLecheUseCase.obtenerPorId(
                descremadoRecepcion.getIdRecepcionLeche());

        validarDisponibleEnRecepcion(recepcion, descremadoRecepcion);

        MovimientoLeche movimientoEntrada = movimientoLecheUseCase.registrarMovimiento(
                descremadoRecepcion.getIdTanqueDestino(),
                TipoMovimientoLeche.ENTRADA_DESCREME,
                descremadoRecepcion.getLitrosDescremados(),
                recepcion.getIdUsuario(),
                construirReferenciaEntrada(recepcion),
                descremadoRecepcion.getObservaciones());

        descremadoRecepcion.setIdMovimientoSalida(null);
        descremadoRecepcion.setIdMovimientoEntrada(movimientoEntrada.getId());

        return repository.guardar(descremadoRecepcion);
    }

    @Override
    public DescremadoRecepcion obtenerPorId(Long id) {
        return repository.obtenerPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro el registro de descremado con ID: " + id));
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
            throw new ReglaNegocioException("La recepcion de leche es obligatoria.");
        }

        if (descremadoRecepcion.getIdTanqueDestino() == null) {
            throw new ReglaNegocioException("El tanque destino de leche descremada es obligatorio.");
        }

        if (descremadoRecepcion.getLitrosDescremados() == null
                || descremadoRecepcion.getLitrosDescremados().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("Los litros descremados deben ser mayores que cero.");
        }

        if (descremadoRecepcion.getCremaObtenidaKg() != null
                && descremadoRecepcion.getCremaObtenidaKg().compareTo(BigDecimal.ZERO) < 0) {
            throw new ReglaNegocioException("La crema obtenida no puede ser negativa.");
        }

        validarCremaEmpacada(descremadoRecepcion);
    }

    private void validarDisponibleEnRecepcion(
            RecepcionLeche recepcion,
            DescremadoRecepcion descremadoRecepcion) {

        BigDecimal litrosRecibidos = recepcion.getCantidadRecibidaLitros() != null
                ? recepcion.getCantidadRecibidaLitros()
                : BigDecimal.ZERO;

        BigDecimal litrosYaDescremados = repository.listarPorRecepcion(recepcion.getId())
                .stream()
                .map(DescremadoRecepcion::getLitrosDescremados)
                .filter(valor -> valor != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal disponible = litrosRecibidos.subtract(litrosYaDescremados);

        if (disponible.compareTo(descremadoRecepcion.getLitrosDescremados()) < 0) {
            throw new ReglaNegocioException(
                    "No hay suficiente leche disponible en la recepcion para descremar. Recepcion ID: "
                            + recepcion.getId()
                            + ", recibido: " + litrosRecibidos
                            + " L, ya descremado: " + litrosYaDescremados
                            + " L, disponible: " + disponible
                            + " L, requerido: " + descremadoRecepcion.getLitrosDescremados() + " L.");
        }
    }

    private void validarCremaEmpacada(DescremadoRecepcion descremadoRecepcion) {
        boolean tieneCremaEmpacada = descremadoRecepcion.getIdSkuCrema() != null
                || descremadoRecepcion.getUnidadesCrema() != null
                || descremadoRecepcion.getKgPorUnidadCrema() != null
                || (descremadoRecepcion.getLoteCrema() != null && !descremadoRecepcion.getLoteCrema().isBlank());

        if (!tieneCremaEmpacada) {
            return;
        }

        if (descremadoRecepcion.getIdSkuCrema() == null) {
            throw new ReglaNegocioException(
                    "La presentacion/SKU de crema es obligatoria si se registra crema empacada.");
        }

        if (descremadoRecepcion.getUnidadesCrema() == null || descremadoRecepcion.getUnidadesCrema() <= 0) {
            throw new ReglaNegocioException("Las unidades de crema empacada deben ser mayores que cero.");
        }

        if (descremadoRecepcion.getKgPorUnidadCrema() == null
                || descremadoRecepcion.getKgPorUnidadCrema().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("Los kg por unidad de crema deben ser mayores que cero.");
        }

        if (descremadoRecepcion.getCremaObtenidaKg() == null
                || descremadoRecepcion.getCremaObtenidaKg().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("La crema obtenida en kg es obligatoria si se registra crema empacada.");
        }
    }

    private String construirReferenciaEntrada(RecepcionLeche recepcion) {
        if (recepcion.getNumeroRemision() != null && !recepcion.getNumeroRemision().isBlank()) {
            return "Entrada leche descremada - Remision " + recepcion.getNumeroRemision();
        }

        return "Entrada leche descremada - Recepcion ID " + recepcion.getId();
    }
}