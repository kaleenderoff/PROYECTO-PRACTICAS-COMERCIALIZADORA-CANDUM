package com.yerman.produccion_api.application.mapper;
import com.yerman.produccion_api.infrastructure.entity.OrdenProduccionDetalleEntity;

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

    public static ProgramacionSku toDomain(OrdenProduccionDetalleEntity entity) {
        if (entity == null) {
            return null;
        }

        // Primero mapeamos la parte de la programación original (si existe)
        ProgramacionSku domain;
        if (entity.getProgramacionSku() != null) {
            domain = toDomain(entity.getProgramacionSku());
            // El ID que nos importa es el del detalle de la orden para actualizaciones posteriores
            domain.setId(entity.getId());
        } else {
            domain = new ProgramacionSku();
            domain.setId(entity.getId());
            domain.setIdSku(entity.getSku() != null ? entity.getSku().getId() : null);
            if (entity.getSku() != null) {
                domain.setCodigoSku(entity.getSku().getCodigoSku());
                domain.setDescripcionSku(entity.getSku().getDescripcion());
                domain.setPesoUnidadGr(entity.getSku().getPesoNetoGr());
                domain.setKgProductoTerminado(calcularKgProductoTerminado(
                    entity.getCantidadProgramada().intValue(), // Aproximación si no hay unidades
                    entity.getSku().getPesoNetoGr()
                ));
            }
        }

        // Sobrescribimos con los valores reales de la ejecución
        domain.setCantidadReal(entity.getCantidadReal());
        domain.setUnidadesReales(entity.getUnidadesReales());
        domain.setObservaciones(entity.getObservaciones());

        return domain;
    }

    public static ProgramacionSku toDomain(ProgramacionSkuEntity entity) {
        if (entity == null) {
            return null;
        }

        ProgramacionSku domain = new ProgramacionSku(
                entity.getId(),
                entity.getProgramacion() != null ? entity.getProgramacion().getId() : null,
                entity.getSku() != null ? entity.getSku().getId() : null,
                entity.getUnidadesObjetivo(),
                entity.getObservaciones());

        if (entity.getSku() != null) {
            domain.setCodigoSku(entity.getSku().getCodigoSku());
            domain.setDescripcionSku(entity.getSku().getDescripcion());
            domain.setPesoUnidadGr(entity.getSku().getPesoNetoGr());
            
            domain.setKgProductoTerminado(calcularKgProductoTerminado(
                entity.getUnidadesObjetivo(), 
                entity.getSku().getPesoNetoGr()
            ));
        }

        if (entity.getProgramacion() != null && entity.getProgramacion().getFormulaVersion() != null) {
            var formula = entity.getProgramacion().getFormulaVersion();
            domain.setRendimientoTeoricoPct(formula.getRendimientoTeoricoPct());
            domain.setKgBatchFormula(formula.getKgBatchTotal());
            
            domain.setKgBatchCalculado(calcularKgBatch(
                domain.getKgProductoTerminado(), 
                formula.getRendimientoTeoricoPct()
            ));
            
            domain.setNumBachesCalculado(calcularNumeroBaches(
                domain.getKgBatchCalculado(), 
                formula.getKgBatchTotal()
            ));
        }

        return domain;
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
                domain.getCodigoSku(),
                domain.getDescripcionSku(),
                domain.getPesoUnidadGr(),
                domain.getUnidadesObjetivo(),
                domain.getKgProductoTerminado(),
                domain.getRendimientoTeoricoPct(),
                domain.getKgBatchFormula(),
                domain.getKgBatchCalculado(),
                domain.getNumBachesCalculado() != null ? domain.getNumBachesCalculado() : null,
                domain.getCantidadReal(),
                domain.getUnidadesReales(),
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
                null, // cantidadReal
                null, // unidadesReales
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