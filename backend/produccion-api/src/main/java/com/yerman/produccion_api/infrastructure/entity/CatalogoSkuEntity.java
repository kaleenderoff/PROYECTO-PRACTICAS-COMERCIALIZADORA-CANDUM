package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "catalogo_sku")
public class CatalogoSkuEntity {

    public enum TipoEnvase {
        DISPENSADOR, DOYPACK, GARRAFA, BALDE, TAZA, TETERO, BIPACK, BOLSA, OTRO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_sku", nullable = false, length = 30, unique = true)
    private String codigoSku;

    @Column(nullable = false, length = 200)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_producto", nullable = false)
    private CatalogoProductoEntity producto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_marca", nullable = false)
    private MarcaEntity marca;

    @Column(name = "peso_neto_gr", nullable = false)
    private Integer pesoNetoGr;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_envase", nullable = false, length = 20)
    private TipoEnvase tipoEnvase = TipoEnvase.OTRO;

    @Column(name = "unidades_por_caja")
    private Integer unidadesPorCaja;

    @Column(name = "es_export", nullable = false)
    private Boolean esExport = false;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (activo == null) {
            activo = true;
        }
        if (esExport == null) {
            esExport = false;
        }
        if (tipoEnvase == null) {
            tipoEnvase = TipoEnvase.OTRO;
        }
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
