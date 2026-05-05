-- 1. Agregar columna (primero NULL por buenas prácticas)
ALTER TABLE empaque_lacteo
ADD COLUMN id_produccion_lactea_batch BIGINT;

-- 2. Crear FK
ALTER TABLE empaque_lacteo
ADD CONSTRAINT fk_empaque_batch
FOREIGN KEY (id_produccion_lactea_batch)
REFERENCES produccion_lactea_batch(id);

-- 3. Volver la columna obligatoria (solo porque es base limpia)
ALTER TABLE empaque_lacteo
MODIFY COLUMN id_produccion_lactea_batch BIGINT NOT NULL;