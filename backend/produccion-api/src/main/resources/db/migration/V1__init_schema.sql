-- =========================================
-- V1__init_schema.sql
-- Esquema inicial compatible con el backend actual
-- NO ejecutar sobre una base ya creada con datos
-- =========================================

-- =========================================
-- TABLA: insumo
-- =========================================
CREATE TABLE `insumo` (
  `id_insumo` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `unidad_medida` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id_insumo`),
  UNIQUE KEY `uq_insumo_nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================
-- TABLA: linea_produccion
-- =========================================
CREATE TABLE `linea_produccion` (
  `id_linea_produccion` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id_linea_produccion`),
  UNIQUE KEY `uq_linea_produccion_nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================
-- TABLA: usuario
-- =========================================
CREATE TABLE `usuario` (
  `id_usuario` bigint NOT NULL AUTO_INCREMENT,
  `cc` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `primer_nombre` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `segundo_nombre` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `primer_apellido` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `segundo_apellido` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `rol` enum('OPERARIO','JEFE_LINEA','INGENIERO','JEFE_PLANTA','ADMIN') COLLATE utf8mb4_unicode_ci NOT NULL,
  `activo` tinyint(1) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `uk_usuario_cc` (`cc`),
  UNIQUE KEY `email` (`email`),
  CONSTRAINT `chk_cc_formato` CHECK (regexp_like(`cc`,_utf8mb4'^[0-9]+$'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================
-- TABLA: log_auditoria
-- =========================================
CREATE TABLE `log_auditoria` (
  `id_log` bigint NOT NULL AUTO_INCREMENT,
  `id_usuario` bigint NOT NULL,
  `accion` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `entidad_afectada` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `id_registro_afectado` bigint DEFAULT NULL,
  `detalle` text COLLATE utf8mb4_unicode_ci,
  `fecha_hora` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_log`),
  KEY `idx_log_auditoria_usuario` (`id_usuario`),
  CONSTRAINT `fk_log_auditoria_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================
-- TABLA: produccion
-- =========================================
CREATE TABLE `produccion` (
  `id_produccion` bigint NOT NULL AUTO_INCREMENT,
  `fecha_produccion` date NOT NULL,
  `tipo_turno` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `numero_lote` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_vencimiento` date DEFAULT NULL,
  `estado` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `id_operario` bigint NOT NULL,
  `id_jefe_linea` bigint NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `observaciones_generales` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `id_linea_produccion` bigint NOT NULL,
  PRIMARY KEY (`id_produccion`),
  UNIQUE KEY `uq_produccion_numero_lote` (`numero_lote`),
  KEY `fk_produccion_operario` (`id_operario`),
  KEY `fk_produccion_jefe_linea` (`id_jefe_linea`),
  KEY `idx_produccion_linea_produccion` (`id_linea_produccion`),
  CONSTRAINT `fk_produccion_jefe_linea` FOREIGN KEY (`id_jefe_linea`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `fk_produccion_linea_produccion` FOREIGN KEY (`id_linea_produccion`) REFERENCES `linea_produccion` (`id_linea_produccion`),
  CONSTRAINT `fk_produccion_operario` FOREIGN KEY (`id_operario`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `fk_turno_jefe_linea` FOREIGN KEY (`id_jefe_linea`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `fk_turno_operario` FOREIGN KEY (`id_operario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================
-- TABLA: producto
-- =========================================
CREATE TABLE `producto` (
  `id_producto` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gramaje_g` decimal(10,2) NOT NULL,
  `marca` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `unidad_medida` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `id_linea_produccion` bigint DEFAULT NULL,
  PRIMARY KEY (`id_producto`),
  UNIQUE KEY `uq_producto` (`nombre`,`gramaje_g`,`marca`),
  KEY `fk_producto_linea` (`id_linea_produccion`),
  CONSTRAINT `fk_producto_linea` FOREIGN KEY (`id_linea_produccion`) REFERENCES `linea_produccion` (`id_linea_produccion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================
-- TABLA: detalle_produccion
-- =========================================
CREATE TABLE `detalle_produccion` (
  `id_detalle_produccion` bigint NOT NULL AUTO_INCREMENT,
  `id_produccion` bigint NOT NULL,
  `id_producto` bigint NOT NULL,
  `kg_programados` decimal(10,2) NOT NULL,
  `kg_batch` decimal(10,2) NOT NULL,
  `num_batch` int NOT NULL,
  `unidades_reales` int NOT NULL,
  `rendimiento_pct` decimal(5,2) GENERATED ALWAYS AS (round(((`kg_batch` / `kg_programados`) * 100),2)) STORED,
  `observaciones` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_hora_registro` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id_detalle_produccion`),
  UNIQUE KEY `uq_detalle_produccion` (`id_produccion`,`id_producto`,`num_batch`),
  KEY `idx_detalle_produccion_produccion` (`id_produccion`),
  KEY `idx_detalle_produccion_producto` (`id_producto`),
  CONSTRAINT `fk_detalle_produccion_produccion` FOREIGN KEY (`id_produccion`) REFERENCES `produccion` (`id_produccion`) ON DELETE CASCADE,
  CONSTRAINT `fk_detalle_produccion_producto` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id_producto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================
-- TABLA: validacion
-- =========================================
CREATE TABLE `validacion` (
  `id_validacion` bigint NOT NULL AUTO_INCREMENT,
  `id_detalle_produccion` bigint NOT NULL,
  `id_validador` bigint NOT NULL,
  `estado` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `observacion` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_validacion` datetime NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id_validacion`),
  UNIQUE KEY `uq_validacion_detalle` (`id_detalle_produccion`),
  KEY `idx_validacion_validador` (`id_validador`),
  CONSTRAINT `fk_validacion_detalle_produccion` FOREIGN KEY (`id_detalle_produccion`) REFERENCES `detalle_produccion` (`id_detalle_produccion`) ON DELETE CASCADE,
  CONSTRAINT `fk_validacion_validador` FOREIGN KEY (`id_validador`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================
-- TABLA: consumo_insumo
-- =========================================
CREATE TABLE `consumo_insumo` (
  `id_consumo_insumo` bigint NOT NULL AUTO_INCREMENT,
  `id_produccion` bigint NOT NULL,
  `id_insumo` bigint NOT NULL,
  `cantidad_consumida` decimal(12,2) NOT NULL,
  `observaciones` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `id_detalle_produccion` bigint DEFAULT NULL,
  PRIMARY KEY (`id_consumo_insumo`),
  UNIQUE KEY `uq_consumo_insumo` (`id_produccion`,`id_insumo`),
  KEY `fk_consumo_insumo_insumo` (`id_insumo`),
  KEY `fk_consumo_detalle` (`id_detalle_produccion`),
  CONSTRAINT `fk_consumo_detalle` FOREIGN KEY (`id_detalle_produccion`) REFERENCES `detalle_produccion` (`id_detalle_produccion`),
  CONSTRAINT `fk_consumo_insumo_insumo` FOREIGN KEY (`id_insumo`) REFERENCES `insumo` (`id_insumo`),
  CONSTRAINT `fk_consumo_insumo_produccion` FOREIGN KEY (`id_produccion`) REFERENCES `produccion` (`id_produccion`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;