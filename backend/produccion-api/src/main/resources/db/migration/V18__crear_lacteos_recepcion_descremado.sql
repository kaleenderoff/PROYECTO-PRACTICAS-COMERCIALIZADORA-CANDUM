CREATE TABLE recepcion_leche (
  id BIGINT NOT NULL AUTO_INCREMENT,
  fecha_recepcion DATE NOT NULL,
  id_proveedor BIGINT NOT NULL,
  litros_segun_remision DECIMAL(10,2) NULL,
  litros_recibidos_real DECIMAL(10,2) NOT NULL,
  kg_brutos DECIMAL(10,2) NULL,
  kg_tara DECIMAL(10,2) NULL,
  kg_netos DECIMAL(10,2) NULL,
  pct_grasa_antes_descremado DECIMAL(6,4) NULL,
  temperatura_llegada DECIMAL(4,1) NULL,
  numero_remision_proveedor VARCHAR(50) NULL,
  vehiculo_placa VARCHAR(20) NULL,
  conductor VARCHAR(100) NULL,
  hora_llegada TIME NULL,
  id_registrado_por BIGINT NOT NULL,
  observaciones TEXT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_recepcion_fecha (fecha_recepcion),
  KEY idx_recepcion_proveedor (id_proveedor),
  KEY idx_recepcion_registrado_por (id_registrado_por),
  CONSTRAINT fk_recepcion_proveedor
    FOREIGN KEY (id_proveedor) REFERENCES proveedor (id),
  CONSTRAINT fk_recepcion_registrado_por
    FOREIGN KEY (id_registrado_por) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE descremado (
  id BIGINT NOT NULL AUTO_INCREMENT,
  fecha_descremado DATE NOT NULL,
  litros_procesados DECIMAL(10,2) NOT NULL,
  litros_entran_produccion DECIMAL(10,2) NULL,
  pct_grasa_antes DECIMAL(6,4) NULL,
  pct_grasa_despues DECIMAL(6,4) NULL,
  grasa_total_kg DECIMAL(10,4) NULL,
  grasa_despues_kg DECIMAL(10,4) NULL,
  diferencia_grasa_kg DECIMAL(10,4) NULL,
  bolsas_crema DECIMAL(5,1) NULL,
  kg_crema_real DECIMAL(10,2) NULL,
  kg_crema_esperado DECIMAL(10,2) NULL,
  id_registrado_por BIGINT NOT NULL,
  observaciones TEXT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_descremado_fecha (fecha_descremado),
  KEY idx_descremado_registrado_por (id_registrado_por),
  CONSTRAINT fk_descremado_registrado_por
    FOREIGN KEY (id_registrado_por) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE descremado_recepcion (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_descremado BIGINT NOT NULL,
  id_recepcion BIGINT NOT NULL,
  litros_aportados DECIMAL(10,2) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_descremado_recepcion (id_descremado, id_recepcion),
  KEY idx_descremado_recepcion_recepcion (id_recepcion),
  CONSTRAINT fk_descremado_recepcion_descremado
    FOREIGN KEY (id_descremado) REFERENCES descremado (id) ON DELETE CASCADE,
  CONSTRAINT fk_descremado_recepcion_recepcion
    FOREIGN KEY (id_recepcion) REFERENCES recepcion_leche (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE subproducto_produccion (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_descremado BIGINT NOT NULL,
  id_sku BIGINT NULL,
  cantidad DECIMAL(10,2) NOT NULL,
  unidad ENUM('BOLSAS', 'KG') NOT NULL DEFAULT 'KG',
  lote VARCHAR(50) NULL,
  fecha_produccion DATE NULL,
  observaciones TEXT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_subproducto_descremado (id_descremado),
  KEY idx_subproducto_sku (id_sku),
  CONSTRAINT fk_subproducto_descremado
    FOREIGN KEY (id_descremado) REFERENCES descremado (id) ON DELETE CASCADE,
  CONSTRAINT fk_subproducto_sku
    FOREIGN KEY (id_sku) REFERENCES catalogo_sku (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
