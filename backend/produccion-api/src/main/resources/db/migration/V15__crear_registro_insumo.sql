CREATE TABLE registro_insumo (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_ejecucion BIGINT NOT NULL,
  id_orden_detalle BIGINT NULL,
  id_insumo BIGINT NOT NULL,
  lote_insumo VARCHAR(100) NULL,
  cantidad_requerida DECIMAL(14,3) NULL,
  cantidad_usada DECIMAL(14,3) NOT NULL,
  unidad_medida VARCHAR(50) NOT NULL,
  momento_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  observaciones VARCHAR(500) NULL,
  id_registrado_por BIGINT NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_registro_insumo_ejecucion (id_ejecucion),
  KEY idx_registro_insumo_orden_detalle (id_orden_detalle),
  KEY idx_registro_insumo_insumo (id_insumo),
  KEY idx_registro_insumo_ejecucion_insumo (id_ejecucion, id_insumo),
  KEY idx_registro_insumo_registrado_por (id_registrado_por),
  CONSTRAINT fk_registro_insumo_ejecucion
    FOREIGN KEY (id_ejecucion) REFERENCES ejecucion_produccion (id) ON DELETE CASCADE,
  CONSTRAINT fk_registro_insumo_orden_detalle
    FOREIGN KEY (id_orden_detalle) REFERENCES orden_produccion_detalle (id),
  CONSTRAINT fk_registro_insumo_insumo
    FOREIGN KEY (id_insumo) REFERENCES insumo (id),
  CONSTRAINT fk_registro_insumo_registrado_por
    FOREIGN KEY (id_registrado_por) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
