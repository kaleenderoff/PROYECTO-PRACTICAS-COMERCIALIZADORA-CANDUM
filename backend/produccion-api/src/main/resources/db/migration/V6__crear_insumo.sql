CREATE TABLE insumo (
  id BIGINT NOT NULL AUTO_INCREMENT,
  codigo VARCHAR(50) NULL,
  nombre VARCHAR(150) NOT NULL,
  descripcion VARCHAR(255) NULL,
  tipo ENUM('MATERIA_PRIMA', 'MATERIAL_EMPAQUE', 'ADITIVO', 'OTRO') NOT NULL DEFAULT 'MATERIA_PRIMA',
  unidad_medida VARCHAR(50) NOT NULL,
  stock_minimo DECIMAL(14,3) NULL,
  id_proveedor BIGINT NULL,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_insumo_codigo (codigo),
  UNIQUE KEY uq_insumo_nombre (nombre),
  KEY idx_insumo_proveedor (id_proveedor),
  CONSTRAINT fk_insumo_proveedor
    FOREIGN KEY (id_proveedor) REFERENCES proveedor (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
