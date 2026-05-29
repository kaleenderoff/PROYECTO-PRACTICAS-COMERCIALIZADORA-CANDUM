-- Actualiza los tipos de envase de los SKUs de Leche Condensada.
-- Producto: Leche Condensada / id_producto = 1.
-- Valores basados en la estructura de formatos usada en el Excel de planeacion.

UPDATE catalogo_sku
SET tipo_envase = 'DISPENSADOR'
WHERE id_producto = 1
  AND peso_neto_gr IN (145, 390, 1000)
  AND descripcion LIKE '%LECHE CONDENSADA%';

UPDATE catalogo_sku
SET tipo_envase = 'DOYPACK'
WHERE id_producto = 1
  AND peso_neto_gr = 300
  AND descripcion LIKE '%LECHE CONDENSADA%';

UPDATE catalogo_sku
SET tipo_envase = 'TAZA'
WHERE id_producto = 1
  AND peso_neto_gr IN (500, 900)
  AND descripcion LIKE '%LECHE CONDENSADA%';

UPDATE catalogo_sku
SET tipo_envase = 'GARRAFA'
WHERE id_producto = 1
  AND peso_neto_gr IN (2100, 4800)
  AND descripcion LIKE '%LECHE CONDENSADA%';