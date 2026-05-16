CREATE TABLE programacion_produccion (
  id BIGINT NOT NULL AUTO_INCREMENT,
  codigo_programacion VARCHAR(40) NOT NULL,
  fecha_produccion DATE NOT NULL,
  id_linea BIGINT NOT NULL,
  id_producto BIGINT NOT NULL,
  id_turno BIGINT NOT NULL,
  num_baches_plan INT NULL,
  kg_bache_plan DECIMAL(10,2) NULL,
  id_formula_version BIGINT NOT NULL,
  id_jefe_produccion BIGINT NOT NULL,
  estado ENUM('BORRADOR', 'CONFIRMADA', 'CON_ORDEN', 'CANCELADA') NOT NULL DEFAULT 'BORRADOR',
  observaciones VARCHAR(500) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_programacion_codigo (codigo_programacion),
  UNIQUE KEY uq_programacion_linea_turno_fecha_producto (fecha_produccion, id_linea, id_turno, id_producto),
  KEY idx_programacion_linea (id_linea),
  KEY idx_programacion_producto (id_producto),
  KEY idx_programacion_turno (id_turno),
  KEY idx_programacion_formula_version (id_formula_version),
  KEY idx_programacion_jefe (id_jefe_produccion),
  KEY idx_programacion_estado_fecha (estado, fecha_produccion),
  CONSTRAINT fk_programacion_linea
    FOREIGN KEY (id_linea) REFERENCES catalogo_linea (id),
  CONSTRAINT fk_programacion_producto
    FOREIGN KEY (id_producto) REFERENCES catalogo_producto (id),
  CONSTRAINT fk_programacion_turno
    FOREIGN KEY (id_turno) REFERENCES turno (id),
  CONSTRAINT fk_programacion_formula_version
    FOREIGN KEY (id_formula_version) REFERENCES formula_version (id),
  CONSTRAINT fk_programacion_jefe
    FOREIGN KEY (id_jefe_produccion) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
