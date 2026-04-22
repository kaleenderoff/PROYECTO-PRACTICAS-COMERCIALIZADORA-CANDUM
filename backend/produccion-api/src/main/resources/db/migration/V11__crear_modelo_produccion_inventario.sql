-- V11: modelo operativo nuevo para lacteos y futuras lineas.
-- Conecta programacion, orden, ejecucion, produccion real, insumos e inventario
-- contra los catalogos nuevos del redisenio.

ALTER TABLE insumo
  ADD COLUMN codigo VARCHAR(50) NULL AFTER id_insumo,
  ADD COLUMN tipo ENUM('MATERIA_PRIMA', 'MATERIAL_EMPAQUE', 'ADITIVO', 'OTRO') NOT NULL DEFAULT 'MATERIA_PRIMA' AFTER descripcion,
  ADD COLUMN stock_minimo DECIMAL(14,3) NULL AFTER unidad_medida,
  ADD COLUMN id_proveedor BIGINT NULL AFTER stock_minimo,
  ADD UNIQUE KEY uq_insumo_codigo (codigo),
  ADD KEY idx_insumo_proveedor (id_proveedor),
  ADD CONSTRAINT fk_insumo_proveedor
    FOREIGN KEY (id_proveedor) REFERENCES proveedor (id);

CREATE TABLE inventario_insumo (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_insumo BIGINT NOT NULL,
  lote VARCHAR(80) NULL,
  fecha_vencimiento DATE NULL,
  cantidad_actual DECIMAL(14,3) NOT NULL DEFAULT 0,
  costo_unitario DECIMAL(14,4) NULL,
  ubicacion VARCHAR(120) NULL,
  estado ENUM('DISPONIBLE', 'RESERVADO', 'BLOQUEADO', 'AGOTADO') NOT NULL DEFAULT 'DISPONIBLE',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_inventario_insumo_lote (id_insumo, lote),
  KEY idx_inventario_insumo_estado (estado),
  KEY idx_inventario_insumo_vencimiento (fecha_vencimiento),
  CONSTRAINT fk_inventario_insumo_insumo
    FOREIGN KEY (id_insumo) REFERENCES insumo (id_insumo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE movimiento_inventario_insumo (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_inventario_insumo BIGINT NOT NULL,
  id_usuario BIGINT NOT NULL,
  tipo_movimiento ENUM('ENTRADA', 'SALIDA_PRODUCCION', 'AJUSTE_POSITIVO', 'AJUSTE_NEGATIVO', 'BLOQUEO', 'LIBERACION') NOT NULL,
  cantidad DECIMAL(14,3) NOT NULL,
  cantidad_anterior DECIMAL(14,3) NOT NULL,
  cantidad_nueva DECIMAL(14,3) NOT NULL,
  referencia_tipo VARCHAR(60) NULL,
  referencia_id BIGINT NULL,
  observacion VARCHAR(500) NULL,
  fecha_movimiento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_mov_inv_insumo_inventario (id_inventario_insumo),
  KEY idx_mov_inv_insumo_usuario (id_usuario),
  KEY idx_mov_inv_insumo_fecha (fecha_movimiento),
  KEY idx_mov_inv_insumo_referencia (referencia_tipo, referencia_id),
  CONSTRAINT fk_mov_inv_insumo_inventario
    FOREIGN KEY (id_inventario_insumo) REFERENCES inventario_insumo (id),
  CONSTRAINT fk_mov_inv_insumo_usuario
    FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE receta_sku (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_sku BIGINT NOT NULL,
  version INT NOT NULL DEFAULT 1,
  nombre VARCHAR(150) NOT NULL,
  rendimiento_esperado_pct DECIMAL(6,2) NULL,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_receta_sku_version (id_sku, version),
  KEY idx_receta_sku_activo (activo),
  CONSTRAINT fk_receta_sku_sku
    FOREIGN KEY (id_sku) REFERENCES catalogo_sku (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE receta_sku_detalle (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_receta BIGINT NOT NULL,
  id_insumo BIGINT NOT NULL,
  cantidad_por_unidad DECIMAL(14,6) NOT NULL,
  unidad_medida VARCHAR(50) NOT NULL,
  merma_pct DECIMAL(6,2) NULL,
  obligatorio TINYINT(1) NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_receta_detalle_insumo (id_receta, id_insumo),
  KEY idx_receta_detalle_insumo (id_insumo),
  CONSTRAINT fk_receta_detalle_receta
    FOREIGN KEY (id_receta) REFERENCES receta_sku (id) ON DELETE CASCADE,
  CONSTRAINT fk_receta_detalle_insumo
    FOREIGN KEY (id_insumo) REFERENCES insumo (id_insumo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE programacion_produccion (
  id BIGINT NOT NULL AUTO_INCREMENT,
  codigo_programacion VARCHAR(40) NOT NULL,
  fecha_programada DATE NOT NULL,
  id_linea BIGINT NOT NULL,
  id_turno BIGINT NOT NULL,
  id_jefe_produccion BIGINT NOT NULL,
  estado ENUM('BORRADOR', 'PROGRAMADA', 'CANCELADA', 'CERRADA') NOT NULL DEFAULT 'BORRADOR',
  observaciones VARCHAR(500) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_programacion_codigo (codigo_programacion),
  UNIQUE KEY uq_programacion_linea_turno_fecha (fecha_programada, id_linea, id_turno),
  KEY idx_programacion_linea (id_linea),
  KEY idx_programacion_turno (id_turno),
  KEY idx_programacion_jefe (id_jefe_produccion),
  KEY idx_programacion_estado_fecha (estado, fecha_programada),
  CONSTRAINT fk_programacion_linea
    FOREIGN KEY (id_linea) REFERENCES catalogo_linea (id),
  CONSTRAINT fk_programacion_turno
    FOREIGN KEY (id_turno) REFERENCES turno (id),
  CONSTRAINT fk_programacion_jefe
    FOREIGN KEY (id_jefe_produccion) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE programacion_produccion_detalle (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_programacion BIGINT NOT NULL,
  id_sku BIGINT NOT NULL,
  cantidad_programada DECIMAL(14,3) NOT NULL,
  unidad_programada VARCHAR(50) NOT NULL DEFAULT 'UNIDADES',
  prioridad INT NOT NULL DEFAULT 1,
  observaciones VARCHAR(500) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_programacion_detalle_sku (id_programacion, id_sku),
  KEY idx_programacion_detalle_sku (id_sku),
  CONSTRAINT fk_programacion_detalle_programacion
    FOREIGN KEY (id_programacion) REFERENCES programacion_produccion (id) ON DELETE CASCADE,
  CONSTRAINT fk_programacion_detalle_sku
    FOREIGN KEY (id_sku) REFERENCES catalogo_sku (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE orden_produccion (
  id BIGINT NOT NULL AUTO_INCREMENT,
  numero_orden VARCHAR(40) NOT NULL,
  id_programacion BIGINT NULL,
  id_linea BIGINT NOT NULL,
  id_turno BIGINT NOT NULL,
  id_jefe_linea BIGINT NOT NULL,
  id_creada_por BIGINT NOT NULL,
  fecha_produccion DATE NOT NULL,
  lote_general VARCHAR(80) NULL,
  estado ENUM('CREADA', 'EN_PROCESO', 'PAUSADA', 'FINALIZADA', 'CANCELADA') NOT NULL DEFAULT 'CREADA',
  observaciones VARCHAR(500) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_orden_numero (numero_orden),
  KEY idx_orden_programacion (id_programacion),
  KEY idx_orden_linea (id_linea),
  KEY idx_orden_turno (id_turno),
  KEY idx_orden_jefe_linea (id_jefe_linea),
  KEY idx_orden_creada_por (id_creada_por),
  KEY idx_orden_estado_fecha (estado, fecha_produccion),
  CONSTRAINT fk_orden_programacion
    FOREIGN KEY (id_programacion) REFERENCES programacion_produccion (id),
  CONSTRAINT fk_orden_linea
    FOREIGN KEY (id_linea) REFERENCES catalogo_linea (id),
  CONSTRAINT fk_orden_turno
    FOREIGN KEY (id_turno) REFERENCES turno (id),
  CONSTRAINT fk_orden_jefe_linea
    FOREIGN KEY (id_jefe_linea) REFERENCES usuario (id_usuario),
  CONSTRAINT fk_orden_creada_por
    FOREIGN KEY (id_creada_por) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE orden_produccion_detalle (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_orden BIGINT NOT NULL,
  id_programacion_detalle BIGINT NULL,
  id_sku BIGINT NOT NULL,
  id_receta BIGINT NULL,
  cantidad_programada DECIMAL(14,3) NOT NULL,
  unidad_programada VARCHAR(50) NOT NULL DEFAULT 'UNIDADES',
  lote_producto VARCHAR(80) NULL,
  fecha_vencimiento DATE NULL,
  estado ENUM('PENDIENTE', 'EN_PROCESO', 'FINALIZADO', 'CANCELADO') NOT NULL DEFAULT 'PENDIENTE',
  observaciones VARCHAR(500) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_orden_detalle_sku_lote (id_orden, id_sku, lote_producto),
  KEY idx_orden_detalle_programacion (id_programacion_detalle),
  KEY idx_orden_detalle_sku (id_sku),
  KEY idx_orden_detalle_receta (id_receta),
  CONSTRAINT fk_orden_detalle_orden
    FOREIGN KEY (id_orden) REFERENCES orden_produccion (id) ON DELETE CASCADE,
  CONSTRAINT fk_orden_detalle_programacion
    FOREIGN KEY (id_programacion_detalle) REFERENCES programacion_produccion_detalle (id),
  CONSTRAINT fk_orden_detalle_sku
    FOREIGN KEY (id_sku) REFERENCES catalogo_sku (id),
  CONSTRAINT fk_orden_detalle_receta
    FOREIGN KEY (id_receta) REFERENCES receta_sku (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE produccion_real_sku (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_orden_detalle BIGINT NOT NULL,
  id_registrado_por BIGINT NOT NULL,
  kg_programados DECIMAL(14,3) NULL,
  kg_reales DECIMAL(14,3) NULL,
  unidades_programadas DECIMAL(14,3) NULL,
  unidades_reales DECIMAL(14,3) NULL,
  cajas_reales DECIMAL(14,3) NULL,
  rendimiento_pct DECIMAL(6,2) NULL,
  fecha_hora_inicio DATETIME NULL,
  fecha_hora_fin DATETIME NULL,
  observaciones VARCHAR(500) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_produccion_real_orden_detalle (id_orden_detalle),
  KEY idx_produccion_real_registrado_por (id_registrado_por),
  CONSTRAINT fk_produccion_real_orden_detalle
    FOREIGN KEY (id_orden_detalle) REFERENCES orden_produccion_detalle (id) ON DELETE CASCADE,
  CONSTRAINT fk_produccion_real_registrado_por
    FOREIGN KEY (id_registrado_por) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE consumo_insumo_orden (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_orden_detalle BIGINT NOT NULL,
  id_insumo BIGINT NOT NULL,
  id_inventario_insumo BIGINT NULL,
  cantidad_requerida DECIMAL(14,3) NULL,
  cantidad_consumida DECIMAL(14,3) NOT NULL,
  unidad_medida VARCHAR(50) NOT NULL,
  id_registrado_por BIGINT NOT NULL,
  observaciones VARCHAR(500) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_consumo_orden_detalle (id_orden_detalle),
  KEY idx_consumo_insumo (id_insumo),
  KEY idx_consumo_inventario (id_inventario_insumo),
  KEY idx_consumo_registrado_por (id_registrado_por),
  CONSTRAINT fk_consumo_orden_detalle
    FOREIGN KEY (id_orden_detalle) REFERENCES orden_produccion_detalle (id) ON DELETE CASCADE,
  CONSTRAINT fk_consumo_orden_insumo
    FOREIGN KEY (id_insumo) REFERENCES insumo (id_insumo),
  CONSTRAINT fk_consumo_orden_inventario
    FOREIGN KEY (id_inventario_insumo) REFERENCES inventario_insumo (id),
  CONSTRAINT fk_consumo_orden_registrado_por
    FOREIGN KEY (id_registrado_por) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE validacion_produccion (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_orden_detalle BIGINT NOT NULL,
  id_validador BIGINT NOT NULL,
  tipo_validacion ENUM('CALIDAD', 'PRODUCCION', 'INVENTARIO') NOT NULL DEFAULT 'CALIDAD',
  estado ENUM('PENDIENTE', 'APROBADA', 'RECHAZADA', 'OBSERVADA') NOT NULL DEFAULT 'PENDIENTE',
  observacion VARCHAR(500) NULL,
  fecha_validacion DATETIME NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_validacion_orden_tipo (id_orden_detalle, tipo_validacion),
  KEY idx_validacion_validador (id_validador),
  KEY idx_validacion_estado (estado),
  CONSTRAINT fk_validacion_produccion_orden_detalle
    FOREIGN KEY (id_orden_detalle) REFERENCES orden_produccion_detalle (id) ON DELETE CASCADE,
  CONSTRAINT fk_validacion_produccion_validador
    FOREIGN KEY (id_validador) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
