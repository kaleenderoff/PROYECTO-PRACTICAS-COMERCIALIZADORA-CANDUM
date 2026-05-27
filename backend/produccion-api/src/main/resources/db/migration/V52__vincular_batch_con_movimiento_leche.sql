-- Vincular ejecucion_batch con movimiento_leche para trazabilidad de consumo de leche

SET @col_exists := (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'ejecucion_batch'
      AND COLUMN_NAME = 'id_movimiento_leche'
);

SET @sql := IF(
    @col_exists = 0,
    'ALTER TABLE ejecucion_batch ADD COLUMN id_movimiento_leche BIGINT NULL',
    'SELECT 1'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


SET @fk_exists := (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS
    WHERE CONSTRAINT_SCHEMA = DATABASE()
      AND CONSTRAINT_NAME = 'fk_ejecucion_batch_movimiento_leche'
);

SET @sql := IF(
    @fk_exists = 0,
    'ALTER TABLE ejecucion_batch ADD CONSTRAINT fk_ejecucion_batch_movimiento_leche FOREIGN KEY (id_movimiento_leche) REFERENCES movimiento_leche(id)',
    'SELECT 1'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;