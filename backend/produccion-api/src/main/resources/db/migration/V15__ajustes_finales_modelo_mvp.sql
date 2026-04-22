-- V15: ajustes finales de consistencia del modelo MVP.
-- Mantiene el diseno sin legacy y refuerza reglas de negocio e indices utiles.

-- =========================================================
-- INSUMOS: timestamps consistentes con el resto del modelo.
-- =========================================================

ALTER TABLE insumo
  MODIFY created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  MODIFY updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

-- =========================================================
-- FORMULAS: dejar una sola observacion tecnica.
-- =========================================================

ALTER TABLE formula_version
  DROP COLUMN observaciones;

-- =========================================================
-- PROGRAMACION: permitir mas de un producto por linea/turno/dia.
-- =========================================================

ALTER TABLE programacion_produccion
  DROP INDEX uq_programacion_linea_turno_fecha,
  ADD UNIQUE KEY uq_programacion_linea_turno_fecha_producto
    (fecha_produccion, id_linea, id_turno, id_producto);

ALTER TABLE programacion_sku
  ADD KEY idx_programacion_sku_programacion (id_programacion);

-- =========================================================
-- ORDEN: siempre nace de una programacion.
-- =========================================================

ALTER TABLE orden_produccion
  MODIFY id_programacion BIGINT NOT NULL;

-- =========================================================
-- EJECUCION: el turno se lee desde la orden/programacion.
-- =========================================================

ALTER TABLE ejecucion_produccion
  DROP FOREIGN KEY fk_ejecucion_turno;

ALTER TABLE ejecucion_produccion
  DROP INDEX idx_ejecucion_turno;

ALTER TABLE ejecucion_produccion
  DROP COLUMN id_turno;

-- =========================================================
-- CONSUMO REPORTADO: indice para reportes por ejecucion/insumo.
-- =========================================================

ALTER TABLE registro_insumo
  ADD KEY idx_registro_insumo_ejecucion_insumo (id_ejecucion, id_insumo);

-- =========================================================
-- PRODUCCION REAL: evitar duplicar SKU en el mismo cierre.
-- =========================================================

ALTER TABLE produccion_real
  ADD KEY idx_produccion_real_estado (estado);

ALTER TABLE produccion_real_sku
  ADD UNIQUE KEY uq_produccion_real_sku (id_produccion_real, id_sku);

-- =========================================================
-- VALIDACION: toda validacion debe tener fecha.
-- =========================================================

ALTER TABLE validacion_produccion
  MODIFY fecha_validacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;
