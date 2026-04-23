-- V16: congelar reglas finales del modelo logico MVP.
-- Cierra los dos campos que no deben quedar sin responsable/origen real.

-- =========================================================
-- FORMULAS: toda version debe tener usuario creador.
-- =========================================================

UPDATE formula_version
SET id_creado_por = (
  SELECT id_usuario
  FROM usuario
  ORDER BY id_usuario
  LIMIT 1
)
WHERE id_creado_por IS NULL;

ALTER TABLE formula_version
  MODIFY id_creado_por BIGINT NOT NULL;

-- =========================================================
-- LOTES: todo lote debe nacer de una ejecucion real.
-- =========================================================

DELETE FROM lote_produccion
WHERE id_ejecucion IS NULL;

ALTER TABLE lote_produccion
  MODIFY id_ejecucion BIGINT NOT NULL;
