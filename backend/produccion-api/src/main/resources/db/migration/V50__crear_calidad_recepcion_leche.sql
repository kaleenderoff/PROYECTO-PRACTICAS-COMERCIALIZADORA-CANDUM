CREATE TABLE calidad_recepcion_leche (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_recepcion_leche BIGINT NOT NULL,
  fecha_control DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  prueba_alcohol_ok TINYINT(1) NULL,
  lactoscan_ok TINYINT(1) NULL,
  acidez DECIMAL(8,4) NULL,
  densidad DECIMAL(8,4) NULL,
  grasa DECIMAL(8,4) NULL,
  agua_pct DECIMAL(8,4) NULL,
  temperatura DECIMAL(6,2) NULL,
  ph DECIMAL(5,2) NULL,
  aprobado TINYINT(1) NOT NULL DEFAULT 1,
  retenido TINYINT(1) NOT NULL DEFAULT 0,
  id_realizado_por BIGINT NOT NULL,
  observaciones VARCHAR(500) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_calidad_recepcion_recepcion (id_recepcion_leche),
  KEY idx_calidad_recepcion_fecha (fecha_control),
  KEY idx_calidad_recepcion_realizado_por (id_realizado_por),
  CONSTRAINT fk_calidad_recepcion_recepcion
    FOREIGN KEY (id_recepcion_leche) REFERENCES recepcion_leche(id) ON DELETE CASCADE,
  CONSTRAINT fk_calidad_recepcion_realizado_por
    FOREIGN KEY (id_realizado_por) REFERENCES usuario(id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
