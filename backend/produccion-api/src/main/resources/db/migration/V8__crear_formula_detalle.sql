CREATE TABLE formula_detalle (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_formula_version BIGINT NOT NULL,
  id_insumo BIGINT NOT NULL,
  cantidad_kg DECIMAL(14,6) NOT NULL,
  porcentaje DECIMAL(10,6) NULL,
  es_critico TINYINT(1) NOT NULL DEFAULT 0,
  orden_adicion INT NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_formula_detalle_insumo (id_formula_version, id_insumo),
  KEY idx_formula_detalle_insumo (id_insumo),
  CONSTRAINT fk_formula_detalle_version
    FOREIGN KEY (id_formula_version) REFERENCES formula_version (id) ON DELETE CASCADE,
  CONSTRAINT fk_formula_detalle_insumo
    FOREIGN KEY (id_insumo) REFERENCES insumo (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
