package com.yerman.produccion_api.infrastructure.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "formula_version")
public class FormulaVersionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public FormulaVersionEntity() {
    }

    public Long getId() {
        return id;
    }
}