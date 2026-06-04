-- El constraint UNIQUE actual bloquea incluso programaciones CANCELADAS,
-- impidiendo crear nuevas para la misma fecha/línea/turno/producto.
-- Solución: columna generada que solo tiene valor para programaciones activas.
-- MySQL excluye NULL de los índices únicos, permitiendo múltiples CANCELADAS.

ALTER TABLE programacion_produccion
    ADD COLUMN clave_unicidad VARCHAR(200) GENERATED ALWAYS AS (
        CASE WHEN estado != 'CANCELADA'
            THEN CONCAT(fecha_produccion, '_', id_linea, '_', id_turno, '_', id_producto)
            ELSE NULL END
    ) VIRTUAL;

ALTER TABLE programacion_produccion
    DROP INDEX uq_programacion_linea_turno_fecha_producto;

CREATE UNIQUE INDEX uq_programacion_activa
    ON programacion_produccion (clave_unicidad);
