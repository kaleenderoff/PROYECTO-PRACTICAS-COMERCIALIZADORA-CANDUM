package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.domain.model.MovimientoLeche;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MovimientoLecheRepositoryPort {

    MovimientoLeche guardar(MovimientoLeche movimiento);

    Optional<MovimientoLeche> obtenerUltimoMovimientoPorTanque(Long idTanque);

    BigDecimal obtenerSaldoActualPorTanque(Long idTanque);

    List<MovimientoLeche> listarPorTanque(Long idTanque);

    List<MovimientoLeche> listarPorFecha(LocalDate fecha);
}