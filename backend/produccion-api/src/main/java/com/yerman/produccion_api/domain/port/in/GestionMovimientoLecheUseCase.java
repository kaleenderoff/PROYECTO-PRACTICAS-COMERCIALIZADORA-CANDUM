package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.domain.model.MovimientoLeche;
import com.yerman.produccion_api.domain.model.TipoMovimientoLeche;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface GestionMovimientoLecheUseCase {

    MovimientoLeche registrarMovimiento(
            Long idTanque,
            TipoMovimientoLeche tipoMovimiento,
            BigDecimal cantidadLitros,
            Long idUsuario,
            String referencia,
            String observaciones);

    BigDecimal obtenerSaldoActualPorTanque(Long idTanque);

    List<MovimientoLeche> listarPorTanque(Long idTanque);

    List<MovimientoLeche> listarPorFecha(LocalDate fecha);
}