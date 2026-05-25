package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "descremado_recepcion",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_descremado_recepcion_lote_crema", columnNames = "lote_crema")
        })
public class DescremadoRecepcionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_recepcion_leche", nullable = false)
    private RecepcionLecheEntity recepcionLeche;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tanque_destino")
    private TanqueLecheEntity tanqueDestino;

    @Column(name = "litros_descremados", nullable = false, precision = 14, scale = 3)
    private BigDecimal litrosDescremados;

    @Column(name = "crema_obtenida_kg", precision = 14, scale = 3)
    private BigDecimal cremaObtenidaKg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sku_crema")
    private CatalogoSkuEntity skuCrema;

    @Column(name = "unidades_crema")
    private Integer unidadesCrema;

    @Column(name = "kg_por_unidad_crema", precision = 14, scale = 3)
    private BigDecimal kgPorUnidadCrema;

    @Column(name = "lote_crema", length = 80)
    private String loteCrema;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_movimiento_salida")
    private MovimientoLecheEntity movimientoSalida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_movimiento_entrada")
    private MovimientoLecheEntity movimientoEntrada;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public DescremadoRecepcionEntity() {
    }

    public Long getId() {
        return id;
    }

    public RecepcionLecheEntity getRecepcionLeche() {
        return recepcionLeche;
    }

    public TanqueLecheEntity getTanqueDestino() {
        return tanqueDestino;
    }

    public BigDecimal getLitrosDescremados() {
        return litrosDescremados;
    }

    public BigDecimal getCremaObtenidaKg() {
        return cremaObtenidaKg;
    }

    public CatalogoSkuEntity getSkuCrema() {
        return skuCrema;
    }

    public Integer getUnidadesCrema() {
        return unidadesCrema;
    }

    public BigDecimal getKgPorUnidadCrema() {
        return kgPorUnidadCrema;
    }

    public String getLoteCrema() {
        return loteCrema;
    }

    public MovimientoLecheEntity getMovimientoSalida() {
        return movimientoSalida;
    }

    public MovimientoLecheEntity getMovimientoEntrada() {
        return movimientoEntrada;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRecepcionLeche(RecepcionLecheEntity recepcionLeche) {
        this.recepcionLeche = recepcionLeche;
    }

    public void setTanqueDestino(TanqueLecheEntity tanqueDestino) {
        this.tanqueDestino = tanqueDestino;
    }

    public void setLitrosDescremados(BigDecimal litrosDescremados) {
        this.litrosDescremados = litrosDescremados;
    }

    public void setCremaObtenidaKg(BigDecimal cremaObtenidaKg) {
        this.cremaObtenidaKg = cremaObtenidaKg;
    }

    public void setSkuCrema(CatalogoSkuEntity skuCrema) {
        this.skuCrema = skuCrema;
    }

    public void setUnidadesCrema(Integer unidadesCrema) {
        this.unidadesCrema = unidadesCrema;
    }

    public void setKgPorUnidadCrema(BigDecimal kgPorUnidadCrema) {
        this.kgPorUnidadCrema = kgPorUnidadCrema;
    }

    public void setLoteCrema(String loteCrema) {
        this.loteCrema = loteCrema;
    }

    public void setMovimientoSalida(MovimientoLecheEntity movimientoSalida) {
        this.movimientoSalida = movimientoSalida;
    }

    public void setMovimientoEntrada(MovimientoLecheEntity movimientoEntrada) {
        this.movimientoEntrada = movimientoEntrada;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
