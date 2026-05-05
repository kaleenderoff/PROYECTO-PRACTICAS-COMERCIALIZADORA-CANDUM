package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "empaque_lacteo")
public class EmpaqueLacteoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_producto_terminado_lacteo")
    private ProductoTerminadoLacteoEntity productoTerminadoLacteo;

    @ManyToOne
    @JoinColumn(name = "id_produccion_lactea_batch", nullable = false)
    private ProduccionLacteaBatchEntity batch;

    private String loteEmpaque;
    private LocalDate fechaEmpaque;
    private LocalDate fechaVencimiento;

    private BigDecimal kilosUtilizados;
    private Integer unidades;
    private BigDecimal cajas;
    private BigDecimal pesoTotalKg;

    private String estado;
    private String observaciones;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public EmpaqueLacteoEntity() {
    }

    public Long getId() {
        return id;
    }

    public ProductoTerminadoLacteoEntity getProductoTerminadoLacteo() {
        return productoTerminadoLacteo;
    }

    public ProduccionLacteaBatchEntity getBatch() {
        return batch;
    }

    public String getLoteEmpaque() {
        return loteEmpaque;
    }

    public LocalDate getFechaEmpaque() {
        return fechaEmpaque;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public BigDecimal getKilosUtilizados() {
        return kilosUtilizados;
    }

    public Integer getUnidades() {
        return unidades;
    }

    public BigDecimal getCajas() {
        return cajas;
    }

    public BigDecimal getPesoTotalKg() {
        return pesoTotalKg;
    }

    public String getEstado() {
        return estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductoTerminadoLacteo(ProductoTerminadoLacteoEntity v) {
        this.productoTerminadoLacteo = v;
    }

    public void setBatch(ProduccionLacteaBatchEntity v) {
        this.batch = v;
    }

    public void setLoteEmpaque(String v) {
        this.loteEmpaque = v;
    }

    public void setFechaEmpaque(LocalDate v) {
        this.fechaEmpaque = v;
    }

    public void setFechaVencimiento(LocalDate v) {
        this.fechaVencimiento = v;
    }

    public void setKilosUtilizados(BigDecimal v) {
        this.kilosUtilizados = v;
    }

    public void setUnidades(Integer v) {
        this.unidades = v;
    }

    public void setCajas(BigDecimal v) {
        this.cajas = v;
    }

    public void setPesoTotalKg(BigDecimal v) {
        this.pesoTotalKg = v;
    }

    public void setEstado(String v) {
        this.estado = v;
    }

    public void setObservaciones(String v) {
        this.observaciones = v;
    }
}