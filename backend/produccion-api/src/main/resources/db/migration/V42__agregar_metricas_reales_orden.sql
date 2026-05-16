ALTER TABLE orden_produccion
ADD COLUMN kg_entrada_real DECIMAL(12,2),
ADD COLUMN kg_pt_real DECIMAL(12,2),
ADD COLUMN rendimiento_real DECIMAL(12,2),
ADD COLUMN merma_real DECIMAL(12,2);
