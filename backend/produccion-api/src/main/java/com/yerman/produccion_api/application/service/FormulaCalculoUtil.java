package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.domain.model.TipoCalculoInsumo;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class FormulaCalculoUtil {

    private static final BigDecimal CIEN = BigDecimal.valueOf(100);

    private FormulaCalculoUtil() {
    }

    public static BigDecimal calcularCantidadPorBatch(
            TipoCalculoInsumo tipoCalculo,
            BigDecimal cantidadKgFijo,
            BigDecimal porcentaje,
            BigDecimal kgBatchTotal) {
        TipoCalculoInsumo tipoSeguro = tipoCalculo != null
                ? tipoCalculo
                : TipoCalculoInsumo.PORCENTAJE_BATCH;

        if (tipoSeguro == TipoCalculoInsumo.FIJO) {
            return cantidadKgFijo != null
                    ? cantidadKgFijo.setScale(3, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO.setScale(3, RoundingMode.HALF_UP);
        }

        if (porcentaje == null || kgBatchTotal == null || kgBatchTotal.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(3, RoundingMode.HALF_UP);
        }

        return porcentaje
                .multiply(kgBatchTotal)
                .divide(CIEN, 3, RoundingMode.HALF_UP);
    }

    public static BigDecimal calcularPorcentajeInformativo(
            BigDecimal cantidadKg,
            BigDecimal kgBatchTotal) {
        if (cantidadKg == null || kgBatchTotal == null || kgBatchTotal.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(6, RoundingMode.HALF_UP);
        }

        return cantidadKg
                .multiply(CIEN)
                .divide(kgBatchTotal, 6, RoundingMode.HALF_UP);
    }

    public static BigDecimal calcularKgPlan(
            Integer unidades,
            Integer pesoNetoGr) {
        if (unidades == null || pesoNetoGr == null || unidades <= 0 || pesoNetoGr <= 0) {
            return BigDecimal.ZERO.setScale(3, RoundingMode.HALF_UP);
        }

        return BigDecimal.valueOf((long) unidades * pesoNetoGr)
                .divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
    }

    public static BigDecimal calcularKgEntradaNecesaria(
            BigDecimal kgPlan,
            BigDecimal rendimientoTeoricoPct) {
        if (kgPlan == null || rendimientoTeoricoPct == null || rendimientoTeoricoPct.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(3, RoundingMode.HALF_UP);
        }

        return kgPlan
                .multiply(CIEN)
                .divide(rendimientoTeoricoPct, 3, RoundingMode.HALF_UP);
    }

    public static BigDecimal calcularNumeroBaches(
            BigDecimal kgEntradaNecesaria,
            BigDecimal kgBatchTotal) {
        if (kgEntradaNecesaria == null || kgBatchTotal == null || kgBatchTotal.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP);
        }

        return kgEntradaNecesaria
                .divide(kgBatchTotal, 4, RoundingMode.HALF_UP);
    }

    public static BigDecimal calcularRendimientoReal(
            BigDecimal kgProducidosReales,
            Integer numBaches,
            BigDecimal kgBatchTotal) {
        if (kgProducidosReales == null || numBaches == null || kgBatchTotal == null || numBaches <= 0) {
            return BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP);
        }

        BigDecimal kgEntrada = kgBatchTotal.multiply(BigDecimal.valueOf(numBaches));

        if (kgEntrada.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP);
        }

        return kgProducidosReales
                .divide(kgEntrada, 4, RoundingMode.HALF_UP);
    }

    public static BigDecimal calcularPctReduccion(
            BigDecimal kgProducidos,
            BigDecimal kgLecheEntrada) {
        if (kgProducidos == null || kgLecheEntrada == null || kgLecheEntrada.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP);
        }

        BigDecimal ratio = kgProducidos
                .divide(kgLecheEntrada, 6, RoundingMode.HALF_UP);

        return BigDecimal.ONE
                .subtract(ratio)
                .setScale(4, RoundingMode.HALF_UP);
    }
}