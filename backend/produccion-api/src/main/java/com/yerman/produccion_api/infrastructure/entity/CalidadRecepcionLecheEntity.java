package com.yerman.produccion_api.infrastructure.entity;

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
@Table(name = "calidad_recepcion_leche")
public class CalidadRecepcionLecheEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_recepcion_leche", nullable = false)
    private RecepcionLecheEntity recepcionLeche;

    @Column(name = "fecha_control", nullable = false)
    private LocalDateTime fechaControl;

    @Column(name = "prueba_alcohol_ok")
    private Boolean pruebaAlcoholOk;

    @Column(name = "lactoscan_ok")
    private Boolean lactoscanOk;

    private BigDecimal acidez;
    private BigDecimal densidad;
    private BigDecimal grasa;

    @Column(name = "agua_pct")
    private BigDecimal aguaPct;

    private BigDecimal temperatura;
    private BigDecimal ph;

    private Boolean aprobado = true;
    private Boolean retenido = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_realizado_por", nullable = false)
    private UsuarioEntity realizadoPor;

    @Column(length = 500)
    private String observaciones;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
