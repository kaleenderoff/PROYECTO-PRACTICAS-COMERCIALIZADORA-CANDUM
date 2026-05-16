ALTER TABLE orden_produccion
ADD COLUMN id_tanque_leche BIGINT,
ADD CONSTRAINT fk_orden_produccion_tanque
    FOREIGN KEY (id_tanque_leche) REFERENCES tanque_leche(id);
