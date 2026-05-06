ALTER TABLE descremado_recepcion
  ADD COLUMN id_tanque_destino BIGINT NULL AFTER id_recepcion_leche,
  ADD COLUMN id_movimiento_salida BIGINT NULL AFTER crema_obtenida_kg,
  ADD COLUMN id_movimiento_entrada BIGINT NULL AFTER id_movimiento_salida,
  ADD KEY idx_descremado_recepcion_tanque_destino (id_tanque_destino),
  ADD KEY idx_descremado_recepcion_mov_salida (id_movimiento_salida),
  ADD KEY idx_descremado_recepcion_mov_entrada (id_movimiento_entrada),
  ADD CONSTRAINT fk_descremado_recepcion_tanque_destino
    FOREIGN KEY (id_tanque_destino) REFERENCES tanque_leche(id),
  ADD CONSTRAINT fk_descremado_recepcion_mov_salida
    FOREIGN KEY (id_movimiento_salida) REFERENCES movimiento_leche(id),
  ADD CONSTRAINT fk_descremado_recepcion_mov_entrada
    FOREIGN KEY (id_movimiento_entrada) REFERENCES movimiento_leche(id);
