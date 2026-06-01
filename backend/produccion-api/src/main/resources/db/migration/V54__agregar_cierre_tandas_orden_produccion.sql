ALTER TABLE orden_produccion
ADD COLUMN tandas_cerradas BOOLEAN NOT NULL DEFAULT FALSE,
ADD COLUMN fecha_cierre_tandas TIMESTAMP NULL,
ADD COLUMN id_usuario_cierre_tandas BIGINT NULL,
ADD COLUMN observaciones_cierre_tandas TEXT NULL;

ALTER TABLE orden_produccion
ADD CONSTRAINT fk_orden_produccion_usuario_cierre_tandas
FOREIGN KEY (id_usuario_cierre_tandas)
REFERENCES usuario(id_usuario);