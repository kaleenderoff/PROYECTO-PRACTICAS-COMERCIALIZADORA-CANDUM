package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.MovimientoLeche;
import com.yerman.produccion_api.domain.model.TipoMovimientoLeche;
import com.yerman.produccion_api.domain.port.in.GestionMovimientoLecheUseCase;
import com.yerman.produccion_api.domain.port.out.MovimientoLecheRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GestionMovimientoLecheService implements GestionMovimientoLecheUseCase {

    private final MovimientoLecheRepositoryPort movimientoRepository;

    public GestionMovimientoLecheService(MovimientoLecheRepositoryPort movimientoRepository) {
        this.movimientoRepository = movimientoRepository;
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
        return movimientoRepository.obtenerSaldoActualPorTanque(idTanque);
    }

    @Override
    public List<MovimientoLeche> listarPorTanque(Long idTanque) {
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

    private BigDecimal calcularNuevoSaldo(
            BigDecimal saldoActual,
            TipoMovimientoLeche tipoMovimiento,
            BigDecimal cantidadLitros) {

        if (tipoMovimiento == null) {
            throw new ReglaNegocioException("El tipo de movimiento es obligatorio.");
        }

        return switch (tipoMovimiento) {
            case SALDO_INICIAL, ENTRADA_RECEPCION, AJUSTE_POSITIVO ->
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