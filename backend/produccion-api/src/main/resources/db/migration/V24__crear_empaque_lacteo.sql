CREATE TABLE empaque_lacteo (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_producto_terminado_lacteo BIGINT NOT NULL,
  lote_empaque VARCHAR(80) NOT NULL,
  fecha_empaque DATE NOT NULL,
  fecha_vencimiento DATE DEFAULT NULL,

  kilos_utilizados DECIMAL(14,3) NOT NULL,
  unidades INT NOT NULL,
  cajas DECIMAL(14,3) DEFAULT NULL,
  peso_total_kg DECIMAL(14,3) NOT NULL,

  estado VARCHAR(50) NOT NULL DEFAULT 'REGISTRADO',
  observaciones VARCHAR(500) DEFAULT NULL,

  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (id),

  KEY idx_empaque_producto_terminado (id_producto_terminado_lacteo),
  KEY idx_empaque_lote (lote_empaque),
  KEY idx_empaque_fecha (fecha_empaque),
  KEY idx_empaque_estado (estado),

  CONSTRAINT fk_empaque_producto_terminado
    FOREIGN KEY (id_producto_terminado_lacteo)
    REFERENCES producto_terminado_lacteo(id),

  CONSTRAINT chk_empaque_kilos_utilizados
    CHECK (kilos_utilizados > 0),

  CONSTRAINT chk_empaque_unidades
    CHECK (unidades > 0),

  CONSTRAINT chk_empaque_peso_total
    CHECK (peso_total_kg > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;