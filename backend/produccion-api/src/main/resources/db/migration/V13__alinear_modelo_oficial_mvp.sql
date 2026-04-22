-- V13: alinea el esquema existente con el modelo oficial MVP.
-- Objetivo: produccion + consumo reportado. El inventario formal queda desacoplado.
-- No se reescriben V11/V12 porque ya forman parte de la historia de Flyway.

-- =========================================================
-- FORMULAS: la formula pertenece al producto base, no al SKU.
-- =========================================================

ALTER TABLE formula
  DROP FOREIGN KEY fk_formula_sku;

ALTER TABLE formula
  DROP INDEX uq_formula_sku_nombre,
  DROP INDEX idx_formula_sku;

ALTER TABLE formula
  ADD COLUMN id_producto BIGINT NULL AFTER nombre;

UPDATE formula f
JOIN catalogo_sku sku ON sku.id = f.id_sku
SET f.id_producto = sku.id_producto;

ALTER TABLE formula
  MODIFY id_producto BIGINT NOT NULL,
  ADD UNIQUE KEY uq_formula_producto_nombre (id_producto, nombre),
  ADD KEY idx_formula_producto (id_producto),
  ADD CONSTRAINT fk_formula_producto
    FOREIGN KEY (id_producto) REFERENCES catalogo_producto (id),
  DROP COLUMN id_sku;

ALTER TABLE formula_version
  MODIFY version VARCHAR(20) NOT NULL,
  MODIFY estado ENUM('BORRADOR', 'APROBADA', 'VIGENTE', 'REEMPLAZADA', 'INACTIVA') NOT NULL DEFAULT 'BORRADOR',
  CHANGE COLUMN fecha_vigencia_desde fecha_inicio_vigencia DATE NOT NULL,
  CHANGE COLUMN fecha_vigencia_hasta fecha_fin_vigencia DATE NULL,
  ADD COLUMN kg_batch_total DECIMAL(10,2) NULL AFTER fecha_fin_vigencia,
  ADD COLUMN reduccion_evaporacion_pct DECIMAL(6,4) NULL AFTER kg_batch_total,
  ADD COLUMN rendimiento_teorico_pct DECIMAL(6,4) NULL AFTER reduccion_evaporacion_pct,
  ADD COLUMN brix_objetivo_min DECIMAL(5,2) NULL AFTER rendimiento_teorico_pct,
  ADD COLUMN brix_objetivo_max DECIMAL(5,2) NULL AFTER brix_objetivo_min,
  ADD COLUMN aprobado_por VARCHAR(100) NULL AFTER estado,
  ADD COLUMN documento_aprobacion VARCHAR(500) NULL AFTER aprobado_por,
  ADD COLUMN observaciones_tecnicas TEXT NULL AFTER documento_aprobacion,
  ADD COLUMN id_creado_por BIGINT NULL AFTER observaciones_tecnicas,
  ADD KEY idx_formula_version_creado_por (id_creado_por),
  ADD CONSTRAINT fk_formula_version_creado_por
    FOREIGN KEY (id_creado_por) REFERENCES usuario (id_usuario);

ALTER TABLE formula_detalle
  CHANGE COLUMN cantidad cantidad_kg DECIMAL(14,6) NOT NULL,
  CHANGE COLUMN orden orden_adicion INT NOT NULL DEFAULT 1,
  ADD COLUMN porcentaje DECIMAL(10,6) NULL AFTER cantidad_kg,
  ADD COLUMN es_critico TINYINT(1) NOT NULL DEFAULT 0 AFTER porcentaje,
  DROP COLUMN porcentaje_merma,
  DROP COLUMN obligatorio;

-- =========================================================
-- PROGRAMACION Y ORDENES: programar y ordenar por producto.
-- =========================================================

ALTER TABLE programacion_produccion
  CHANGE COLUMN fecha_programada fecha_produccion DATE NOT NULL,
  ADD COLUMN id_producto BIGINT NULL AFTER id_linea,
  ADD COLUMN num_baches_plan INT NULL AFTER id_turno,
  ADD COLUMN kg_bache_plan DECIMAL(10,2) NULL AFTER num_baches_plan,
  ADD COLUMN id_formula_version BIGINT NULL AFTER kg_bache_plan;

UPDATE programacion_produccion p
JOIN programacion_produccion_detalle d ON d.id_programacion = p.id
JOIN catalogo_sku sku ON sku.id = d.id_sku
SET p.id_producto = sku.id_producto
WHERE p.id_producto IS NULL;

