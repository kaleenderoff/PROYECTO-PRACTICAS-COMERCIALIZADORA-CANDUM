CREATE TABLE orden_produccion_detalle (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_orden BIGINT NOT NULL,
  id_programacion_sku BIGINT NULL,
  id_sku BIGINT NOT NULL,
  cantidad_programada DECIMAL(14,3) NOT NULL,
  unidad_programada VARCHAR(50) NOT NULL DEFAULT 'UNIDADES',
  prioridad INT NOT NULL DEFAULT 1,
  observaciones VARCHAR(500) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_orden_detalle_sku (id_orden, id_sku),
  KEY idx_orden_detalle_programacion_sku (id_programacion_sku),
  KEY idx_orden_detalle_sku (id_sku),
  CONSTRAINT fk_orden_detalle_orden
    FOREIGN KEY (id_orden) REFERENCES orden_produccion (id) ON DELETE CASCADE,
  CONSTRAINT fk_orden_detalle_programacion_sku
    FOREIGN KEY (id_programacion_sku) REFERENCES programacion_sku (id),
  CONSTRAINT fk_orden_detalle_sku
    FOREIGN KEY (id_sku) REFERENCES catalogo_sku (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
