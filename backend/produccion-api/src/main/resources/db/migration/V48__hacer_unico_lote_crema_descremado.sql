UPDATE descremado_recepcion
SET lote_crema = CONCAT(lote_crema, '-', id_recepcion_leche)
WHERE lote_crema IS NOT NULL;

ALTER TABLE descremado_recepcion
  ADD UNIQUE KEY uq_descremado_recepcion_lote_crema (lote_crema);
