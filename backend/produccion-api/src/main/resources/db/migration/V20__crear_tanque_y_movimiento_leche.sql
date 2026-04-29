CREATE TABLE tanque_leche (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  tipo VARCHAR(30) NOT NULL,
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_tanque_leche_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE movimiento_leche (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_tanque BIGINT NOT NULL,
  tipo_movimiento VARCHAR(30) NOT NULL,
  fecha_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  cantidad_litros DECIMAL(14,3) NOT NULL,
  saldo_resultante_litros DECIMAL(14,3) NOT NULL,
  id_usuario BIGINT NOT NULL,
  referencia VARCHAR(100) NULL,
  observaciones VARCHAR(500) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_movimiento_leche_tanque (id_tanque),
  KEY idx_movimiento_leche_fecha (fecha_hora),
  KEY idx_movimiento_leche_tipo (tipo_movimiento),
  KEY idx_movimiento_leche_usuario (id_usuario),
  CONSTRAINT fk_movimiento_leche_tanque
    FOREIGN KEY (id_tanque) REFERENCES tanque_leche(id),
  CONSTRAINT fk_movimiento_leche_usuario
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO tanque_leche (nombre, tipo, activo)
VALUES
('Tanque de refrigeración', 'REFRIGERACION', TRUE),
('Tanque de conservación', 'CONSERVACION', TRUE);