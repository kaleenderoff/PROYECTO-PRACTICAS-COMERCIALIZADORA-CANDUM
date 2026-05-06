package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.MovimientoLeche;
import com.yerman.produccion_api.domain.model.SaldoTanqueLeche;
import com.yerman.produccion_api.domain.model.TipoMovimientoLeche;
import com.yerman.produccion_api.domain.port.in.GestionMovimientoLecheUseCase;
import com.yerman.produccion_api.domain.port.out.MovimientoLecheRepositoryPort;
import com.yerman.produccion_api.domain.port.out.TanqueLecheRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GestionMovimientoLecheService implements GestionMovimientoLecheUseCase {

    private final MovimientoLecheRepositoryPort movimientoRepository;
    private final TanqueLecheRepositoryPort tanqueRepository;

    public GestionMovimientoLecheService(
            MovimientoLecheRepositoryPort movimientoRepository,
            TanqueLecheRepositoryPort tanqueRepository) {
        this.movimientoRepository = movimientoRepository;
        this.tanqueRepository = tanqueRepository;
    }

    @Override
    public MovimientoLeche registrarMovimiento(
            Long idTanque,
            TipoMovimientoLeche tipoMovimiento,
            BigDecimal cantidadLitros,
            Long idUsuario,
            String referencia,
            String observaciones) {

        validarCantidad(cantidadLitros);
        validarTanqueActivo(idTanque);

        BigDecimal saldoActual = movimientoRepository.obtenerSaldoActualPorTanque(idTanque);
        BigDecimal nuevoSaldo = calcularNuevoSaldo(saldoActual, tipoMovimiento, cantidadLitros);

        MovimientoLeche movimiento = new MovimientoLeche();
        movimiento.setIdTanque(idTanque);
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setFechaHora(LocalDateTime.now());
        movimiento.setCantidadLitros(cantidadLitros);
        movimiento.setSaldoResultanteLitros(nuevoSaldo);
        movimiento.setIdUsuario(idUsuario);
        movimiento.setReferencia(referencia);
        movimiento.setObservaciones(observaciones);

        return movimientoRepository.guardar(movimiento);
    }

    @Override
    public BigDecimal obtenerSaldoActualPorTanque(Long idTanque) {
        validarTanqueExistente(idTanque);
        return movimientoRepository.obtenerSaldoActualPorTanque(idTanque);
    }

    @Override
    public List<MovimientoLeche> listarPorTanque(Long idTanque) {
        validarTanqueExistente(idTanque);
        return movimientoRepository.listarPorTanque(idTanque);
    }

    @Override
    public List<MovimientoLeche> listarPorFecha(LocalDate fecha) {
        return movimientoRepository.listarPorFecha(fecha);
    }

    private void validarCantidad(BigDecimal cantidadLitros) {
        if (cantidadLitros == null || cantidadLitros.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("La cantidad de litros debe ser mayor que cero.");
        }
    }

    private SaldoTanqueLeche validarTanqueExistente(Long idTanque) {
        if (idTanque == null) {
            throw new ReglaNegocioException("El tanque de leche es obligatorio.");
        }

        return tanqueRepository.obtenerSaldo(idTanque)
                .orElseThrow(() -> new ReglaNegocioException("No existe un tanque de leche con ID: " + idTanque));
    }

    private void validarTanqueActivo(Long idTanque) {
        SaldoTanqueLeche tanque = validarTanqueExistente(idTanque);
        if (!Boolean.TRUE.equals(tanque.getActivo())) {
            throw new ReglaNegocioException("El tanque de leche no esta activo: " + tanque.getNombre());
        }
    }

    private BigDecimal calcularNuevoSaldo(
            BigDecimal saldoActual,
            TipoMovimientoLeche tipoMovimiento,
            BigDecimal cantidadLitros) {

        if (tipoMovimiento == null) {
            throw new ReglaNegocioException("El tipo de movimiento es obligatorio.");
        }

        return switch (tipoMovimiento) {
            case SALDO_INICIAL, ENTRADA_RECEPCION, ENTRADA_DESCREME, AJUSTE_POSITIVO ->
                saldoActual.add(cantidadLitros);

            case SALIDA_PRODUCCION, SALIDA_DESCREME, AJUSTE_NEGATIVO -> {
                if (saldoActual.compareTo(cantidadLitros) < 0) {
                    throw new ReglaNegocioException(
                            "No hay suficiente leche disponible. Saldo actual: "
                                    + saldoActual + " L, cantidad solicitada: " + cantidadLitros + " L.");
                }
                yield saldoActual.subtract(cantidadLitros);
            }
        };
    }
}
