package com.yerman.produccion_api.infrastructure.entity;

import com.yerman.produccion_api.domain.model.TipoTanqueLeche;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tanque_leche", uniqueConstraints = {
        @UniqueConstraint(name = "uq_tanque_leche_nombre", columnNames = "nombre")
})
public class TanqueLecheEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoTanqueLeche tipo;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public TanqueLecheEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoTanqueLeche getTipo() {
        return tipo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public boolean isActivo() {
        return Boolean.TRUE.equals(activo);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(TipoTanqueLeche tipo) {
        this.tipo = tipo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}