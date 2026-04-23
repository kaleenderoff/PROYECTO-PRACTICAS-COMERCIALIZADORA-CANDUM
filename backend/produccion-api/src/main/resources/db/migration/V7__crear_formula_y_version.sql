CREATE TABLE formula (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(150) NOT NULL,
  id_producto BIGINT NOT NULL,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_formula_producto_nombre (id_producto, nombre),
  KEY idx_formula_producto (id_producto),
  CONSTRAINT fk_formula_producto
    FOREIGN KEY (id_producto) REFERENCES catalogo_producto (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE formula_version (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_formula BIGINT NOT NULL,
  version VARCHAR(20) NOT NULL,
  fecha_inicio_vigencia DATE NOT NULL,
  fecha_fin_vigencia DATE NULL,
  kg_batch_total DECIMAL(10,2) NULL,
  reduccion_evaporacion_pct DECIMAL(6,4) NULL,
  rendimiento_teorico_pct DECIMAL(6,4) NULL,
  brix_objetivo_min DECIMAL(5,2) NULL,
  brix_objetivo_max DECIMAL(5,2) NULL,
  estado ENUM('BORRADOR', 'APROBADA', 'VIGENTE', 'REEMPLAZADA', 'INACTIVA') NOT NULL DEFAULT 'BORRADOR',
  aprobado_por VARCHAR(100) NULL,
  documento_aprobacion VARCHAR(500) NULL,
  observaciones_tecnicas TEXT NULL,
  id_creado_por BIGINT NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_formula_version (id_formula, version),
  KEY idx_formula_version_estado (estado),
  KEY idx_formula_version_creado_por (id_creado_por),
  CONSTRAINT fk_formula_version_formula
    FOREIGN KEY (id_formula) REFERENCES formula (id) ON DELETE CASCADE,
  CONSTRAINT fk_formula_version_creado_por
    FOREIGN KEY (id_creado_por) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
