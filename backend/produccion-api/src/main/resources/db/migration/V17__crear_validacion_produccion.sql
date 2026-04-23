CREATE TABLE validacion_produccion (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_ejecucion BIGINT NOT NULL,
  id_produccion_real BIGINT NOT NULL,
  aprobado TINYINT(1) NOT NULL DEFAULT 0,
  id_jefe_produccion BIGINT NOT NULL,
  observacion VARCHAR(500) NULL,
  fecha_validacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  requiere_revision TINYINT(1) NOT NULL DEFAULT 0,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_validacion_ejecucion (id_ejecucion),
  UNIQUE KEY uq_validacion_produccion_real (id_produccion_real),
  KEY idx_validacion_jefe_produccion (id_jefe_produccion),
  CONSTRAINT fk_validacion_produccion_ejecucion
    FOREIGN KEY (id_ejecucion) REFERENCES ejecucion_produccion (id),
  CONSTRAINT fk_validacion_produccion_real
    FOREIGN KEY (id_produccion_real) REFERENCES produccion_real (id),
  CONSTRAINT fk_validacion_jefe_produccion
    FOREIGN KEY (id_jefe_produccion) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
