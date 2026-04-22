-- SKUs lacteos extraidos de:
-- C:\Users\Santiago\Downloads\datos ing Astrid\REPORTE DE PRODUCCION N.  17 04 26.xlsx
-- Hoja: BASE DE PRODUCTOS ACT.
-- Se cargan 50 codigos unicos; el archivo contiene 51 filas lacteas porque 26.01.03.010 aparece duplicado.

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
  SELECT '31.02.01.001' AS codigo_sku, 'CREMA DE LECHE BOLSA * 18KL' AS descripcion, 'Crema de Leche' AS producto, 'Yerman' AS marca, 18000 AS peso_neto_gr, 'BOLSA' AS tipo_envase, 1 AS unidades_por_caja, 0 AS es_export
  UNION ALL SELECT '25.01.01.010', 'LECHE CONDENSADA EDIC. NAVIDEÑA 390GR NONA PEPA', 'Leche Condensada', 'Nona Pepa', 390, 'OTRO', 24, 0
  UNION ALL SELECT '25.01.01.014', 'LECHE CONDENSADA ESPARCIBLE 145G NONA PEPA', 'Leche Condensada', 'Nona Pepa', 145, 'OTRO', 28, 0
  UNION ALL SELECT '25.03.01.006', 'LECHE CONDENSADA ESPARCIBLE 300G NONA PEPA', 'Leche Condensada', 'Nona Pepa', 300, 'OTRO', 36, 0
  UNION ALL SELECT '25.01.01.001', 'LECHE CONDENSADA ESPARCIBLE 390GR NONA PEPA', 'Leche Condensada', 'Nona Pepa', 390, 'OTRO', 24, 0
  UNION ALL SELECT '25.01.01.002', 'LECHE CONDENSADA ESPARCIBLE 500GR NONA PEPA', 'Leche Condensada', 'Nona Pepa', 500, 'OTRO', 12, 0
  UNION ALL SELECT '25.01.01.003', 'LECHE CONDENSADA ESPARCIBLE 900GR NONA PEPA', 'Leche Condensada', 'Nona Pepa', 900, 'OTRO', 12, 0
  UNION ALL SELECT '25.01.01.004', 'LECHE CONDENSADA ESPARCIBLE 1000GR NONA PEPA', 'Leche Condensada', 'Nona Pepa', 1000, 'OTRO', 12, 0
  UNION ALL SELECT '25.01.01.005', 'LECHE CONDENSADA ESPARCIBLE 2100GR NONA PEPA', 'Leche Condensada', 'Nona Pepa', 2100, 'OTRO', 6, 0
  UNION ALL SELECT '25.01.01.012', 'LECHE CONDENSADA ESPARCIBLE 4800GR NONA PEPA', 'Leche Condensada', 'Nona Pepa', 4800, 'OTRO', 1, 0
  UNION ALL SELECT '25.01.01.013', 'LECHE CONDENSADA 25 KG NONA PEPA', 'Leche Condensada', 'Nona Pepa', 25000, 'OTRO', 1, 0
  UNION ALL SELECT '26.02.01.002', 'BIPACK LECHE COND + DULCE LECHE 390 GR NONA PEPA', 'Bipack Lacteo', 'Nona Pepa', 390, 'BIPACK', 12, 0
  UNION ALL SELECT '26.02.01.003', 'BIPACK LECHE COND + DULCE LECHE 1000 GR NONA PEPA', 'Bipack Lacteo', 'Nona Pepa', 1000, 'BIPACK', 6, 0
  UNION ALL SELECT '25.01.03.001', 'BIPACK LECHE CONDENSADA REPOSTERIA 390GR*2 NONA PEPA', 'Bipack Lacteo', 'Nona Pepa', 390, 'BIPACK', 12, 0
  UNION ALL SELECT '26.01.03.011', 'DULCE DE LECHE EDIC. NAVIDEÑA 200GR NONA PEPA', 'Dulce de Leche', 'Nona Pepa', 200, 'OTRO', 24, 0
  UNION ALL SELECT '26.01.03.004', 'DULCE LECHE 390GR NONA PEPA', 'Dulce de Leche', 'Nona Pepa', 390, 'OTRO', 24, 0
  UNION ALL SELECT '26.01.03.003', 'DULCE LECHE 250GR NONA PEPA', 'Dulce de Leche', 'Nona Pepa', 250, 'OTRO', 24, 0
  UNION ALL SELECT '26.01.03.005', 'DULCE LECHE 500GR NONA PEPA', 'Dulce de Leche', 'Nona Pepa', 500, 'OTRO', 12, 0
  UNION ALL SELECT '26.01.03.013', 'DULCE DE LECHE EDIC. NAVIDEÑA 500GR NONA PEPA', 'Dulce de Leche', 'Nona Pepa', 500, 'OTRO', 12, 0
  UNION ALL SELECT '26.01.03.006', 'DULCE LECHE 900GR NONA PEPA', 'Dulce de Leche', 'Nona Pepa', 900, 'OTRO', 12, 0
  UNION ALL SELECT '26.01.03.007', 'DULCE LECHE 1000GR NONA PEPA', 'Dulce de Leche', 'Nona Pepa', 1000, 'OTRO', 12, 0
  UNION ALL SELECT '26.01.03.001', 'DULCE LECHE 5000GR NONA PEPA', 'Dulce de Leche', 'Nona Pepa', 5000, 'OTRO', 4, 0
  UNION ALL SELECT '26.01.03.010', 'DULCE LECHE REPOSTERIA 25 KG NONA PEPA', 'Dulce de Leche', 'Nona Pepa', 25000, 'OTRO', 1, 0
  UNION ALL SELECT '26.01.03.008', 'DULCE LECHE 900GR PANADERIA NONA PEPA', 'Dulce de Leche', 'Nona Pepa', 900, 'OTRO', 12, 0
  UNION ALL SELECT '26.01.03.002', 'DULCE LECHE 5000GR PANADERIA NONA PEPA', 'Dulce de Leche', 'Nona Pepa', 5000, 'OTRO', 4, 0
  UNION ALL SELECT '26.03.02.003', 'DULCE DE LECHE 390GR GRANNY', 'Dulce de Leche', 'Granny', 390, 'OTRO', 24, 0
  UNION ALL SELECT '26.03.02.002', 'DULCE DE LECHE 250GR GRANNY', 'Dulce de Leche', 'Granny', 250, 'OTRO', 24, 0
  UNION ALL SELECT '26.03.02.004', 'DULCE DE LECHE 500GR GRANNY', 'Dulce de Leche', 'Granny', 500, 'OTRO', 12, 0
  UNION ALL SELECT '26.03.02.005', 'DULCE DE LECHE 900GR GRANNY', 'Dulce de Leche', 'Granny', 900, 'OTRO', 12, 0
  UNION ALL SELECT '25.03.01.001', 'LECHE CONDENSADA GRANNY PREMIUM 390 GRM', 'Leche Condensada', 'Granny', 390, 'OTRO', 24, 0
  UNION ALL SELECT '25.03.01.002', 'LECHE CONDENSADA GRANNY PREMIUM 500 GRM', 'Leche Condensada', 'Granny', 500, 'OTRO', 12, 0
  UNION ALL SELECT '25.03.01.003', 'LECHE CONDENSADA GRANNY PREMIUM 900 GRM', 'Leche Condensada', 'Granny', 900, 'OTRO', 12, 0
  UNION ALL SELECT '25.03.01.004', 'LECHE CONDENSADA GRANNY PREMIUM 1000 GRM', 'Leche Condensada', 'Granny', 1000, 'OTRO', 12, 0
  UNION ALL SELECT '25.03.01.008', 'LECHE CONDENSADA GRANNY 4800 GRM', 'Leche Condensada', 'Granny', 4800, 'OTRO', 1, 0
  UNION ALL SELECT '25.03.03.001', 'BIPACK LECHE COND. DULCE LECHE 390 GR GRANNY', 'Bipack Lacteo', 'Granny', 390, 'BIPACK', 12, 0
  UNION ALL SELECT '26.03.01.001', 'AREQUIPE GRANNY ESPARCIBLE 390 GRM', 'Arequipe', 'Granny', 390, 'OTRO', 24, 0
  UNION ALL SELECT '26.03.01.002', 'AREQUIPE GRANNY ESPARCIBLE 250 GRM', 'Arequipe', 'Granny', 250, 'OTRO', 24, 0
  UNION ALL SELECT '26.03.01.003', 'AREQUIPE GRANNY ESPARCIBLE 500 GRM', 'Arequipe', 'Granny', 500, 'OTRO', 12, 0
  UNION ALL SELECT '26.03.02.006', 'DULCE DE LECHE GRANNY 5000 GRM', 'Dulce de Leche', 'Granny', 5000, 'OTRO', 1, 0
  UNION ALL SELECT '26.03.01.004', 'AREQUIPE GRANNY ESPARCIBLE 900 GRM', 'Arequipe', 'Granny', 900, 'OTRO', 12, 0
  UNION ALL SELECT '26.04.01.001', 'AREQUIPE LA HUERTA ESPARCIBLE 250 G', 'Arequipe', 'La Huerta', 250, 'OTRO', 24, 0
  UNION ALL SELECT '25.03.01.005', 'LECHE CONDENSADA GRANNY X 300 G', 'Leche Condensada', 'Granny', 300, 'OTRO', 36, 0
  UNION ALL SELECT '25.03.04', 'LECHE CONDENSADAESPARCIBLE 4800GR NP EXPORT', 'Leche Condensada', 'Nona Pepa', 4800, 'OTRO', 4, 1
  UNION ALL SELECT '25.03.02', 'LECHE CONDENSADA ESPARCIBLE 390GR NONA PEPA EXPORT', 'Leche Condensada', 'Nona Pepa', 390, 'OTRO', 24, 1
  UNION ALL SELECT '26.03.03', 'DULCE DE LECHE REPOSTERIA 5000GR NP EXPORT', 'Dulce de Leche', 'Nona Pepa', 5000, 'OTRO', 4, 1
  UNION ALL SELECT '26.03.01', 'DULCE DE LECHE REPOSTERIA 390GR NP EXPORT', 'Dulce de Leche', 'Nona Pepa', 390, 'OTRO', 24, 1
  UNION ALL SELECT '26.03.02', 'DULCE DE LECHE REPOSTERIA 250GR NP EXPORT', 'Dulce de Leche', 'Nona Pepa', 250, 'OTRO', 24, 1
  UNION ALL SELECT '26.01.03.014', 'DULCE LECHE 5000GR LAMAJE', 'Dulce de Leche', 'Lamaje', 5000, 'OTRO', 1, 0
  UNION ALL SELECT '26.01.03.015', 'DULCE LECHE EDIC. NAVIDEÑA 200GR LAMAJE', 'Dulce de Leche', 'Lamaje', 200, 'OTRO', 24, 0
  UNION ALL SELECT '25.03.01.010', 'LECHE CONDENSADA AMANECER 300G DOYPACK *36und', 'Leche Condensada', 'Amanecer', 300, 'DOYPACK', 36, 0
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
