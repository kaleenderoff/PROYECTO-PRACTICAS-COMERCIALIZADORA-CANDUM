package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.response.ProgramacionSkuResponse;
import com.yerman.produccion_api.domain.model.ProgramacionSku;
import com.yerman.produccion_api.infrastructure.entity.CatalogoSkuEntity;
import com.yerman.produccion_api.infrastructure.entity.FormulaVersionEntity;
import com.yerman.produccion_api.infrastructure.entity.ProgramacionProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.ProgramacionSkuEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ProgramacionSkuMapper {

    private ProgramacionSkuMapper() {
    }

    public static ProgramacionSku toDomain(ProgramacionSkuEntity entity) {
        if (entity == null) {
            return null;
        }

        return new ProgramacionSku(
                entity.getId(),
                entity.getProgramacion() != null ? entity.getProgramacion().getId() : null,
                entity.getSku() != null ? entity.getSku().getId() : null,
                entity.getUnidadesObjetivo(),
                entity.getObservaciones());
    }

    public static ProgramacionSkuEntity toEntity(
            ProgramacionSku domain,
            ProgramacionProduccionEntity programacion,
            CatalogoSkuEntity sku) {
        if (domain == null) {
            return null;
        }

        ProgramacionSkuEntity entity = new ProgramacionSkuEntity();
        entity.setProgramacion(programacion);
        entity.setSku(sku);
        entity.setUnidadesObjetivo(domain.getUnidadesObjetivo());
        entity.setObservaciones(domain.getObservaciones());
        return entity;
    }

    public static ProgramacionSkuResponse toResponse(ProgramacionSku domain) {
        if (domain == null) {
            return null;
        }

        return new ProgramacionSkuResponse(
                domain.getId(),
                domain.getIdProgramacion(),
                domain.getIdSku(),
                null,
                null,
                null,
                domain.getUnidadesObjetivo(),
                null,
                null,
                null,
                null,
                null,
                domain.getObservaciones());
    }

    public static ProgramacionSkuResponse toResponse(ProgramacionSkuEntity entity) {
        if (entity == null) {
            return null;
        }

        CatalogoSkuEntity sku = entity.getSku();
        ProgramacionProduccionEntity programacion = entity.getProgramacion();
        FormulaVersionEntity formulaVersion = programacion != null ? programacion.getFormulaVersion() : null;

        Integer unidades = entity.getUnidadesObjetivo();
        Integer pesoUnidadGr = sku != null ? sku.getPesoNetoGr() : null;

        BigDecimal kgProductoTerminado = calcularKgProductoTerminado(unidades, pesoUnidadGr);

        BigDecimal rendimientoTeoricoPct = formulaVersion != null
                ? formulaVersion.getRendimientoTeoricoPct()
                : null;

        BigDecimal kgBatchFormula = formulaVersion != null
                ? formulaVersion.getKgBatchTotal()
                : null;

        BigDecimal kgBatchCalculado = calcularKgBatch(kgProductoTerminado, rendimientoTeoricoPct);
        BigDecimal numBachesCalculado = calcularNumeroBaches(kgBatchCalculado, kgBatchFormula);

        return new ProgramacionSkuResponse(
                entity.getId(),
                programacion != null ? programacion.getId() : null,
                sku != null ? sku.getId() : null,
                sku != null ? sku.getCodigoSku() : null,
                sku != null ? sku.getDescripcion() : null,
                pesoUnidadGr,
                unidades,
                kgProductoTerminado,
                rendimientoTeoricoPct,
                kgBatchFormula,
                kgBatchCalculado,
                numBachesCalculado,
                entity.getObservaciones());
    }

    private static BigDecimal calcularKgProductoTerminado(Integer unidades, Integer pesoUnidadGr) {
        if (unidades == null || pesoUnidadGr == null) {
            return null;
        }

        return BigDecimal.valueOf(unidades)
                .multiply(BigDecimal.valueOf(pesoUnidadGr))
                .divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
    }

    private static BigDecimal calcularKgBatch(BigDecimal kgProductoTerminado, BigDecimal rendimientoTeoricoPct) {
        if (kgProductoTerminado == null || rendimientoTeoricoPct == null
                || rendimientoTeoricoPct.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }

        BigDecimal rendimientoDecimal = rendimientoTeoricoPct.compareTo(BigDecimal.ONE) > 0
                ? rendimientoTeoricoPct.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP)
                : rendimientoTeoricoPct;

        return kgProductoTerminado.divide(rendimientoDecimal, 3, RoundingMode.HALF_UP);
    }

    private static BigDecimal calcularNumeroBaches(BigDecimal kgBatchCalculado, BigDecimal kgBatchFormula) {
        if (kgBatchCalculado == null || kgBatchFormula == null || kgBatchFormula.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }

        return kgBatchCalculado.divide(kgBatchFormula, 3, RoundingMode.HALF_UP);
    }
}