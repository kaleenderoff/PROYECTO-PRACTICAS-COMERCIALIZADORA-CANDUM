ALTER TABLE produccion_lactea
ADD COLUMN id_orden_produccion BIGINT NULL AFTER id;

ALTER TABLE produccion_lactea
ADD KEY idx_produccion_lactea_orden (id_orden_produccion);

ALTER TABLE produccion_lactea
ADD CONSTRAINT fk_produccion_lactea_orden
FOREIGN KEY (id_orden_produccion)
REFERENCES orden_produccion(id);
