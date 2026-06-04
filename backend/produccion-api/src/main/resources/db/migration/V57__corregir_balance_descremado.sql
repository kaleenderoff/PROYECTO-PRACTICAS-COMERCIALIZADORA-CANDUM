UPDATE movimiento_leche m
JOIN descremado_recepcion dr
  ON dr.id_movimiento_entrada = m.id
SET m.cantidad_litros = GREATEST(dr.litros_descremados - COALESCE(dr.crema_obtenida_kg, 0), 0)
WHERE m.tipo_movimiento = 'ENTRADA_DESCREME';

UPDATE movimiento_leche m
JOIN (
  SELECT
    id,
    SUM(
      CASE
        WHEN tipo_movimiento IN ('SALDO_INICIAL', 'ENTRADA_RECEPCION', 'ENTRADA_DESCREME', 'AJUSTE_POSITIVO')
          THEN cantidad_litros
        WHEN tipo_movimiento IN ('SALIDA_PRODUCCION', 'SALIDA_DESCREME', 'AJUSTE_NEGATIVO')
          THEN -cantidad_litros
        ELSE 0
      END
    ) OVER (
      PARTITION BY id_tanque
      ORDER BY fecha_hora, id
      ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
    ) AS saldo_recalculado
  FROM movimiento_leche
) saldos
  ON saldos.id = m.id
SET m.saldo_resultante_litros = saldos.saldo_recalculado;
