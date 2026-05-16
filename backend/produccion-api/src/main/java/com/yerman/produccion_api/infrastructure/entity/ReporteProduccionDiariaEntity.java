package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reporte_produccion_diaria")
@Getter
@Setter
public class ReporteProduccionDiariaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "sku_descripcion", nullable = false, length = 200)
    private String skuDescripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_producto", nullable = false)
    private TipoProducto tipoProducto;

    @Column(name = "peso_neto_gr", nullable = false)
    private Integer pesoNetoGr;

    @Column(name = "unidades_reales", nullable = false)
    private Integer unidadesReales;

    @Column(name = "kg_pt_reales", nullable = false, precision = 14, scale = 3)
    private BigDecimal kgPtReales;

    @Column(length = 50)
    private String fuente;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum TipoProducto {
        LC, DL, OTRO
    }
}
