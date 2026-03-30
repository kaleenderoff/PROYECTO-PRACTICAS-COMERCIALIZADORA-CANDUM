package com.yerman.produccion_api.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ConsumoInsumo {

    private Long idConsumoInsumo;
    private Long idProduccion;
    private Long idDetalleProduccion;
    private Insumo insumo;
    private BigDecimal cantidadConsumida;
    private String observaciones;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ConsumoInsumo() {
    }

    public ConsumoInsumo(Long idConsumoInsumo, Long idProduccion, Long idDetalleProduccion,
            Insumo insumo, BigDecimal cantidadConsumida, String observaciones,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idConsumoInsumo = idConsumoInsumo;
        this.idProduccion = idProduccion;
        this.idDetalleProduccion = idDetalleProduccion;
        this.insumo = insumo;
        this.cantidadConsumida = cantidadConsumida;
        this.observaciones = observaciones;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getIdConsumoInsumo() {
        return idConsumoInsumo;
    }

    public void setIdConsumoInsumo(Long idConsumoInsumo) {
        this.idConsumoInsumo = idConsumoInsumo;
    }

    public Long getIdProduccion() {
        return idProduccion;
    }

    public void setIdProduccion(Long idProduccion) {
        this.idProduccion = idProduccion;
    }

    public Long getIdDetalleProduccion() {
        return idDetalleProduccion;
    }

    public void setIdDetalleProduccion(Long idDetalleProduccion) {
        this.idDetalleProduccion = idDetalleProduccion;
    }

    public Insumo getInsumo() {
        return insumo;
    }

    public void setInsumo(Insumo insumo) {
        this.insumo = insumo;
    }

    public BigDecimal getCantidadConsumida() {
        return cantidadConsumida;
    }

    public void setCantidadConsumida(BigDecimal cantidadConsumida) {
        this.cantidadConsumida = cantidadConsumida;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}