ALTER TABLE programacion_produccion
  MODIFY estado ENUM('BORRADOR', 'CONFIRMADA', 'EN_EJECUCION', 'CERRADA', 'CANCELADA') NOT NULL DEFAULT 'BORRADOR',
  ADD KEY idx_programacion_producto (id_producto),
  ADD KEY idx_programacion_formula_version (id_formula_version),
  ADD CONSTRAINT fk_programacion_producto
    FOREIGN KEY (id_producto) REFERENCES catalogo_producto (id),
  ADD CONSTRAINT fk_programacion_formula_version
    FOREIGN KEY (id_formula_version) REFERENCES formula_version (id);

ALTER TABLE orden_produccion_detalle
  DROP FOREIGN KEY fk_orden_detalle_programacion;

RENAME TABLE programacion_produccion_detalle TO programacion_sku;

ALTER TABLE programacion_sku
  CHANGE COLUMN cantidad_programada unidades_objetivo INT NOT NULL,
  DROP COLUMN unidad_programada,
  DROP COLUMN prioridad;

ALTER TABLE orden_produccion_detalle
  CHANGE COLUMN id_programacion_detalle id_programacion_sku BIGINT NULL,
  RENAME INDEX idx_orden_detalle_programacion TO idx_orden_detalle_programacion_sku,
  ADD CONSTRAINT fk_orden_detalle_programacion_sku
    FOREIGN KEY (id_programacion_sku) REFERENCES programacion_sku (id);

ALTER TABLE orden_produccion
  ADD COLUMN id_producto BIGINT NULL AFTER id_linea;

UPDATE orden_produccion o
JOIN programacion_produccion p ON p.id = o.id_programacion
SET o.id_producto = p.id_producto
WHERE o.id_producto IS NULL;

ALTER TABLE orden_produccion
  DROP FOREIGN KEY fk_orden_jefe_linea;

ALTER TABLE orden_produccion
  CHANGE COLUMN id_jefe_linea id_jefe_linea_ejecutor BIGINT NOT NULL,
  MODIFY estado ENUM('PROGRAMADA', 'EN_EJECUCION', 'FINALIZADA', 'CERRADA', 'CANCELADA') NOT NULL DEFAULT 'PROGRAMADA',
  ADD COLUMN fecha_inicio_real DATETIME NULL AFTER observaciones,
  ADD COLUMN fecha_fin_real DATETIME NULL AFTER fecha_inicio_real,
  DROP COLUMN lote_general,
  ADD KEY idx_orden_producto (id_producto),
  ADD CONSTRAINT fk_orden_producto
    FOREIGN KEY (id_producto) REFERENCES catalogo_producto (id),
  ADD CONSTRAINT fk_orden_jefe_linea
    FOREIGN KEY (id_jefe_linea_ejecutor) REFERENCES usuario (id_usuario);

ALTER TABLE orden_produccion_detalle
  DROP FOREIGN KEY fk_orden_detalle_receta,
  DROP FOREIGN KEY fk_orden_detalle_formula_version;

ALTER TABLE orden_produccion_detalle
  DROP INDEX idx_orden_detalle_receta,
  DROP INDEX idx_orden_detalle_formula_version;

ALTER TABLE orden_produccion_detalle
  DROP INDEX uq_orden_detalle_sku_lote,
  DROP COLUMN id_receta,
  DROP COLUMN id_formula_version,
  DROP COLUMN lote_producto,
  DROP COLUMN fecha_vencimiento,
  DROP COLUMN estado,
  ADD UNIQUE KEY uq_orden_detalle_sku (id_orden, id_sku);

-- =========================================================
-- EJECUCION: datos reales del proceso.
-- =========================================================

