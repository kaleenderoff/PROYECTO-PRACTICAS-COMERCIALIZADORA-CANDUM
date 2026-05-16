package com.yerman.produccion_api.infrastructure.entity;

import com.yerman.produccion_api.domain.model.EjecucionBatch.EstadoBatch;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ejecucion_batch")
public class EjecucionBatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orden_produccion", nullable = false)
    private OrdenProduccionEntity ordenProduccion;

    @Column(name = "numero_batch", nullable = false)
    private Integer numeroBatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_marmita", nullable = false)
    private MarmitaEntity marmita;

    @Column(name = "kg_entrada", nullable = false)
    private BigDecimal kgEntrada;

    @Column(name = "kg_producidos")
    private BigDecimal kgProducidos;

    @Column(name = "rendimiento_pct")
    private BigDecimal rendimientoPct;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoBatch estado = EstadoBatch.EN_PROCESO;

    @Column(length = 500)
    private String observaciones;

    @Column(name = "hubo_reproceso")
    private Boolean huboReproceso;

    @Column(name = "batch_conforme")
    private Boolean batchConforme;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (estado == null) estado = EstadoBatch.EN_PROCESO;
    }
}
