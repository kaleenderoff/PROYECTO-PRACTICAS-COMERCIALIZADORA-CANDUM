-- V10: catalogo de SKUs producido desde los formatos de lacteos.

CREATE TABLE catalogo_sku (
  id BIGINT NOT NULL AUTO_INCREMENT,
  codigo_sku VARCHAR(30) NOT NULL,
  descripcion VARCHAR(200) NOT NULL,
  id_producto BIGINT NOT NULL,
  id_marca BIGINT NOT NULL,
  peso_neto_gr INT NOT NULL,
  tipo_envase ENUM(
    'DISPENSADOR',
    'DOYPACK',
    'GARRAFA',
    'BALDE',
    'TAZA',
    'TETERO',
    'BIPACK',
    'BOLSA',
    'OTRO'
  ) NOT NULL DEFAULT 'OTRO',
  unidades_por_caja INT NULL,
  es_export TINYINT(1) NOT NULL DEFAULT 0,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_catalogo_sku_codigo (codigo_sku),
  KEY idx_catalogo_sku_producto (id_producto),
  KEY idx_catalogo_sku_marca (id_marca),
  CONSTRAINT fk_catalogo_sku_producto
    FOREIGN KEY (id_producto) REFERENCES catalogo_producto (id),
  CONSTRAINT fk_catalogo_sku_marca
    FOREIGN KEY (id_marca) REFERENCES marca (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
