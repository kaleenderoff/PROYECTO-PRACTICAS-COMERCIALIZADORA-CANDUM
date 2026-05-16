ALTER TABLE catalogo_sku 
ADD COLUMN unidad_medida VARCHAR(10) NOT NULL DEFAULT 'gr' AFTER tipo_envase;
