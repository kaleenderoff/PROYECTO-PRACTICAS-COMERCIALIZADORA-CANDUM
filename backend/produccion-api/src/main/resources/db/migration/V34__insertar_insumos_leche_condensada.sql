INSERT INTO insumo (codigo, nombre, descripcion, tipo, unidad_medida, stock_minimo, activo)
SELECT 'INS-LECHE-LIQUIDA', 'LECHE LIQUIDA', 'Materia prima leche liquida', 'MATERIA_PRIMA', 'kg', 0, true
WHERE NOT EXISTS (SELECT 1 FROM insumo WHERE codigo = 'INS-LECHE-LIQUIDA');

INSERT INTO insumo (codigo, nombre, descripcion, tipo, unidad_medida, stock_minimo, activo)
SELECT 'INS-AZUCAR', 'AZUCAR', 'Azucar para formulacion lactea', 'MATERIA_PRIMA', 'kg', 0, true
WHERE NOT EXISTS (SELECT 1 FROM insumo WHERE codigo = 'INS-AZUCAR');

INSERT INTO insumo (codigo, nombre, descripcion, tipo, unidad_medida, stock_minimo, activo)
SELECT 'INS-ALMIDON-ARGO', 'ALMIDON ARGO', 'Almidon Argo', 'ADITIVO', 'kg', 0, true
WHERE NOT EXISTS (SELECT 1 FROM insumo WHERE codigo = 'INS-ALMIDON-ARGO');

INSERT INTO insumo (codigo, nombre, descripcion, tipo, unidad_medida, stock_minimo, activo)
SELECT 'INS-BICARBONATO-SODIO', 'BICARBONATO DE SODIO', 'Bicarbonato de sodio', 'ADITIVO', 'kg', 0, true
WHERE NOT EXISTS (SELECT 1 FROM insumo WHERE codigo = 'INS-BICARBONATO-SODIO');

INSERT INTO insumo (codigo, nombre, descripcion, tipo, unidad_medida, stock_minimo, activo)
SELECT 'INS-LACTASA', 'LACTASA', 'Lactasa', 'ADITIVO', 'kg', 0, true
WHERE NOT EXISTS (SELECT 1 FROM insumo WHERE codigo = 'INS-LACTASA');

INSERT INTO insumo (codigo, nombre, descripcion, tipo, unidad_medida, stock_minimo, activo)
SELECT 'INS-CARRAGENINA', 'CARRAGENINA', 'Carragenina', 'ADITIVO', 'kg', 0, true
WHERE NOT EXISTS (SELECT 1 FROM insumo WHERE codigo = 'INS-CARRAGENINA');

INSERT INTO insumo (codigo, nombre, descripcion, tipo, unidad_medida, stock_minimo, activo)
SELECT 'INS-ANTIESPUMANTE', 'ANTIESPUMANTE', 'Antiespumante', 'ADITIVO', 'kg', 0, true
WHERE NOT EXISTS (SELECT 1 FROM insumo WHERE codigo = 'INS-ANTIESPUMANTE');

INSERT INTO insumo (codigo, nombre, descripcion, tipo, unidad_medida, stock_minimo, activo)
SELECT 'INS-NATAMICINA', 'NATAMICINA', 'Natamicina', 'ADITIVO', 'kg', 0, true
WHERE NOT EXISTS (SELECT 1 FROM insumo WHERE codigo = 'INS-NATAMICINA');

INSERT INTO insumo (codigo, nombre, descripcion, tipo, unidad_medida, stock_minimo, activo)
SELECT 'INS-SORBATO', 'SORBATO', 'Sorbato', 'ADITIVO', 'kg', 0, true
WHERE NOT EXISTS (SELECT 1 FROM insumo WHERE codigo = 'INS-SORBATO');

INSERT INTO insumo (codigo, nombre, descripcion, tipo, unidad_medida, stock_minimo, activo)
SELECT 'INS-BENZOATO', 'BENZOATO', 'Benzoato', 'ADITIVO', 'kg', 0, true
WHERE NOT EXISTS (SELECT 1 FROM insumo WHERE codigo = 'INS-BENZOATO');

INSERT INTO insumo (codigo, nombre, descripcion, tipo, unidad_medida, stock_minimo, activo)
SELECT 'INS-CITRATO-SODIO', 'CITRATO DE SODIO', 'Citrato de sodio', 'ADITIVO', 'kg', 0, true
WHERE NOT EXISTS (SELECT 1 FROM insumo WHERE codigo = 'INS-CITRATO-SODIO');