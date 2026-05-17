package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "control_calidad_proceso")
public class ControlCalidadProcesoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orden_produccion", nullable = false)
    private OrdenProduccionEntity ordenProduccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ejecucion_batch")
    private EjecucionBatchEntity ejecucionBatch;

    @Column(name = "fecha_produccion", nullable = false)
    private LocalDate fechaProduccion;

    @Column(name = "tipo_producto", length = 120)
    private String tipoProducto;

    @Column(length = 150)
    private String producto;

    @Column(length = 80)
    private String lote;

    @Column(name = "numero_marmita")
    private Integer numeroMarmita;

    @Column(name = "producto_en_proceso", length = 150)
    private String productoEnProceso;

    @Column(name = "ph_leche")
    private BigDecimal phLeche;

    @Column(name = "acidez_leche")
    private BigDecimal acidezLeche;

    @Column(name = "densidad_leche")
    private BigDecimal densidadLeche;

    @Column(name = "grasa_leche")
    private BigDecimal grasaLeche;

    @Column(name = "hora_inicio_hidrolisis")
    private LocalTime horaInicioHidrolisis;

    @Column(name = "ph_inicial")
    private BigDecimal phInicial;

    @Column(name = "hora_fin_hidrolisis")
    private LocalTime horaFinHidrolisis;

    @Column(name = "temperatura_inicial")
    private BigDecimal temperaturaInicial;

    @Column(name = "temperatura_final")
    private BigDecimal temperaturaFinal;

    @Column(name = "acidez_inicial")
    private BigDecimal acidezInicial;

    @Column(name = "acidez_final")
    private BigDecimal acidezFinal;

    @Column(name = "ph_final")
    private BigDecimal phFinal;

    @Column(name = "brix_inicial")
    private BigDecimal brixInicial;

    @Column(name = "brix_final")
    private BigDecimal brixFinal;

    private BigDecimal presion;

    @Column(name = "temperatura_coccion")
    private BigDecimal temperaturaCoccion;

    @Column(name = "temperatura_envasado")
    private BigDecimal temperaturaEnvasado;

    @Column(name = "color_visual", length = 80)
    private String colorVisual;

    @Column(name = "sabor_visual", length = 80)
    private String saborVisual;

    @Column(name = "textura_visual", length = 80)
    private String texturaVisual;

    @Column(name = "presentacion_envasado", length = 120)
    private String presentacionEnvasado;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

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

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
