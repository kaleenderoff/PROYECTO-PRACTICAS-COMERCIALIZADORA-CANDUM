CREATE TABLE lote_produccion (
  id BIGINT NOT NULL AUTO_INCREMENT,
  codigo_lote VARCHAR(80) NOT NULL,
  id_ejecucion BIGINT NOT NULL,
  id_sku BIGINT NOT NULL,
  fecha_produccion DATE NOT NULL,
  fecha_vencimiento DATE NULL,
  estado ENUM('ACTIVO', 'EN_DISTRIBUCION', 'RETIRADO', 'VENCIDO') NOT NULL DEFAULT 'ACTIVO',
  observaciones VARCHAR(500) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_lote_codigo (codigo_lote),
  KEY idx_lote_ejecucion (id_ejecucion),
  KEY idx_lote_sku (id_sku),
  CONSTRAINT fk_lote_ejecucion
    FOREIGN KEY (id_ejecucion) REFERENCES ejecucion_produccion (id) ON DELETE CASCADE,
  CONSTRAINT fk_lote_sku
    FOREIGN KEY (id_sku) REFERENCES catalogo_sku (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE produccion_real (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_ejecucion BIGINT NOT NULL,
  kg_totales_reales DECIMAL(10,2) NULL,
  litros_leche_utilizados DECIMAL(10,2) NULL,
  rendimiento_real_1_pct DECIMAL(6,4) NULL,
  rendimiento_real_2_pct DECIMAL(6,4) NULL,
  estado ENUM('BORRADOR', 'REGISTRADA', 'VALIDADA', 'CERRADA') NOT NULL DEFAULT 'BORRADOR',
  observaciones_finales TEXT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_produccion_real_ejecucion (id_ejecucion),
  KEY idx_produccion_real_estado (estado),
  CONSTRAINT fk_produccion_real_ejecucion
    FOREIGN KEY (id_ejecucion) REFERENCES ejecucion_produccion (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE produccion_real_sku (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_produccion_real BIGINT NOT NULL,
  id_orden_detalle BIGINT NOT NULL,
  id_lote_produccion BIGINT NULL,
  id_sku BIGINT NOT NULL,
  id_registrado_por BIGINT NOT NULL,
  kg_programados DECIMAL(14,3) NULL,
  kg_reales DECIMAL(10,2) NULL,
  unidades_plan INT NULL,
  diferencia_unidades INT NULL,
  unidades_reales INT NOT NULL,
  cajas_reales DECIMAL(14,3) NULL,
  rendimiento_pct DECIMAL(6,2) NULL,
  fecha_hora_inicio DATETIME NULL,
  fecha_hora_fin DATETIME NULL,
  observaciones VARCHAR(500) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_produccion_real_sku (id_produccion_real, id_sku),
  KEY idx_produccion_real_sku_orden_detalle (id_orden_detalle),
  KEY idx_produccion_real_sku_lote (id_lote_produccion),
  KEY idx_produccion_real_sku_sku (id_sku),
  KEY idx_produccion_real_registrado_por (id_registrado_por),
  CONSTRAINT fk_produccion_real_sku_produccion_real
    FOREIGN KEY (id_produccion_real) REFERENCES produccion_real (id) ON DELETE CASCADE,
  CONSTRAINT fk_produccion_real_orden_detalle
    FOREIGN KEY (id_orden_detalle) REFERENCES orden_produccion_detalle (id) ON DELETE CASCADE,
  CONSTRAINT fk_produccion_real_sku_lote
    FOREIGN KEY (id_lote_produccion) REFERENCES lote_produccion (id),
  CONSTRAINT fk_produccion_real_sku_sku
    FOREIGN KEY (id_sku) REFERENCES catalogo_sku (id),
  CONSTRAINT fk_produccion_real_registrado_por
    FOREIGN KEY (id_registrado_por) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
