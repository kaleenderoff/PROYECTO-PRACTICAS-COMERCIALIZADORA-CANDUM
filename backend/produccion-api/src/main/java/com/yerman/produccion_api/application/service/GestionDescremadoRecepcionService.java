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
import com.yerman.produccion_api.infrastructure.entity.CalidadRecepcionLecheEntity;
import com.yerman.produccion_api.infrastructure.repository.CalidadRecepcionLecheJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class GestionDescremadoRecepcionService implements GestionDescremadoRecepcionUseCase {

    private final DescremadoRecepcionRepositoryPort repository;
    private final GestionRecepcionLecheUseCase recepcionLecheUseCase;
    private final GestionMovimientoLecheUseCase movimientoLecheUseCase;
    private final CalidadRecepcionLecheJpaRepository calidadRecepcionRepository;

    public GestionDescremadoRecepcionService(
            DescremadoRecepcionRepositoryPort repository,
            GestionRecepcionLecheUseCase recepcionLecheUseCase,
            GestionMovimientoLecheUseCase movimientoLecheUseCase,
            CalidadRecepcionLecheJpaRepository calidadRecepcionRepository) {
        this.repository = repository;
        this.recepcionLecheUseCase = recepcionLecheUseCase;
        this.movimientoLecheUseCase = movimientoLecheUseCase;
        this.calidadRecepcionRepository = calidadRecepcionRepository;
    }

    @Override
    @Transactional
    public DescremadoRecepcion registrarDescremado(DescremadoRecepcion descremadoRecepcion) {
        validarDescremado(descremadoRecepcion);

        RecepcionLeche recepcion = recepcionLecheUseCase.obtenerPorId(
                descremadoRecepcion.getIdRecepcionLeche());

        validarCalidadAprobada(recepcion);
        validarDisponibleEnRecepcion(recepcion, descremadoRecepcion);

        /*
         * El descremado consume leche del tanque donde entró la recepción.
         * Por eso aquí SOLO se registra SALIDA_DESCREME.
         *
         * No se debe registrar ENTRADA_DESCREME por la misma cantidad,
         * porque eso vuelve a sumar lo que se acaba de restar y el saldo
         * del tanque queda igual.
         */
        MovimientoLeche movimientoSalida = movimientoLecheUseCase.registrarMovimiento(
                recepcion.getIdTanque(),
                TipoMovimientoLeche.SALIDA_DESCREME,
                descremadoRecepcion.getLitrosDescremados(),
                recepcion.getIdUsuario(),
                construirReferenciaSalida(recepcion),
                descremadoRecepcion.getObservaciones());

        descremadoRecepcion.setIdMovimientoSalida(movimientoSalida.getId());
        descremadoRecepcion.setIdMovimientoEntrada(null);

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

    private void validarCalidadAprobada(RecepcionLeche recepcion) {
        CalidadRecepcionLecheEntity calidad = calidadRecepcionRepository
                .findFirstByRecepcionLecheIdOrderByFechaControlDescIdDesc(recepcion.getId())
                .orElseThrow(() -> new ReglaNegocioException(
                        "La recepcion debe tener control de calidad registrado antes de descremar."));

        if (Boolean.TRUE.equals(calidad.getRetenido())) {
            throw new ReglaNegocioException(
                    "No se puede descremar una recepcion retenida por calidad.");
        }

        if (!Boolean.TRUE.equals(calidad.getAprobado())) {
            throw new ReglaNegocioException(
                    "No se puede descremar una recepcion no aprobada por calidad.");
        }
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
        normalizarLoteCrema(descremadoRecepcion);

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

        if (descremadoRecepcion.getLoteCrema() == null || descremadoRecepcion.getLoteCrema().isBlank()) {
            throw new ReglaNegocioException("El lote de crema es obligatorio si se registra crema empacada.");
        }

        if (repository.existeLoteCrema(descremadoRecepcion.getLoteCrema())) {
            throw new ReglaNegocioException(
                    "Ya existe un registro de crema empacada con el lote: " + descremadoRecepcion.getLoteCrema());
        }
    }

    private void normalizarLoteCrema(DescremadoRecepcion descremadoRecepcion) {
        if (descremadoRecepcion.getLoteCrema() == null) {
            return;
        }

        String lote = descremadoRecepcion.getLoteCrema().trim();
        descremadoRecepcion.setLoteCrema(lote.isBlank() ? null : lote);
    }

    private String construirReferenciaSalida(RecepcionLeche recepcion) {
        if (recepcion.getNumeroRemision() != null && !recepcion.getNumeroRemision().isBlank()) {
            return "Salida a descreme - Remision " + recepcion.getNumeroRemision();
        }

        return "Salida a descreme - Recepcion ID " + recepcion.getId();
    }
}