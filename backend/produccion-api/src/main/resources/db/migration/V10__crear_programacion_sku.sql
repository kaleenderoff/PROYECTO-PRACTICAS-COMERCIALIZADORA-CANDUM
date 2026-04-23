CREATE TABLE programacion_sku (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_programacion BIGINT NOT NULL,
  id_sku BIGINT NOT NULL,
  unidades_objetivo INT NOT NULL,
  observaciones VARCHAR(500) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_programacion_sku (id_programacion, id_sku),
  KEY idx_programacion_sku_programacion (id_programacion),
  KEY idx_programacion_sku_sku (id_sku),
  CONSTRAINT fk_programacion_sku_programacion
    FOREIGN KEY (id_programacion) REFERENCES programacion_produccion (id) ON DELETE CASCADE,
  CONSTRAINT fk_programacion_sku_sku
    FOREIGN KEY (id_sku) REFERENCES catalogo_sku (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
