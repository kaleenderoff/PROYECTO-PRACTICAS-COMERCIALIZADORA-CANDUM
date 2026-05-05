CREATE TABLE medicion_calidad_lactea (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_produccion_lactea BIGINT NOT NULL,
  id_produccion_lactea_batch BIGINT NULL,
  tipo_medicion ENUM('BACHE', 'MEZCLA', 'TANDA') NOT NULL,
  referencia VARCHAR(80) NOT NULL,
  brix DECIMAL(5,2) NULL,
  ph DECIMAL(4,2) NULL,
  fecha_hora_medicion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  id_usuario_calidad BIGINT NOT NULL,
  observaciones VARCHAR(500) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (id),

  KEY idx_medicion_calidad_produccion (id_produccion_lactea),
  KEY idx_medicion_calidad_batch (id_produccion_lactea_batch),
  KEY idx_medicion_calidad_usuario (id_usuario_calidad),
  KEY idx_medicion_calidad_fecha (fecha_hora_medicion),
  KEY idx_medicion_calidad_tipo (tipo_medicion),

  CONSTRAINT fk_medicion_calidad_produccion
    FOREIGN KEY (id_produccion_lactea)
    REFERENCES produccion_lactea(id)
    ON DELETE CASCADE,

  CONSTRAINT fk_medicion_calidad_batch
    FOREIGN KEY (id_produccion_lactea_batch)
    REFERENCES produccion_lactea_batch(id),

  CONSTRAINT fk_medicion_calidad_usuario
    FOREIGN KEY (id_usuario_calidad)
    REFERENCES usuario(id_usuario),

  CONSTRAINT chk_medicion_calidad_valor
    CHECK (brix IS NOT NULL OR ph IS NOT NULL)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
