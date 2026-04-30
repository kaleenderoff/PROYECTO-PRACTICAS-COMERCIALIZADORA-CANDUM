CREATE TABLE recepcion_leche (
  id BIGINT NOT NULL AUTO_INCREMENT,
  fecha_recepcion DATE NOT NULL,
  tipo_materia_prima VARCHAR(80) NOT NULL DEFAULT 'LECHE CRUDA',
  proveedor VARCHAR(120) NOT NULL,
  cantidad_recibida_litros DECIMAL(14,3) NOT NULL,
  recibido_por VARCHAR(120) NULL,
  id_tanque BIGINT NOT NULL,
  id_usuario BIGINT NOT NULL,
  id_movimiento_leche BIGINT NULL,
  numero_remision VARCHAR(60) NULL,
  cantidad_remision_litros DECIMAL(14,3) NULL,
  observaciones VARCHAR(500) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (id),

  KEY idx_recepcion_leche_fecha (fecha_recepcion),
  KEY idx_recepcion_leche_proveedor (proveedor),
  KEY idx_recepcion_leche_tanque (id_tanque),
  KEY idx_recepcion_leche_usuario (id_usuario),
  KEY idx_recepcion_leche_movimiento (id_movimiento_leche),

  CONSTRAINT fk_recepcion_leche_tanque
    FOREIGN KEY (id_tanque) REFERENCES tanque_leche(id),

  CONSTRAINT fk_recepcion_leche_usuario
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),

  CONSTRAINT fk_recepcion_leche_movimiento
    FOREIGN KEY (id_movimiento_leche) REFERENCES movimiento_leche(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE recepcion_leche_pesaje (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_recepcion_leche BIGINT NOT NULL,
  numero_pesaje INT NOT NULL,
  peso_bruto_kg DECIMAL(14,3) NOT NULL,
  tara_kg DECIMAL(14,3) NOT NULL,
  peso_neto_kg DECIMAL(14,3) NOT NULL,
  observaciones VARCHAR(300) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

  PRIMARY KEY (id),

  UNIQUE KEY uq_recepcion_pesaje_numero
    (id_recepcion_leche, numero_pesaje),

  KEY idx_pesaje_recepcion (id_recepcion_leche),

  CONSTRAINT fk_pesaje_recepcion_leche
    FOREIGN KEY (id_recepcion_leche)
    REFERENCES recepcion_leche(id)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE descremado_recepcion (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_recepcion_leche BIGINT NOT NULL,
  litros_descremados DECIMAL(14,3) NOT NULL,
  crema_obtenida_kg DECIMAL(14,3) NULL,
  observaciones VARCHAR(500) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

  PRIMARY KEY (id),

  KEY idx_descremado_recepcion (id_recepcion_leche),

  CONSTRAINT fk_descremado_recepcion_recepcion
    FOREIGN KEY (id_recepcion_leche)
    REFERENCES recepcion_leche(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;