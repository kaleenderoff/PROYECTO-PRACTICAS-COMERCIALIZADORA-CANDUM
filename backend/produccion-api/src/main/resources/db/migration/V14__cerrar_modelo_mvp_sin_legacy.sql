-- V14: cierre del modelo MVP.
-- El sistema queda como produccion + consumo reportado, sin inventario operativo
-- ni tablas legacy como parte del esquema activo.

-- =========================================================
-- LEGACY: retirar tablas del flujo viejo.
-- =========================================================

DROP TABLE IF EXISTS validacion;
DROP TABLE IF EXISTS consumo_insumo;
DROP TABLE IF EXISTS empaque;
DROP TABLE IF EXISTS detalle_produccion;
DROP TABLE IF EXISTS producto_terminado;
DROP TABLE IF EXISTS produccion;
DROP TABLE IF EXISTS producto;
DROP TABLE IF EXISTS linea_produccion;

-- =========================================================
-- INVENTARIO OPERATIVO: retirar del MVP.
-- El consumo real queda en registro_insumo.
-- =========================================================

DROP TABLE IF EXISTS consumo_insumo_orden;
DROP TABLE IF EXISTS receta_sku_detalle;
DROP TABLE IF EXISTS receta_sku;
DROP TABLE IF EXISTS movimiento_inventario_insumo;
DROP TABLE IF EXISTS inventario_insumo;

-- =========================================================
-- INSUMOS: normalizar PK a id para el modelo nuevo.
-- =========================================================

ALTER TABLE formula_detalle
  DROP FOREIGN KEY fk_formula_detalle_insumo;

ALTER TABLE registro_insumo
  DROP FOREIGN KEY fk_registro_insumo_insumo;

ALTER TABLE insumo
  DROP FOREIGN KEY fk_insumo_proveedor;

ALTER TABLE insumo
  DROP PRIMARY KEY,
  CHANGE COLUMN id_insumo id BIGINT NOT NULL AUTO_INCREMENT,
  ADD PRIMARY KEY (id);

ALTER TABLE insumo
  ADD CONSTRAINT fk_insumo_proveedor
    FOREIGN KEY (id_proveedor) REFERENCES proveedor (id);

ALTER TABLE formula_detalle
  ADD CONSTRAINT fk_formula_detalle_insumo
    FOREIGN KEY (id_insumo) REFERENCES insumo (id);

ALTER TABLE registro_insumo
  ADD CONSTRAINT fk_registro_insumo_insumo
    FOREIGN KEY (id_insumo) REFERENCES insumo (id);

-- =========================================================
-- FORMULAS: cantidades en kg, sin unidad ambigua.
-- =========================================================

ALTER TABLE formula_detalle
  DROP COLUMN unidad_medida;

-- =========================================================
-- PROGRAMACION Y ORDENES: producto y formula obligatorios.
-- =========================================================

ALTER TABLE programacion_produccion
  MODIFY id_producto BIGINT NOT NULL,
  MODIFY id_formula_version BIGINT NOT NULL;

ALTER TABLE orden_produccion
  MODIFY id_producto BIGINT NOT NULL;

-- =========================================================
-- EJECUCION: evitar duplicidad fecha/hora.
-- =========================================================

ALTER TABLE ejecucion_produccion
  DROP COLUMN fecha_hora_inicio,
  DROP COLUMN fecha_hora_fin;

ALTER TABLE medicion_bache
  CHANGE COLUMN ph ph DECIMAL(4,2) NULL;

-- =========================================================
-- RESULTADO Y VALIDACION: relaciones obligatorias y 1:1.
-- =========================================================

ALTER TABLE produccion_real_sku
  MODIFY id_produccion_real BIGINT NOT NULL,
  MODIFY id_sku BIGINT NOT NULL;

ALTER TABLE validacion_produccion
  MODIFY id_ejecucion BIGINT NOT NULL,
  MODIFY id_produccion_real BIGINT NOT NULL,
  ADD UNIQUE KEY uq_validacion_produccion_real (id_produccion_real);

-- =========================================================
-- LACTEOS: fechas y origenes obligatorios.
-- =========================================================

UPDATE descremado
SET fecha_descremado = CURRENT_DATE
WHERE fecha_descremado IS NULL;

ALTER TABLE descremado
  MODIFY fecha_descremado DATE NOT NULL;

ALTER TABLE subproducto_produccion
  MODIFY id_descremado BIGINT NOT NULL;

-- =========================================================
-- USUARIO: alinear timestamps con el resto del modelo.
-- =========================================================

ALTER TABLE usuario
  MODIFY created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  MODIFY updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
