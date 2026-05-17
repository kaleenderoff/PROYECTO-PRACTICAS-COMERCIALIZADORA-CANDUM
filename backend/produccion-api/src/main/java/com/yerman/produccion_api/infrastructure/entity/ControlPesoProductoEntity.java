package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "control_peso_producto")
public class ControlPesoProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orden_produccion", nullable = false)
    private OrdenProduccionEntity ordenProduccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ejecucion_batch")
    private EjecucionBatchEntity ejecucionBatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sku")
    private CatalogoSkuEntity sku;

    @Column(name = "fecha_control", nullable = false)
    private LocalDate fechaControl;

    @Column(length = 150)
    private String producto;

    @Column(length = 120)
    private String marca;

    @Column(length = 80)
    private String lote;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(length = 120)
    private String presentacion;

    @Column(name = "numero_tanda", length = 40)
    private String numeroTanda;

    @Column(name = "rango_batches", length = 80)
    private String rangoBatches;

    @Column(name = "peso_bruto_promedio")
    private BigDecimal pesoBrutoPromedio;

    @Column(name = "tara_promedio")
    private BigDecimal taraPromedio;

    @Column(name = "peso_neto_promedio")
    private BigDecimal pesoNetoPromedio;

    @Column(name = "apariencia_ok")
    private Boolean aparienciaOk;

    @Column(name = "etiquetado_ok")
    private Boolean etiquetadoOk;

    @Column(name = "tapado_ok")
    private Boolean tapadoOk;

    @Column(name = "cantidad_por_caja")
    private Integer cantidadPorCaja;

    private Boolean liberado = false;
    private Boolean retenido = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_realizado_por", nullable = false)
    private UsuarioEntity realizadoPor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_verificado_por")
    private UsuarioEntity verificadoPor;

    @Column(length = 500)
    private String observaciones;

    @OneToMany(mappedBy = "controlPesoProducto", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("numeroMuestra ASC")
    private List<ControlPesoMuestraEntity> muestras = new ArrayList<>();

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
