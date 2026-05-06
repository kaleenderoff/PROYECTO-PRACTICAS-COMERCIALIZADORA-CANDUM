ALTER TABLE descremado_recepcion
  ADD COLUMN id_sku_crema BIGINT NULL AFTER crema_obtenida_kg,
  ADD COLUMN unidades_crema INT NULL AFTER id_sku_crema,
  ADD COLUMN kg_por_unidad_crema DECIMAL(14,3) NULL AFTER unidades_crema,
  ADD COLUMN lote_crema VARCHAR(80) NULL AFTER kg_por_unidad_crema,
  ADD KEY idx_descremado_recepcion_sku_crema (id_sku_crema),
  ADD CONSTRAINT fk_descremado_recepcion_sku_crema
    FOREIGN KEY (id_sku_crema) REFERENCES catalogo_sku(id);