ALTER TABLE ejecucion_produccion
  ADD COLUMN id_turno BIGINT NULL AFTER id_jefe_linea,
  ADD COLUMN hora_inicio TIME NULL AFTER id_turno,
  ADD COLUMN hora_fin TIME NULL AFTER hora_inicio,
  ADD COLUMN hora_hidrolisis TIME NULL AFTER hora_fin,
  ADD COLUMN hora_cocimiento TIME NULL AFTER hora_hidrolisis,
  ADD COLUMN num_baches_real INT NULL AFTER hora_cocimiento,
  ADD COLUMN kg_bache_real DECIMAL(10,2) NULL AFTER num_baches_real,
  ADD COLUMN kg_totales_reales DECIMAL(10,2) NULL AFTER kg_bache_real,
  ADD COLUMN kg_reproceso DECIMAL(10,2) NOT NULL DEFAULT 0 AFTER kg_totales_reales,
  ADD COLUMN es_reproceso TINYINT(1) NOT NULL DEFAULT 0 AFTER kg_reproceso,
  ADD COLUMN temperatura_promedio DECIMAL(5,2) NULL AFTER es_reproceso,
  ADD COLUMN tiempo_proceso_min INT NULL AFTER temperatura_promedio,
  ADD COLUMN variables_proceso JSON NULL AFTER tiempo_proceso_min,
  ADD COLUMN operario_fisico_nombre VARCHAR(100) NULL AFTER observaciones,
  ADD KEY idx_ejecucion_turno (id_turno),
  ADD CONSTRAINT fk_ejecucion_turno
    FOREIGN KEY (id_turno) REFERENCES turno (id);

UPDATE ejecucion_produccion e
JOIN orden_produccion o ON o.id = e.id_orden
SET e.id_turno = o.id_turno
WHERE e.id_turno IS NULL;

ALTER TABLE ejecucion_produccion
  MODIFY estado ENUM('PENDIENTE', 'EN_PROCESO', 'FINALIZADA', 'CON_NOVEDAD', 'REVISADA') NOT NULL DEFAULT 'PENDIENTE';

ALTER TABLE medicion_bache
  DROP FOREIGN KEY fk_medicion_bache_orden_detalle,
  DROP FOREIGN KEY fk_medicion_bache_lote,
  DROP FOREIGN KEY fk_medicion_bache_registrado_por;

ALTER TABLE medicion_bache
  DROP INDEX uq_medicion_bache,
  DROP INDEX idx_medicion_bache_lote,
  DROP INDEX idx_medicion_bache_registrado_por;

ALTER TABLE medicion_bache
  CHANGE COLUMN numero_bache numero_bache VARCHAR(10) NOT NULL,
  ADD COLUMN tipo_medicion ENUM('BACHE', 'MEZCLA', 'TANDA') NOT NULL DEFAULT 'BACHE' AFTER numero_bache,
  CHANGE COLUMN hora_inicio hora_medicion TIME NULL,
  CHANGE COLUMN id_registrado_por id_auxiliar_calidad BIGINT NOT NULL,
  DROP COLUMN id_orden_detalle,
  DROP COLUMN id_lote_produccion,
  DROP COLUMN kg_programados,
  DROP COLUMN kg_obtenidos,
  DROP COLUMN temperatura,
  DROP COLUMN hora_fin,
  ADD UNIQUE KEY uq_medicion_bache (id_ejecucion, numero_bache, tipo_medicion),
  ADD KEY idx_medicion_bache_auxiliar (id_auxiliar_calidad),
  ADD CONSTRAINT fk_medicion_bache_auxiliar
    FOREIGN KEY (id_auxiliar_calidad) REFERENCES usuario (id_usuario);

ALTER TABLE novedad_produccion
  DROP FOREIGN KEY fk_novedad_orden_detalle,
  DROP FOREIGN KEY fk_novedad_reportada_por,
  DROP FOREIGN KEY fk_novedad_cerrada_por;

ALTER TABLE novedad_produccion
  DROP INDEX idx_novedad_orden_detalle,
  DROP INDEX idx_novedad_tipo_estado,
  DROP INDEX idx_novedad_reportada_por,
  DROP INDEX idx_novedad_cerrada_por;

ALTER TABLE novedad_produccion
  CHANGE COLUMN tipo tipo_novedad ENUM('BAJA_GRASA', 'FALLA_CALDERA', 'RETRASO_LECHE', 'FALLA_EQUIPO', 'BRIX_FUERA_RANGO', 'REPROCESO', 'CAMBIO_PROCESO', 'OTRO') NOT NULL,
  ADD COLUMN impacto_rendimiento_pct DECIMAL(5,2) NULL AFTER descripcion,
  CHANGE COLUMN fecha_hora_inicio hora_ocurrencia TIME NULL,
  CHANGE COLUMN id_reportada_por id_jefe_linea BIGINT NOT NULL,
  DROP COLUMN id_orden_detalle,
  DROP COLUMN severidad,
  DROP COLUMN fecha_hora_fin,
  DROP COLUMN estado,
  DROP COLUMN id_cerrada_por,
  ADD KEY idx_novedad_jefe_linea (id_jefe_linea),
  ADD CONSTRAINT fk_novedad_jefe_linea
    FOREIGN KEY (id_jefe_linea) REFERENCES usuario (id_usuario);

