ALTER TABLE empaque_lacteo
ADD COLUMN id_sku BIGINT NULL AFTER id_produccion_lactea_batch;

ALTER TABLE empaque_lacteo
ADD KEY idx_empaque_lacteo_sku (id_sku);

ALTER TABLE empaque_lacteo
ADD CONSTRAINT fk_empaque_lacteo_sku
FOREIGN KEY (id_sku)
REFERENCES catalogo_sku(id);
