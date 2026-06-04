-- Sincroniza el estado de programaciones con el estado de sus órdenes asociadas.
-- Las programaciones cuyas órdenes están CANCELADAS deben quedar CANCELADAS
-- para liberar el constraint de unicidad (fecha_produccion, id_linea, id_turno, id_producto).
UPDATE programacion_produccion p
    JOIN orden_produccion op ON op.id_programacion = p.id
SET p.estado = 'CANCELADA'
WHERE op.estado = 'CANCELADA'
  AND p.estado != 'CANCELADA';
