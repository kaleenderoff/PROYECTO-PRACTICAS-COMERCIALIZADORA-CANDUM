package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class SimularProgramacionResponse {

    private BigDecimal totalKgProductoTerminado;
    private BigDecimal totalKgBatch;
    private BigDecimal totalBatches;

    private List<DetalleSimulacionSkuResponse> detalle;

    public SimularProgramacionResponse(
            BigDecimal totalKgProductoTerminado,
            BigDecimal totalKgBatch,
            BigDecimal totalBatches,
            List<DetalleSimulacionSkuResponse> detalle) {
        this.totalKgProductoTerminado = totalKgProductoTerminado;
        this.totalKgBatch = totalKgBatch;
        this.totalBatches = totalBatches;
        this.detalle = detalle;
    }

    public BigDecimal getTotalKgProductoTerminado() {
        return totalKgProductoTerminado;
    }

    public BigDecimal getTotalKgBatch() {
        return totalKgBatch;
    }

    public BigDecimal getTotalBatches() {
        return totalBatches;
    }

    public List<DetalleSimulacionSkuResponse> getDetalle() {
        return detalle;
    }

    public static class DetalleSimulacionSkuResponse {

        private Long idSku;
        private String codigoSku;
        private String descripcionSku;

        private Integer unidades;
        private Integer pesoUnidadGr;

        private BigDecimal kgProductoTerminado;
        private BigDecimal kgBatchCalculado;
        private BigDecimal batchesCalculados;

        public DetalleSimulacionSkuResponse(
                Long idSku,
                String codigoSku,
                String descripcionSku,
                Integer unidades,
                Integer pesoUnidadGr,
                BigDecimal kgProductoTerminado,
                BigDecimal kgBatchCalculado,
                BigDecimal batchesCalculados) {
            this.idSku = idSku;
            this.codigoSku = codigoSku;
            this.descripcionSku = descripcionSku;
            this.unidades = unidades;
            this.pesoUnidadGr = pesoUnidadGr;
            this.kgProductoTerminado = kgProductoTerminado;
            this.kgBatchCalculado = kgBatchCalculado;
            this.batchesCalculados = batchesCalculados;
        }

        public Long getIdSku() {
            return idSku;
        }

        public String getCodigoSku() {
            return codigoSku;
        }

        public String getDescripcionSku() {
            return descripcionSku;
        }

        public Integer getUnidades() {
            return unidades;
        }

        public Integer getPesoUnidadGr() {
            return pesoUnidadGr;
        }

        public BigDecimal getKgProductoTerminado() {
            return kgProductoTerminado;
        }

        public BigDecimal getKgBatchCalculado() {
            return kgBatchCalculado;
        }

        public BigDecimal getBatchesCalculados() {
            return batchesCalculados;
        }
    }
}