-- =========================================================
-- CONSUMO REPORTADO: no depende de inventario formal.
-- =========================================================

ALTER TABLE registro_insumo
  DROP FOREIGN KEY fk_registro_insumo_inventario,
  DROP FOREIGN KEY fk_registro_insumo_movimiento;

ALTER TABLE registro_insumo
  DROP INDEX idx_registro_insumo_inventario,
  DROP INDEX idx_registro_insumo_movimiento;

ALTER TABLE registro_insumo
  MODIFY id_orden_detalle BIGINT NULL,
  ADD COLUMN lote_insumo VARCHAR(100) NULL AFTER id_insumo,
  DROP COLUMN id_inventario_insumo,
  DROP COLUMN id_movimiento_inventario;

-- =========================================================
-- RESULTADO Y TRAZABILIDAD: el lote nace de la ejecucion.
-- =========================================================

ALTER TABLE lote_produccion
  DROP FOREIGN KEY fk_lote_orden_detalle;

ALTER TABLE lote_produccion
  DROP INDEX idx_lote_orden_detalle;

ALTER TABLE lote_produccion
  ADD COLUMN id_ejecucion BIGINT NULL AFTER codigo_lote;

UPDATE lote_produccion l
JOIN orden_produccion_detalle od ON od.id = l.id_orden_detalle
JOIN orden_produccion o ON o.id = od.id_orden
JOIN ejecucion_produccion e ON e.id_orden = o.id
SET l.id_ejecucion = e.id
WHERE l.id_ejecucion IS NULL;

ALTER TABLE lote_produccion
  MODIFY estado ENUM('ACTIVO', 'EN_DISTRIBUCION', 'RETIRADO', 'VENCIDO') NOT NULL DEFAULT 'ACTIVO',
  ADD KEY idx_lote_ejecucion (id_ejecucion),
  ADD CONSTRAINT fk_lote_ejecucion
    FOREIGN KEY (id_ejecucion) REFERENCES ejecucion_produccion (id) ON DELETE CASCADE,
  DROP COLUMN id_orden_detalle;

ALTER TABLE produccion_real
  DROP FOREIGN KEY fk_produccion_real_orden;

ALTER TABLE produccion_real
  DROP FOREIGN KEY fk_produccion_real_consolidado_por;

ALTER TABLE produccion_real
  DROP INDEX idx_produccion_real_orden,
  DROP INDEX idx_produccion_real_consolidado_por;

ALTER TABLE produccion_real
  CHANGE COLUMN kg_reales_total kg_totales_reales DECIMAL(10,2) NULL,
  ADD COLUMN litros_leche_utilizados DECIMAL(10,2) NULL AFTER kg_totales_reales,
  CHANGE COLUMN rendimiento_total_pct rendimiento_real_1_pct DECIMAL(6,4) NULL,
  ADD COLUMN rendimiento_real_2_pct DECIMAL(6,4) NULL AFTER rendimiento_real_1_pct,
  MODIFY estado ENUM('BORRADOR', 'REGISTRADA', 'VALIDADA', 'CERRADA') NOT NULL DEFAULT 'BORRADOR',
  CHANGE COLUMN observaciones observaciones_finales TEXT NULL,
  DROP COLUMN id_orden,
  DROP COLUMN kg_programados_total,
  DROP COLUMN unidades_reales_total,
  DROP COLUMN cajas_reales_total,
  DROP COLUMN consolidado_por,
  DROP COLUMN fecha_consolidacion;

ALTER TABLE produccion_real_sku
  ADD KEY idx_produccion_real_sku_orden_detalle (id_orden_detalle);

ALTER TABLE produccion_real_sku
  DROP INDEX uq_produccion_real_orden_detalle;

ALTER TABLE produccion_real_sku
  ADD COLUMN id_sku BIGINT NULL AFTER id_lote_produccion;

UPDATE produccion_real_sku prs
JOIN orden_produccion_detalle od ON od.id = prs.id_orden_detalle
SET prs.id_sku = od.id_sku
WHERE prs.id_sku IS NULL;

