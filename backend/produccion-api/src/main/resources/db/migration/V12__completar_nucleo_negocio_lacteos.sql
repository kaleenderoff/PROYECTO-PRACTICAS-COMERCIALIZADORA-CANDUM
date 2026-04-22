-- V12: completa el nucleo real del negocio.
-- Mantiene la base tecnica existente y agrega las piezas del flujo productivo:
-- formulas, ejecucion, baches, lotes, novedades y proceso especifico de lacteos.

CREATE TABLE formula (
  id BIGINT NOT NULL AUTO_INCREMENT,
  codigo VARCHAR(50) NOT NULL,
  nombre VARCHAR(150) NOT NULL,
  id_sku BIGINT NOT NULL,
  descripcion VARCHAR(500) NULL,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_formula_codigo (codigo),
  UNIQUE KEY uq_formula_sku_nombre (id_sku, nombre),
  KEY idx_formula_sku (id_sku),
  CONSTRAINT fk_formula_sku
    FOREIGN KEY (id_sku) REFERENCES catalogo_sku (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE formula_version (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_formula BIGINT NOT NULL,
  version INT NOT NULL,
  rendimiento_esperado_pct DECIMAL(6,2) NULL,
  fecha_vigencia_desde DATE NOT NULL,
  fecha_vigencia_hasta DATE NULL,
  estado ENUM('BORRADOR', 'VIGENTE', 'INACTIVA') NOT NULL DEFAULT 'BORRADOR',
  observaciones VARCHAR(500) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_formula_version (id_formula, version),
  KEY idx_formula_version_estado (estado),
  CONSTRAINT fk_formula_version_formula
    FOREIGN KEY (id_formula) REFERENCES formula (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE formula_detalle (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_formula_version BIGINT NOT NULL,
  id_insumo BIGINT NOT NULL,
  cantidad DECIMAL(14,6) NOT NULL,
  unidad_medida VARCHAR(50) NOT NULL,
  porcentaje_merma DECIMAL(6,2) NULL,
  obligatorio TINYINT(1) NOT NULL DEFAULT 1,
  orden INT NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_formula_detalle_insumo (id_formula_version, id_insumo),
  KEY idx_formula_detalle_insumo (id_insumo),
  CONSTRAINT fk_formula_detalle_version
    FOREIGN KEY (id_formula_version) REFERENCES formula_version (id) ON DELETE CASCADE,
  CONSTRAINT fk_formula_detalle_insumo
    FOREIGN KEY (id_insumo) REFERENCES insumo (id_insumo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE orden_produccion_detalle
  ADD COLUMN id_formula_version BIGINT NULL AFTER id_receta,
  ADD KEY idx_orden_detalle_formula_version (id_formula_version),
  ADD CONSTRAINT fk_orden_detalle_formula_version
    FOREIGN KEY (id_formula_version) REFERENCES formula_version (id);

CREATE TABLE ejecucion_produccion (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_orden BIGINT NOT NULL,
  id_jefe_linea BIGINT NOT NULL,
  fecha_hora_inicio DATETIME NULL,
  fecha_hora_fin DATETIME NULL,
  estado ENUM('NO_INICIADA', 'EN_PROCESO', 'PAUSADA', 'FINALIZADA', 'CANCELADA') NOT NULL DEFAULT 'NO_INICIADA',
  observaciones VARCHAR(500) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_ejecucion_orden (id_orden),
  KEY idx_ejecucion_jefe_linea (id_jefe_linea),
  KEY idx_ejecucion_estado (estado),
  CONSTRAINT fk_ejecucion_orden
    FOREIGN KEY (id_orden) REFERENCES orden_produccion (id) ON DELETE CASCADE,
  CONSTRAINT fk_ejecucion_jefe_linea
    FOREIGN KEY (id_jefe_linea) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE lote_produccion (
  id BIGINT NOT NULL AUTO_INCREMENT,
  codigo_lote VARCHAR(80) NOT NULL,
  id_orden_detalle BIGINT NOT NULL,
  id_sku BIGINT NOT NULL,
  fecha_produccion DATE NOT NULL,
  fecha_vencimiento DATE NULL,
  estado ENUM('ABIERTO', 'CERRADO', 'BLOQUEADO', 'LIBERADO') NOT NULL DEFAULT 'ABIERTO',
  observaciones VARCHAR(500) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_lote_produccion_codigo (codigo_lote),
  KEY idx_lote_orden_detalle (id_orden_detalle),
  KEY idx_lote_sku (id_sku),
  KEY idx_lote_estado (estado),
  CONSTRAINT fk_lote_orden_detalle
    FOREIGN KEY (id_orden_detalle) REFERENCES orden_produccion_detalle (id) ON DELETE CASCADE,
  CONSTRAINT fk_lote_sku
    FOREIGN KEY (id_sku) REFERENCES catalogo_sku (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE produccion_real (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_ejecucion BIGINT NOT NULL,
  id_orden BIGINT NOT NULL,
  kg_programados_total DECIMAL(14,3) NULL,
  kg_reales_total DECIMAL(14,3) NULL,
  unidades_reales_total DECIMAL(14,3) NULL,
  cajas_reales_total DECIMAL(14,3) NULL,
  rendimiento_total_pct DECIMAL(6,2) NULL,
  estado ENUM('ABIERTA', 'CONSOLIDADA', 'VALIDADA', 'RECHAZADA') NOT NULL DEFAULT 'ABIERTA',
  consolidado_por BIGINT NULL,
  fecha_consolidacion DATETIME NULL,
  observaciones VARCHAR(500) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_produccion_real_ejecucion (id_ejecucion),
  KEY idx_produccion_real_orden (id_orden),
  KEY idx_produccion_real_consolidado_por (consolidado_por),
  CONSTRAINT fk_produccion_real_ejecucion
    FOREIGN KEY (id_ejecucion) REFERENCES ejecucion_produccion (id) ON DELETE CASCADE,
  CONSTRAINT fk_produccion_real_orden
    FOREIGN KEY (id_orden) REFERENCES orden_produccion (id) ON DELETE CASCADE,
  CONSTRAINT fk_produccion_real_consolidado_por
    FOREIGN KEY (consolidado_por) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE produccion_real_sku
  ADD COLUMN id_produccion_real BIGINT NULL AFTER id,
  ADD COLUMN id_lote_produccion BIGINT NULL AFTER id_orden_detalle,
  ADD KEY idx_produccion_real_sku_produccion_real (id_produccion_real),
  ADD KEY idx_produccion_real_sku_lote (id_lote_produccion),
  ADD CONSTRAINT fk_produccion_real_sku_produccion_real
    FOREIGN KEY (id_produccion_real) REFERENCES produccion_real (id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_produccion_real_sku_lote
    FOREIGN KEY (id_lote_produccion) REFERENCES lote_produccion (id);

CREATE TABLE medicion_bache (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_ejecucion BIGINT NOT NULL,
  id_orden_detalle BIGINT NOT NULL,
  id_lote_produccion BIGINT NULL,
  numero_bache INT NOT NULL,
  kg_programados DECIMAL(14,3) NULL,
  kg_obtenidos DECIMAL(14,3) NULL,
  temperatura DECIMAL(6,2) NULL,
  ph DECIMAL(5,2) NULL,
  grados_brix DECIMAL(6,2) NULL,
  hora_inicio DATETIME NULL,
  hora_fin DATETIME NULL,
  observaciones VARCHAR(500) NULL,
  id_registrado_por BIGINT NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_medicion_bache (id_orden_detalle, numero_bache),
  KEY idx_medicion_bache_ejecucion (id_ejecucion),
  KEY idx_medicion_bache_lote (id_lote_produccion),
  KEY idx_medicion_bache_registrado_por (id_registrado_por),
  CONSTRAINT fk_medicion_bache_ejecucion
    FOREIGN KEY (id_ejecucion) REFERENCES ejecucion_produccion (id) ON DELETE CASCADE,
  CONSTRAINT fk_medicion_bache_orden_detalle
    FOREIGN KEY (id_orden_detalle) REFERENCES orden_produccion_detalle (id) ON DELETE CASCADE,
  CONSTRAINT fk_medicion_bache_lote
    FOREIGN KEY (id_lote_produccion) REFERENCES lote_produccion (id),
  CONSTRAINT fk_medicion_bache_registrado_por
    FOREIGN KEY (id_registrado_por) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE registro_insumo (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_ejecucion BIGINT NOT NULL,
  id_orden_detalle BIGINT NOT NULL,
  id_insumo BIGINT NOT NULL,
  id_inventario_insumo BIGINT NULL,
  id_movimiento_inventario BIGINT NULL,
  cantidad_requerida DECIMAL(14,3) NULL,
  cantidad_usada DECIMAL(14,3) NOT NULL,
  unidad_medida VARCHAR(50) NOT NULL,
  momento_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  observaciones VARCHAR(500) NULL,
  id_registrado_por BIGINT NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_registro_insumo_ejecucion (id_ejecucion),
  KEY idx_registro_insumo_orden_detalle (id_orden_detalle),
  KEY idx_registro_insumo_insumo (id_insumo),
  KEY idx_registro_insumo_inventario (id_inventario_insumo),
  KEY idx_registro_insumo_movimiento (id_movimiento_inventario),
  KEY idx_registro_insumo_registrado_por (id_registrado_por),
  CONSTRAINT fk_registro_insumo_ejecucion
    FOREIGN KEY (id_ejecucion) REFERENCES ejecucion_produccion (id) ON DELETE CASCADE,
  CONSTRAINT fk_registro_insumo_orden_detalle
    FOREIGN KEY (id_orden_detalle) REFERENCES orden_produccion_detalle (id) ON DELETE CASCADE,
  CONSTRAINT fk_registro_insumo_insumo
    FOREIGN KEY (id_insumo) REFERENCES insumo (id_insumo),
  CONSTRAINT fk_registro_insumo_inventario
    FOREIGN KEY (id_inventario_insumo) REFERENCES inventario_insumo (id),
  CONSTRAINT fk_registro_insumo_movimiento
    FOREIGN KEY (id_movimiento_inventario) REFERENCES movimiento_inventario_insumo (id),
  CONSTRAINT fk_registro_insumo_registrado_por
    FOREIGN KEY (id_registrado_por) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE novedad_produccion (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_ejecucion BIGINT NOT NULL,
  id_orden_detalle BIGINT NULL,
  tipo ENUM('PARADA', 'MERMA', 'CALIDAD', 'EQUIPO', 'PERSONAL', 'MATERIAL', 'OTRA') NOT NULL,
  severidad ENUM('BAJA', 'MEDIA', 'ALTA', 'CRITICA') NOT NULL DEFAULT 'MEDIA',
  descripcion VARCHAR(800) NOT NULL,
  fecha_hora_inicio DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_hora_fin DATETIME NULL,
  estado ENUM('ABIERTA', 'EN_REVISION', 'CERRADA') NOT NULL DEFAULT 'ABIERTA',
  id_reportada_por BIGINT NOT NULL,
  id_cerrada_por BIGINT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_novedad_ejecucion (id_ejecucion),
  KEY idx_novedad_orden_detalle (id_orden_detalle),
  KEY idx_novedad_tipo_estado (tipo, estado),
  KEY idx_novedad_reportada_por (id_reportada_por),
  KEY idx_novedad_cerrada_por (id_cerrada_por),
  CONSTRAINT fk_novedad_ejecucion
    FOREIGN KEY (id_ejecucion) REFERENCES ejecucion_produccion (id) ON DELETE CASCADE,
  CONSTRAINT fk_novedad_orden_detalle
    FOREIGN KEY (id_orden_detalle) REFERENCES orden_produccion_detalle (id) ON DELETE CASCADE,
  CONSTRAINT fk_novedad_reportada_por
    FOREIGN KEY (id_reportada_por) REFERENCES usuario (id_usuario),
  CONSTRAINT fk_novedad_cerrada_por
    FOREIGN KEY (id_cerrada_por) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE recepcion_leche (
  id BIGINT NOT NULL AUTO_INCREMENT,
  codigo_recepcion VARCHAR(50) NOT NULL,
  id_proveedor BIGINT NULL,
  fecha_recepcion DATETIME NOT NULL,
  cantidad_litros DECIMAL(14,3) NOT NULL,
  temperatura DECIMAL(6,2) NULL,
  acidez DECIMAL(6,3) NULL,
  densidad DECIMAL(8,4) NULL,
  estado ENUM('RECIBIDA', 'APROBADA', 'RECHAZADA', 'EN_PROCESO') NOT NULL DEFAULT 'RECIBIDA',
  observaciones VARCHAR(500) NULL,
  id_registrado_por BIGINT NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_recepcion_leche_codigo (codigo_recepcion),
  KEY idx_recepcion_leche_proveedor (id_proveedor),
  KEY idx_recepcion_leche_fecha (fecha_recepcion),
  KEY idx_recepcion_leche_registrado_por (id_registrado_por),
  CONSTRAINT fk_recepcion_leche_proveedor
    FOREIGN KEY (id_proveedor) REFERENCES proveedor (id),
  CONSTRAINT fk_recepcion_leche_registrado_por
    FOREIGN KEY (id_registrado_por) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE descremado (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_ejecucion BIGINT NOT NULL,
  codigo_proceso VARCHAR(50) NOT NULL,
  fecha_hora_inicio DATETIME NULL,
  fecha_hora_fin DATETIME NULL,
  litros_procesados DECIMAL(14,3) NULL,
  litros_crema_obtenida DECIMAL(14,3) NULL,
  litros_leche_descremada DECIMAL(14,3) NULL,
  rendimiento_pct DECIMAL(6,2) NULL,
  estado ENUM('PROGRAMADO', 'EN_PROCESO', 'FINALIZADO', 'CANCELADO') NOT NULL DEFAULT 'PROGRAMADO',
  observaciones VARCHAR(500) NULL,
  id_registrado_por BIGINT NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_descremado_codigo (codigo_proceso),
  KEY idx_descremado_ejecucion (id_ejecucion),
  KEY idx_descremado_registrado_por (id_registrado_por),
  CONSTRAINT fk_descremado_ejecucion
    FOREIGN KEY (id_ejecucion) REFERENCES ejecucion_produccion (id) ON DELETE CASCADE,
  CONSTRAINT fk_descremado_registrado_por
    FOREIGN KEY (id_registrado_por) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE descremado_recepcion (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_descremado BIGINT NOT NULL,
  id_recepcion_leche BIGINT NOT NULL,
  litros_usados DECIMAL(14,3) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_descremado_recepcion (id_descremado, id_recepcion_leche),
  KEY idx_descremado_recepcion_recepcion (id_recepcion_leche),
  CONSTRAINT fk_descremado_recepcion_descremado
    FOREIGN KEY (id_descremado) REFERENCES descremado (id) ON DELETE CASCADE,
  CONSTRAINT fk_descremado_recepcion_recepcion
    FOREIGN KEY (id_recepcion_leche) REFERENCES recepcion_leche (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE subproducto_produccion (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_ejecucion BIGINT NOT NULL,
  id_lote_produccion BIGINT NULL,
  tipo ENUM('CREMA', 'LECHE_DESCREMADA', 'SUERO', 'MERMA', 'OTRO') NOT NULL,
  cantidad DECIMAL(14,3) NOT NULL,
  unidad_medida VARCHAR(50) NOT NULL,
  destino ENUM('REPROCESO', 'INVENTARIO', 'DESECHO', 'VENTA', 'OTRO') NOT NULL DEFAULT 'INVENTARIO',
  observaciones VARCHAR(500) NULL,
  id_registrado_por BIGINT NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_subproducto_ejecucion (id_ejecucion),
  KEY idx_subproducto_lote (id_lote_produccion),
  KEY idx_subproducto_tipo (tipo),
  KEY idx_subproducto_registrado_por (id_registrado_por),
  CONSTRAINT fk_subproducto_ejecucion
    FOREIGN KEY (id_ejecucion) REFERENCES ejecucion_produccion (id) ON DELETE CASCADE,
  CONSTRAINT fk_subproducto_lote
    FOREIGN KEY (id_lote_produccion) REFERENCES lote_produccion (id),
  CONSTRAINT fk_subproducto_registrado_por
    FOREIGN KEY (id_registrado_por) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
