CREATE TABLE IF NOT EXISTS `reporte_produccion_diaria` (
  `id`               BIGINT NOT NULL AUTO_INCREMENT,
  `fecha`            DATE NOT NULL,
  `sku_descripcion`  VARCHAR(200) NOT NULL,
  `tipo_producto`    ENUM('LC','DL','OTRO') NOT NULL DEFAULT 'LC',
  `peso_neto_gr`     INT NOT NULL,
  `unidades_reales`  INT NOT NULL,
  `kg_pt_reales`     DECIMAL(14,3) NOT NULL,
  `fuente`           VARCHAR(50) DEFAULT 'HISTORICO_EXCEL',
  `created_at`       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_rpd_fecha`       (`fecha`),
  KEY `idx_rpd_tipo`        (`tipo_producto`),
  KEY `idx_rpd_fecha_tipo`  (`fecha`, `tipo_producto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
