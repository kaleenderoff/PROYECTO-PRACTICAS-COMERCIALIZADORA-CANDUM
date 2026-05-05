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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_producto_terminado_lacteo", nullable = false)
    private ProductoTerminadoLacteoEntity productoTerminadoLacteo;

    @Column(name = "lote_empaque", nullable = false, length = 80)
    private String loteEmpaque;

    @Column(name = "fecha_empaque", nullable = false)
    private LocalDate fechaEmpaque;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "kilos_utilizados", nullable = false, precision = 14, scale = 3)
    private BigDecimal kilosUtilizados;

    @Column(name = "unidades", nullable = false)
    private Integer unidades;

    @Column(name = "cajas", precision = 14, scale = 3)
    private BigDecimal cajas;

    @Column(name = "peso_total_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal pesoTotalKg;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public EmpaqueLacteoEntity() {
    }

    public Long getId() {
        return id;
    }

    public ProductoTerminadoLacteoEntity getProductoTerminadoLacteo() {
        return productoTerminadoLacteo;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductoTerminadoLacteo(ProductoTerminadoLacteoEntity productoTerminadoLacteo) {
        this.productoTerminadoLacteo = productoTerminadoLacteo;
    }

    public void setLoteEmpaque(String loteEmpaque) {
        this.loteEmpaque = loteEmpaque;
    }

    public void setFechaEmpaque(LocalDate fechaEmpaque) {
        this.fechaEmpaque = fechaEmpaque;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public void setKilosUtilizados(BigDecimal kilosUtilizados) {
        this.kilosUtilizados = kilosUtilizados;
    }

    public void setUnidades(Integer unidades) {
        this.unidades = unidades;
    }

    public void setCajas(BigDecimal cajas) {
        this.cajas = cajas;
    }

    public void setPesoTotalKg(BigDecimal pesoTotalKg) {
        this.pesoTotalKg = pesoTotalKg;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}