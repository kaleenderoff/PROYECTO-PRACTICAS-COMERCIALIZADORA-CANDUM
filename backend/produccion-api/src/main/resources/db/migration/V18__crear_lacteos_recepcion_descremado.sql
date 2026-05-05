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
