ALTER TABLE medicion_calidad_lactea
  MODIFY id_produccion_lactea BIGINT NULL,
  ADD COLUMN id_orden_produccion BIGINT NULL AFTER id_produccion_lactea_batch,
  ADD COLUMN id_ejecucion_batch BIGINT NULL AFTER id_orden_produccion,
  ADD KEY idx_medicion_calidad_orden (id_orden_produccion),
  ADD KEY idx_medicion_calidad_ejecucion_batch (id_ejecucion_batch),
  ADD CONSTRAINT fk_medicion_calidad_orden
    FOREIGN KEY (id_orden_produccion) REFERENCES orden_produccion(id) ON DELETE CASCADE,
  ADD CONSTRAINT fk_medicion_calidad_ejecucion_batch
    FOREIGN KEY (id_ejecucion_batch) REFERENCES ejecucion_batch(id);
