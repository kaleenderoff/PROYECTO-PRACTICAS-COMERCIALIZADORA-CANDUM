-- Datos iniciales idempotentes para los catalogos base.
-- Los horarios de turnos quedan como base operativa y se ajustan cuando Astrid confirme el horario exacto.

INSERT INTO turno (nombre, hora_inicio, hora_fin, activo)
VALUES
  ('Turno 1', '06:00:00', '14:00:00', 1),
  ('Turno 2', '14:00:00', '22:00:00', 1)
ON DUPLICATE KEY UPDATE
  hora_inicio = VALUES(hora_inicio),
  hora_fin = VALUES(hora_fin),
  activo = VALUES(activo);

INSERT INTO proveedor (nombre, activo)
VALUES
  ('La Ley', 1),
  ('Palmeras', 1),
  ('Acacias', 1)
ON DUPLICATE KEY UPDATE
  activo = VALUES(activo);

INSERT INTO marca (nombre, es_propia, activo)
VALUES
  ('Nona Pepa', 1, 1),
  ('Granny', 1, 1),
  ('La Huerta', 1, 1),
  ('Amanecer', 1, 1),
  ('Lamaje', 1, 1),
  ('Yerman', 1, 1)
ON DUPLICATE KEY UPDATE
  es_propia = VALUES(es_propia),
  activo = VALUES(activo);

INSERT INTO catalogo_linea (nombre, activo)
VALUES
  ('Lacteos', 1),
  ('Mayonesa', 1),
  ('Salsas', 1),
  ('Aceite', 1),
  ('Margarina', 1),
  ('Azucar', 1)
ON DUPLICATE KEY UPDATE
  activo = VALUES(activo);

INSERT INTO catalogo_producto (nombre, id_linea, activo)
SELECT producto.nombre, linea.id, 1
FROM (
  SELECT 'Leche Condensada' AS nombre, 'Lacteos' AS linea
  UNION ALL SELECT 'Dulce de Leche', 'Lacteos'
  UNION ALL SELECT 'Arequipe', 'Lacteos'
  UNION ALL SELECT 'Crema de Leche', 'Lacteos'
  UNION ALL SELECT 'Bipack Lacteo', 'Lacteos'
) producto
JOIN catalogo_linea linea ON linea.nombre = producto.linea
ON DUPLICATE KEY UPDATE
  activo = VALUES(activo);
