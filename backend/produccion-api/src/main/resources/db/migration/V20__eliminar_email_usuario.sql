-- eliminar índice primero
ALTER TABLE usuario DROP INDEX uq_usuario_email;

-- luego eliminar columna
ALTER TABLE usuario DROP COLUMN email;