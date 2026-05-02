package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.domain.model.ProductoTerminadoLacteo;
import com.yerman.produccion_api.infrastructure.entity.ProductoTerminadoLacteoEntity;

public class ProductoTerminadoLacteoMapper {

    private ProductoTerminadoLacteoMapper() {
    }

    public static ProductoTerminadoLacteo toDomain(ProductoTerminadoLacteoEntity entity) {
        if (entity == null) {
            return null;
        }

        return new ProductoTerminadoLacteo(
                entity.getId(),
                entity.getProduccionLacteaBatch() != null ? entity.getProduccionLacteaBatch().getId() : null,
                entity.getProducto(),
                entity.getLote(),
                entity.getKilosProducidos(),
                entity.getKilosDisponibles(),
                entity.getEstado(),
                entity.getObservaciones());
    }
}