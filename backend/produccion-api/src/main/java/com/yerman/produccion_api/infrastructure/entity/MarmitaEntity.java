package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "marmita")
public class MarmitaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", nullable = false, unique = true)
    private Integer numero;

    @Column(name = "nombre", nullable = false, length = 80)
    private String nombre;

    @Column(name = "activa", nullable = false)
    private Boolean activa;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public MarmitaEntity() {
    }

    public Long getId() {
        return id;
    }

    public Integer getNumero() {
        return numero;
    }

    public String getNombre() {
        return nombre;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }
}