ALTER TABLE produccion_real_sku
  CHANGE COLUMN unidades_reales unidades_reales INT NOT NULL,
  CHANGE COLUMN kg_reales kg_reales DECIMAL(10,2) NULL,
  CHANGE COLUMN unidades_programadas unidades_plan INT NULL,
  ADD COLUMN diferencia_unidades INT NULL AFTER unidades_plan,
  ADD KEY idx_produccion_real_sku_sku (id_sku),
  ADD CONSTRAINT fk_produccion_real_sku_sku
    FOREIGN KEY (id_sku) REFERENCES catalogo_sku (id);

ALTER TABLE validacion_produccion
  DROP FOREIGN KEY fk_validacion_produccion_orden_detalle,
  DROP FOREIGN KEY fk_validacion_produccion_validador;

ALTER TABLE validacion_produccion
  DROP INDEX uq_validacion_orden_tipo,
  DROP INDEX idx_validacion_validador,
  DROP INDEX idx_validacion_estado;

ALTER TABLE validacion_produccion
  ADD COLUMN id_ejecucion BIGINT NULL AFTER id,
  ADD COLUMN id_produccion_real BIGINT NULL AFTER id_ejecucion;

UPDATE validacion_produccion v
JOIN orden_produccion_detalle od ON od.id = v.id_orden_detalle
JOIN orden_produccion o ON o.id = od.id_orden
JOIN ejecucion_produccion e ON e.id_orden = o.id
LEFT JOIN produccion_real pr ON pr.id_ejecucion = e.id
SET v.id_ejecucion = e.id,
    v.id_produccion_real = pr.id
WHERE v.id_ejecucion IS NULL;

ALTER TABLE validacion_produccion
  CHANGE COLUMN id_validador id_jefe_produccion BIGINT NOT NULL,
  ADD COLUMN aprobado TINYINT(1) NOT NULL DEFAULT 0 AFTER id_produccion_real,
  ADD COLUMN requiere_revision TINYINT(1) NOT NULL DEFAULT 0 AFTER fecha_validacion,
  DROP COLUMN id_orden_detalle,
  DROP COLUMN tipo_validacion,
  DROP COLUMN estado,
  ADD UNIQUE KEY uq_validacion_ejecucion (id_ejecucion),
  ADD KEY idx_validacion_produccion_real (id_produccion_real),
  ADD KEY idx_validacion_jefe_produccion (id_jefe_produccion),
  ADD CONSTRAINT fk_validacion_produccion_ejecucion
    FOREIGN KEY (id_ejecucion) REFERENCES ejecucion_produccion (id),
  ADD CONSTRAINT fk_validacion_produccion_real
    FOREIGN KEY (id_produccion_real) REFERENCES produccion_real (id),
  ADD CONSTRAINT fk_validacion_jefe_produccion
    FOREIGN KEY (id_jefe_produccion) REFERENCES usuario (id_usuario);

-- =========================================================
-- LACTEOS: recepcion/descremado independientes de produccion.
-- =========================================================

ALTER TABLE recepcion_leche
  DROP INDEX uq_recepcion_leche_codigo;

ALTER TABLE recepcion_leche
  DROP FOREIGN KEY fk_recepcion_leche_proveedor;

ALTER TABLE recepcion_leche
  CHANGE COLUMN fecha_recepcion fecha_recepcion DATE NOT NULL,
  MODIFY id_proveedor BIGINT NOT NULL,
  ADD COLUMN litros_segun_remision DECIMAL(10,2) NULL AFTER id_proveedor,
  CHANGE COLUMN cantidad_litros litros_recibidos_real DECIMAL(10,2) NOT NULL,
  ADD COLUMN kg_brutos DECIMAL(10,2) NULL AFTER litros_recibidos_real,
  ADD COLUMN kg_tara DECIMAL(10,2) NULL AFTER kg_brutos,
  ADD COLUMN kg_netos DECIMAL(10,2) NULL AFTER kg_tara,
  ADD COLUMN pct_grasa_antes_descremado DECIMAL(6,4) NULL AFTER kg_netos,
  CHANGE COLUMN temperatura temperatura_llegada DECIMAL(4,1) NULL,
  ADD COLUMN numero_remision_proveedor VARCHAR(50) NULL AFTER temperatura_llegada,
  ADD COLUMN vehiculo_placa VARCHAR(20) NULL AFTER numero_remision_proveedor,
  ADD COLUMN conductor VARCHAR(100) NULL AFTER vehiculo_placa,
  ADD COLUMN hora_llegada TIME NULL AFTER conductor,
  DROP COLUMN codigo_recepcion,
  DROP COLUMN estado;

