package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "control_peso_muestra")
public class ControlPesoMuestraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_control_peso_producto", nullable = false)
    private ControlPesoProductoEntity controlPesoProducto;

    @Column(name = "numero_muestra", nullable = false)
    private Integer numeroMuestra;

    @Column(name = "peso_bruto")
    private BigDecimal pesoBruto;

    private BigDecimal tara;

    @Column(name = "peso_neto", nullable = false)
    private BigDecimal pesoNeto;
}
