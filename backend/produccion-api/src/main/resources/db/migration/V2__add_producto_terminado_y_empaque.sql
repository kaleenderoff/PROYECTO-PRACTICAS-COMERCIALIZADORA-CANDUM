-- =========================================
-- V2__add_producto_terminado_y_empaque.sql
-- =========================================

CREATE TABLE `producto_terminado` (
  `id_producto_terminado` bigint NOT NULL AUTO_INCREMENT,
  `id_producto` bigint NOT NULL,
  `sku` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre_comercial` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `referencia` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gramaje_g` decimal(10,2) NOT NULL,
  `unidad_medida` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `embalaje` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id_producto_terminado`),
  UNIQUE KEY `uq_producto_terminado_sku` (`sku`),
  KEY `idx_producto_terminado_producto` (`id_producto`),
  CONSTRAINT `fk_producto_terminado_producto`
    FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id_producto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `empaque` (
  `id_empaque` bigint NOT NULL AUTO_INCREMENT,
  `id_detalle_produccion` bigint NOT NULL,
  `id_producto_terminado` bigint NOT NULL,
  `lote_empaque` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_empaque` datetime NOT NULL,
  `fecha_vencimiento` date DEFAULT NULL,
  `estado` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'REGISTRADO',
  `cantidad_unidades` int NOT NULL,
  `cantidad_cajas` int DEFAULT NULL,
  `peso_total_kg` decimal(10,2) DEFAULT NULL,
  `observaciones` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id_empaque`),
  UNIQUE KEY `uq_empaque_detalle_producto_lote`
    (`id_detalle_produccion`, `id_producto_terminado`, `lote_empaque`),
  KEY `idx_empaque_detalle_produccion` (`id_detalle_produccion`),
  KEY `idx_empaque_producto_terminado` (`id_producto_terminado`),
  KEY `idx_empaque_fecha_empaque` (`fecha_empaque`),
  CONSTRAINT `fk_empaque_detalle_produccion`
    FOREIGN KEY (`id_detalle_produccion`)
    REFERENCES `detalle_produccion` (`id_detalle_produccion`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_empaque_producto_terminado`
    FOREIGN KEY (`id_producto_terminado`)
    REFERENCES `producto_terminado` (`id_producto_terminado`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;