CREATE TABLE catalogo_producto (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(150) NOT NULL,
  id_linea BIGINT NOT NULL,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_catalogo_producto_linea_nombre (id_linea, nombre),
  KEY idx_catalogo_producto_linea (id_linea),
  CONSTRAINT fk_catalogo_producto_linea
    FOREIGN KEY (id_linea) REFERENCES catalogo_linea (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
