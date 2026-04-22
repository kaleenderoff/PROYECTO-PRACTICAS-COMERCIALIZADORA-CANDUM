-- SKUs lacteos iniciales para habilitar pruebas de flujo.
-- La lista completa de 51 SKUs se cargara cuando este el Excel maestro definitivo en el repo.

INSERT INTO catalogo_sku (
  codigo_sku,
  descripcion,
  id_producto,
  id_marca,
  peso_neto_gr,
  tipo_envase,
  unidades_por_caja,
  es_export,
  activo
)
SELECT sku.codigo_sku,
       sku.descripcion,
       producto.id,
       marca.id,
       sku.peso_neto_gr,
       sku.tipo_envase,
       sku.unidades_por_caja,
       sku.es_export,
       1
FROM (
  SELECT 'DL-NP-DISP-145' AS codigo_sku, 'Dulce de Leche Nona Pepa Dispensador 145g' AS descripcion,
         'Dulce de Leche' AS producto, 'Nona Pepa' AS marca, 145 AS peso_neto_gr,
         'DISPENSADOR' AS tipo_envase, 24 AS unidades_por_caja, 0 AS es_export
  UNION ALL SELECT 'DL-NP-DOY-390', 'Dulce de Leche Nona Pepa Doypack 390g',
         'Dulce de Leche', 'Nona Pepa', 390, 'DOYPACK', 12, 0
  UNION ALL SELECT 'DL-GR-TAZA-1000', 'Dulce de Leche Granny Taza 1000g',
         'Dulce de Leche', 'Granny', 1000, 'TAZA', 6, 0
  UNION ALL SELECT 'DL-NP-BALDE-4800', 'Dulce de Leche Nona Pepa Balde 4800g',
         'Dulce de Leche', 'Nona Pepa', 4800, 'BALDE', 4, 0
  UNION ALL SELECT '31.02.01.001', 'Crema de Leche Bolsa 18kg',
         'Crema de Leche', 'Yerman', 18000, 'BOLSA', 1, 0
) sku
JOIN catalogo_producto producto ON producto.nombre = sku.producto
JOIN marca ON marca.nombre = sku.marca
ON DUPLICATE KEY UPDATE
  descripcion = VALUES(descripcion),
  id_producto = VALUES(id_producto),
  id_marca = VALUES(id_marca),
  peso_neto_gr = VALUES(peso_neto_gr),
  tipo_envase = VALUES(tipo_envase),
  unidades_por_caja = VALUES(unidades_por_caja),
  es_export = VALUES(es_export),
  activo = VALUES(activo);
