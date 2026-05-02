CREATE TABLE producto_terminado_lacteo (
  id BIGINT NOT NULL AUTO_INCREMENT,

  id_produccion_lactea_batch BIGINT NOT NULL,

  producto VARCHAR(120) NOT NULL,
  lote VARCHAR(80) NULL,

  kilos_producidos DECIMAL(14,3) NOT NULL,
  kilos_disponibles DECIMAL(14,3) NOT NULL,

  estado VARCHAR(50) NOT NULL DEFAULT 'DISPONIBLE',

  observaciones VARCHAR(500) NULL,

  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (id),

  KEY idx_producto_terminado_batch (id_produccion_lactea_batch),
  KEY idx_producto_terminado_producto (producto),
  KEY idx_producto_terminado_lote (lote),
  KEY idx_producto_terminado_estado (estado),

  CONSTRAINT fk_producto_terminado_batch
    FOREIGN KEY (id_produccion_lactea_batch)
    REFERENCES produccion_lactea_batch(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;