CREATE TABLE validacion_orden_produccion (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_orden BIGINT NOT NULL,
  aprobado TINYINT(1) NOT NULL DEFAULT 0,
  id_jefe_produccion BIGINT NOT NULL,
  observacion VARCHAR(500) NULL,
  fecha_validacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  requiere_revision TINYINT(1) NOT NULL DEFAULT 0,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_validacion_orden_produccion (id_orden),
  KEY idx_validacion_orden_jefe (id_jefe_produccion),
  KEY idx_validacion_orden_fecha (fecha_validacion),
  CONSTRAINT fk_validacion_orden_orden
    FOREIGN KEY (id_orden) REFERENCES orden_produccion(id),
  CONSTRAINT fk_validacion_orden_jefe
    FOREIGN KEY (id_jefe_produccion) REFERENCES usuario(id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
