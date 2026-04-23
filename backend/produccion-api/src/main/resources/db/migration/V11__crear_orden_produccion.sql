CREATE TABLE orden_produccion (
  id BIGINT NOT NULL AUTO_INCREMENT,
  numero_orden VARCHAR(40) NOT NULL,
  id_programacion BIGINT NOT NULL,
  id_linea BIGINT NOT NULL,
  id_producto BIGINT NOT NULL,
  id_turno BIGINT NOT NULL,
  id_jefe_linea_ejecutor BIGINT NOT NULL,
  id_creada_por BIGINT NOT NULL,
  fecha_produccion DATE NOT NULL,
  estado ENUM('PROGRAMADA', 'EN_EJECUCION', 'FINALIZADA', 'CERRADA', 'CANCELADA') NOT NULL DEFAULT 'PROGRAMADA',
  observaciones VARCHAR(500) NULL,
  fecha_inicio_real DATETIME NULL,
  fecha_fin_real DATETIME NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_orden_numero (numero_orden),
  KEY idx_orden_programacion (id_programacion),
  KEY idx_orden_linea (id_linea),
  KEY idx_orden_producto (id_producto),
  KEY idx_orden_turno (id_turno),
  KEY idx_orden_jefe_linea (id_jefe_linea_ejecutor),
  KEY idx_orden_creada_por (id_creada_por),
  KEY idx_orden_estado_fecha (estado, fecha_produccion),
  CONSTRAINT fk_orden_programacion
    FOREIGN KEY (id_programacion) REFERENCES programacion_produccion (id),
  CONSTRAINT fk_orden_linea
    FOREIGN KEY (id_linea) REFERENCES catalogo_linea (id),
  CONSTRAINT fk_orden_producto
    FOREIGN KEY (id_producto) REFERENCES catalogo_producto (id),
  CONSTRAINT fk_orden_turno
    FOREIGN KEY (id_turno) REFERENCES turno (id),
  CONSTRAINT fk_orden_jefe_linea
    FOREIGN KEY (id_jefe_linea_ejecutor) REFERENCES usuario (id_usuario),
  CONSTRAINT fk_orden_creada_por
    FOREIGN KEY (id_creada_por) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
