CREATE TABLE registro_insumo_lacteo (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_produccion_lactea BIGINT NOT NULL,
  id_produccion_lactea_batch BIGINT NULL,
  id_insumo BIGINT NOT NULL,
  lote_insumo VARCHAR(100) NULL,
  cantidad_requerida DECIMAL(14,3) NULL,
  cantidad_usada DECIMAL(14,3) NOT NULL,
  unidad_medida VARCHAR(50) NOT NULL,
  fecha_hora_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  id_usuario BIGINT NOT NULL,
  observaciones VARCHAR(500) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (id),

  KEY idx_registro_insumo_lacteo_produccion (id_produccion_lactea),
  KEY idx_registro_insumo_lacteo_batch (id_produccion_lactea_batch),
  KEY idx_registro_insumo_lacteo_insumo (id_insumo),
  KEY idx_registro_insumo_lacteo_usuario (id_usuario),
  KEY idx_registro_insumo_lacteo_fecha (fecha_hora_registro),
  KEY idx_registro_insumo_lacteo_prod_insumo (id_produccion_lactea, id_insumo),

  CONSTRAINT fk_registro_insumo_lacteo_produccion
    FOREIGN KEY (id_produccion_lactea)
    REFERENCES produccion_lactea(id)
    ON DELETE CASCADE,

  CONSTRAINT fk_registro_insumo_lacteo_batch
    FOREIGN KEY (id_produccion_lactea_batch)
    REFERENCES produccion_lactea_batch(id),

  CONSTRAINT fk_registro_insumo_lacteo_insumo
    FOREIGN KEY (id_insumo)
    REFERENCES insumo(id),

  CONSTRAINT fk_registro_insumo_lacteo_usuario
    FOREIGN KEY (id_usuario)
    REFERENCES usuario(id_usuario),

  CONSTRAINT chk_registro_insumo_lacteo_cantidad_usada
    CHECK (cantidad_usada > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
