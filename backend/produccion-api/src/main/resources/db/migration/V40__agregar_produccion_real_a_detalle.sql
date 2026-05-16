ALTER TABLE orden_produccion_detalle 
ADD COLUMN cantidad_real DECIMAL(14,3) DEFAULT NULL,
ADD COLUMN unidades_reales INT DEFAULT NULL;
