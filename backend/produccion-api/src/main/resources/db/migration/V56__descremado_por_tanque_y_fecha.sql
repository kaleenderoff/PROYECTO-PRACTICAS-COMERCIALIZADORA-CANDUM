ALTER TABLE descremado_recepcion
ADD COLUMN fecha_descremado DATE NULL AFTER id_recepcion_leche;

ALTER TABLE descremado_recepcion
ADD COLUMN id_tanque_origen BIGINT NULL AFTER fecha_descremado;

ALTER TABLE descremado_recepcion
ADD COLUMN id_usuario BIGINT NULL AFTER id_tanque_destino;

ALTER TABLE descremado_recepcion
ADD CONSTRAINT fk_descremado_tanque_origen
FOREIGN KEY (id_tanque_origen)
REFERENCES tanque_leche(id);

ALTER TABLE descremado_recepcion
ADD CONSTRAINT fk_descremado_usuario
FOREIGN KEY (id_usuario)
REFERENCES usuario(id_usuario);

UPDATE descremado_recepcion dr
INNER JOIN recepcion_leche rl
    ON rl.id = dr.id_recepcion_leche
SET
    dr.fecha_descremado = rl.fecha_recepcion,
    dr.id_tanque_origen = rl.id_tanque,
    dr.id_usuario = rl.id_usuario;

ALTER TABLE descremado_recepcion
MODIFY COLUMN fecha_descremado DATE NOT NULL;

ALTER TABLE descremado_recepcion
MODIFY COLUMN id_tanque_origen BIGINT NOT NULL;

ALTER TABLE descremado_recepcion
MODIFY COLUMN id_usuario BIGINT NOT NULL;

ALTER TABLE descremado_recepcion
MODIFY COLUMN id_recepcion_leche BIGINT NULL;