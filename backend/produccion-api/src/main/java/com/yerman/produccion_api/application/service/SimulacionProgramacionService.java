package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.dto.request.SimularProgramacionRequest;
import com.yerman.produccion_api.application.dto.response.SimularProgramacionResponse;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.infrastructure.entity.CatalogoSkuEntity;
import com.yerman.produccion_api.infrastructure.entity.FormulaVersionEntity;
import com.yerman.produccion_api.infrastructure.repository.CatalogoSkuJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.FormulaVersionJpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class SimulacionProgramacionService {

    private final FormulaVersionJpaRepository formulaVersionRepository;
    private final CatalogoSkuJpaRepository skuRepository;

    public SimulacionProgramacionService(
            FormulaVersionJpaRepository formulaVersionRepository,
            CatalogoSkuJpaRepository skuRepository) {
        this.formulaVersionRepository = formulaVersionRepository;
        this.skuRepository = skuRepository;
    }

    public SimularProgramacionResponse simular(SimularProgramacionRequest request) {
        FormulaVersionEntity formulaVigente = formulaVersionRepository
                .findFirstByFormulaProductoIdAndEstadoOrderByFechaInicioVigenciaDesc(
                        request.getIdProducto(),
                        FormulaVersionEntity.EstadoFormula.VIGENTE)
                .orElseThrow(() -> new ReglaNegocioException(
                        "No existe una fórmula vigente para el producto seleccionado"));

        validarFormula(formulaVigente);

        BigDecimal totalKgProductoTerminado = BigDecimal.ZERO;
        BigDecimal totalKgBatch = BigDecimal.ZERO;
        BigDecimal totalBatches = BigDecimal.ZERO;

        List<SimularProgramacionResponse.DetalleSimulacionSkuResponse> detalle = new ArrayList<>();

        for (SimularProgramacionRequest.SimularSkuRequest skuRequest : request.getSkus()) {
            CatalogoSkuEntity sku = skuRepository.findById(skuRequest.getIdSku())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "SKU no encontrado con id: " + skuRequest.getIdSku()));

            validarSkuPerteneceAlProducto(sku, request.getIdProducto());

            BigDecimal kgProductoTerminado = calcularKgProductoTerminado(
                    skuRequest.getUnidades(),
                    sku.getPesoNetoGr());

            BigDecimal kgBatchCalculado = calcularKgBatch(
                    kgProductoTerminado,
                    formulaVigente.getRendimientoTeoricoPct());

            BigDecimal batchesCalculados = calcularBatches(
                    kgBatchCalculado,
                    formulaVigente.getKgBatchTotal());

            totalKgProductoTerminado = totalKgProductoTerminado.add(kgProductoTerminado);
            totalKgBatch = totalKgBatch.add(kgBatchCalculado);
            totalBatches = totalBatches.add(batchesCalculados);

            detalle.add(new SimularProgramacionResponse.DetalleSimulacionSkuResponse(
                    sku.getId(),
                    sku.getCodigoSku(),
                    sku.getDescripcion(),
                    skuRequest.getUnidades(),
                    sku.getPesoNetoGr(),
                    kgProductoTerminado,
                    kgBatchCalculado,
                    batchesCalculados));
        }

        return new SimularProgramacionResponse(
                totalKgProductoTerminado.setScale(3, RoundingMode.HALF_UP),
                totalKgBatch.setScale(3, RoundingMode.HALF_UP),
                totalBatches.setScale(3, RoundingMode.HALF_UP),
                detalle);
    }

    private void validarFormula(FormulaVersionEntity formulaVersion) {
        if (formulaVersion.getKgBatchTotal() == null
                || formulaVersion.getKgBatchTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("La fórmula vigente no tiene kg batch total configurado");
        }

        if (formulaVersion.getRendimientoTeoricoPct() == null
                || formulaVersion.getRendimientoTeoricoPct().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("La fórmula vigente no tiene rendimiento teórico configurado");
        }
    }

    private void validarSkuPerteneceAlProducto(CatalogoSkuEntity sku, Long idProducto) {
        if (sku.getProducto() == null || !sku.getProducto().getId().equals(idProducto)) {
            throw new ReglaNegocioException(
                    "El SKU " + sku.getCodigoSku() + " no pertenece al producto seleccionado");
        }
    }

    private BigDecimal calcularKgProductoTerminado(Integer unidades, Integer pesoUnidadGr) {
        return BigDecimal.valueOf(unidades)
                .multiply(BigDecimal.valueOf(pesoUnidadGr))
                .divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularKgBatch(BigDecimal kgProductoTerminado, BigDecimal rendimientoTeoricoPct) {
        BigDecimal rendimientoDecimal = rendimientoTeoricoPct.compareTo(BigDecimal.ONE) > 0
                ? rendimientoTeoricoPct.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP)
                : rendimientoTeoricoPct;

        return kgProductoTerminado.divide(rendimientoDecimal, 3, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularBatches(BigDecimal kgBatchCalculado, BigDecimal kgBatchFormula) {
        return kgBatchCalculado.divide(kgBatchFormula, 3, RoundingMode.HALF_UP);
    }
}