ALTER TABLE recepcion_leche
  DROP COLUMN acidez,
  DROP COLUMN densidad;

ALTER TABLE recepcion_leche
  ADD CONSTRAINT fk_recepcion_leche_proveedor
    FOREIGN KEY (id_proveedor) REFERENCES proveedor (id);

ALTER TABLE descremado_recepcion
  DROP FOREIGN KEY fk_descremado_recepcion_recepcion;

ALTER TABLE descremado_recepcion
  CHANGE COLUMN id_recepcion_leche id_recepcion BIGINT NOT NULL,
  CHANGE COLUMN litros_usados litros_aportados DECIMAL(10,2) NOT NULL,
  ADD CONSTRAINT fk_descremado_recepcion_recepcion
    FOREIGN KEY (id_recepcion) REFERENCES recepcion_leche (id);

ALTER TABLE descremado
  DROP FOREIGN KEY fk_descremado_ejecucion;

ALTER TABLE descremado
  DROP INDEX idx_descremado_ejecucion;

ALTER TABLE descremado
  ADD COLUMN fecha_descremado DATE NULL AFTER id,
  CHANGE COLUMN litros_procesados litros_procesados DECIMAL(10,2) NOT NULL,
  ADD COLUMN litros_entran_produccion DECIMAL(10,2) NULL AFTER litros_procesados,
  ADD COLUMN pct_grasa_antes DECIMAL(6,4) NULL AFTER litros_entran_produccion,
  ADD COLUMN pct_grasa_despues DECIMAL(6,4) NULL AFTER pct_grasa_antes,
  ADD COLUMN grasa_total_kg DECIMAL(10,4) NULL AFTER pct_grasa_despues,
  ADD COLUMN grasa_despues_kg DECIMAL(10,4) NULL AFTER grasa_total_kg,
  ADD COLUMN diferencia_grasa_kg DECIMAL(10,4) NULL AFTER grasa_despues_kg,
  ADD COLUMN bolsas_crema DECIMAL(5,1) NULL AFTER diferencia_grasa_kg,
  ADD COLUMN kg_crema_real DECIMAL(10,2) NULL AFTER bolsas_crema,
  ADD COLUMN kg_crema_esperado DECIMAL(10,2) NULL AFTER kg_crema_real,
  DROP COLUMN id_ejecucion,
  DROP COLUMN codigo_proceso,
  DROP COLUMN fecha_hora_inicio,
  DROP COLUMN fecha_hora_fin,
  DROP COLUMN litros_crema_obtenida,
  DROP COLUMN litros_leche_descremada,
  DROP COLUMN rendimiento_pct,
  DROP COLUMN estado,
  ADD KEY idx_descremado_fecha (fecha_descremado);

ALTER TABLE subproducto_produccion
  DROP FOREIGN KEY fk_subproducto_ejecucion,
  DROP FOREIGN KEY fk_subproducto_lote,
  DROP FOREIGN KEY fk_subproducto_registrado_por;

ALTER TABLE subproducto_produccion
  DROP INDEX idx_subproducto_ejecucion,
  DROP INDEX idx_subproducto_lote,
  DROP INDEX idx_subproducto_tipo,
  DROP INDEX idx_subproducto_registrado_por;

ALTER TABLE subproducto_produccion
  ADD COLUMN id_descremado BIGINT NULL AFTER id,
  ADD COLUMN id_sku BIGINT NULL AFTER id_descremado,
  CHANGE COLUMN unidad_medida unidad ENUM('BOLSAS', 'KG') NOT NULL DEFAULT 'KG',
  ADD COLUMN lote VARCHAR(50) NULL AFTER unidad,
  ADD COLUMN fecha_produccion DATE NULL AFTER lote,
  DROP COLUMN id_ejecucion,
  DROP COLUMN id_lote_produccion,
  DROP COLUMN tipo,
  DROP COLUMN destino,
  DROP COLUMN id_registrado_por,
  ADD KEY idx_subproducto_descremado (id_descremado),
  ADD KEY idx_subproducto_sku (id_sku),
  ADD CONSTRAINT fk_subproducto_descremado
    FOREIGN KEY (id_descremado) REFERENCES descremado (id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_subproducto_sku
    FOREIGN KEY (id_sku) REFERENCES catalogo_sku (id);
