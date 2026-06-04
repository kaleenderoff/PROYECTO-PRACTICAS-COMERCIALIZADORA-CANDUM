package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.DescremadoRecepcion;
import com.yerman.produccion_api.domain.model.MovimientoLeche;
import com.yerman.produccion_api.domain.model.TipoMovimientoLeche;
import com.yerman.produccion_api.domain.port.in.GestionDescremadoRecepcionUseCase;
import com.yerman.produccion_api.domain.port.in.GestionMovimientoLecheUseCase;
import com.yerman.produccion_api.domain.port.out.DescremadoRecepcionRepositoryPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class GestionDescremadoRecepcionService implements GestionDescremadoRecepcionUseCase {

    private final DescremadoRecepcionRepositoryPort repository;
    private final GestionMovimientoLecheUseCase movimientoLecheUseCase;

    public GestionDescremadoRecepcionService(
            DescremadoRecepcionRepositoryPort repository,
            GestionMovimientoLecheUseCase movimientoLecheUseCase) {
        this.repository = repository;
        this.movimientoLecheUseCase = movimientoLecheUseCase;
    }

    @Override
    @Transactional
    public DescremadoRecepcion registrarDescremado(DescremadoRecepcion descremadoRecepcion) {
        validarDescremado(descremadoRecepcion);

        MovimientoLeche movimientoSalida = movimientoLecheUseCase.registrarMovimiento(
                descremadoRecepcion.getIdTanqueOrigen(),
                TipoMovimientoLeche.SALIDA_DESCREME,
                descremadoRecepcion.getLitrosDescremados(),
                descremadoRecepcion.getIdUsuario(),
                construirReferenciaSalida(descremadoRecepcion),
                descremadoRecepcion.getObservaciones());

        descremadoRecepcion.setIdMovimientoSalida(movimientoSalida.getId());

        if (descremadoRecepcion.getIdTanqueDestino() != null) {
            BigDecimal litrosTransferidos = calcularLecheDescremadaTransferida(descremadoRecepcion);

            MovimientoLeche movimientoEntrada = movimientoLecheUseCase.registrarMovimiento(
                    descremadoRecepcion.getIdTanqueDestino(),
                    TipoMovimientoLeche.ENTRADA_DESCREME,
                    litrosTransferidos,
                    descremadoRecepcion.getIdUsuario(),
                    construirReferenciaEntrada(descremadoRecepcion),
                    descremadoRecepcion.getObservaciones());

            descremadoRecepcion.setIdMovimientoEntrada(movimientoEntrada.getId());
        } else {
            descremadoRecepcion.setIdMovimientoEntrada(null);
        }

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

        if (descremadoRecepcion.getFechaDescremado() == null) {
            throw new ReglaNegocioException("La fecha de descremado es obligatoria.");
        }

        if (descremadoRecepcion.getFechaDescremado().isAfter(LocalDate.now().plusDays(1))) {
            throw new ReglaNegocioException("La fecha de descremado no puede ser una fecha futura lejana.");
        }

        if (descremadoRecepcion.getIdTanqueOrigen() == null) {
            throw new ReglaNegocioException("El tanque origen de la leche es obligatorio.");
        }

        if (descremadoRecepcion.getIdTanqueDestino() == null) {
            throw new ReglaNegocioException("El tanque destino de la leche descremada es obligatorio.");
        }

        if (descremadoRecepcion.getIdUsuario() == null) {
            throw new ReglaNegocioException("El usuario que registra el descremado es obligatorio.");
        }

        if (descremadoRecepcion.getLitrosDescremados() == null
                || descremadoRecepcion.getLitrosDescremados().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("Los litros descremados deben ser mayores que cero.");
        }

        BigDecimal saldoActual = movimientoLecheUseCase.obtenerSaldoActualPorTanque(
                descremadoRecepcion.getIdTanqueOrigen());

        if (saldoActual.compareTo(descremadoRecepcion.getLitrosDescremados()) < 0) {
            throw new ReglaNegocioException(
                    "No hay suficiente leche disponible en el tanque origen. Saldo actual: "
                            + saldoActual + " L, cantidad solicitada: "
                            + descremadoRecepcion.getLitrosDescremados() + " L.");
        }

        if (descremadoRecepcion.getIdTanqueDestino().equals(descremadoRecepcion.getIdTanqueOrigen())) {
            throw new ReglaNegocioException("El tanque destino no puede ser igual al tanque origen.");
        }

        if (descremadoRecepcion.getCremaObtenidaKg() != null
                && descremadoRecepcion.getCremaObtenidaKg().compareTo(BigDecimal.ZERO) < 0) {
            throw new ReglaNegocioException("La crema obtenida no puede ser negativa.");
        }

        if (descremadoRecepcion.getCremaObtenidaKg() != null
                && descremadoRecepcion.getCremaObtenidaKg().compareTo(descremadoRecepcion.getLitrosDescremados()) >= 0) {
            throw new ReglaNegocioException(
                    "La crema obtenida no puede ser mayor o igual a los litros descremados.");
        }

        if (calcularLecheDescremadaTransferida(descremadoRecepcion).compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException(
                    "La leche descremada transferida al tanque destino debe ser mayor que cero.");
        }

        validarCremaEmpacada(descremadoRecepcion);
    }

    private BigDecimal calcularLecheDescremadaTransferida(DescremadoRecepcion descremadoRecepcion) {
        BigDecimal litrosDescremados = descremadoRecepcion.getLitrosDescremados() != null
                ? descremadoRecepcion.getLitrosDescremados()
                : BigDecimal.ZERO;
        BigDecimal cremaObtenida = descremadoRecepcion.getCremaObtenidaKg() != null
                ? descremadoRecepcion.getCremaObtenidaKg()
                : BigDecimal.ZERO;

        return litrosDescremados.subtract(cremaObtenida);
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

    private String construirReferenciaSalida(DescremadoRecepcion descremadoRecepcion) {
        return "Salida a descreme - "
                + descremadoRecepcion.getFechaDescremado()
                + " - Tanque origen ID "
                + descremadoRecepcion.getIdTanqueOrigen();
    }

    private String construirReferenciaEntrada(DescremadoRecepcion descremadoRecepcion) {
        return "Entrada por descreme - "
                + descremadoRecepcion.getFechaDescremado()
                + " - Tanque origen ID "
                + descremadoRecepcion.getIdTanqueOrigen();
    }
}
