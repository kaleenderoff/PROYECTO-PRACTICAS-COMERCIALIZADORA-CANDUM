CREATE TABLE marmita (
  id BIGINT NOT NULL AUTO_INCREMENT,
  numero INT NOT NULL,
  nombre VARCHAR(80) NOT NULL,
  activa BOOLEAN NOT NULL DEFAULT TRUE,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (id),
  UNIQUE KEY uq_marmita_numero (numero)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO marmita (numero, nombre, activa) VALUES
(1, 'Marmita 1', TRUE),
(2, 'Marmita 2', TRUE),
(3, 'Marmita 3', TRUE),
(4, 'Marmita 4', TRUE),
(5, 'Marmita 5', TRUE),
(6, 'Marmita 6', TRUE);


CREATE TABLE produccion_lactea (
  id BIGINT NOT NULL AUTO_INCREMENT,

  fecha_produccion DATE NOT NULL,
  producto VARCHAR(120) NOT NULL,

  id_tanque BIGINT NOT NULL,
  id_usuario BIGINT NOT NULL,

  observaciones VARCHAR(500) NULL,

  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (id),

  KEY idx_produccion_lactea_fecha (fecha_produccion),
  KEY idx_produccion_lactea_producto (producto),
  KEY idx_produccion_lactea_tanque (id_tanque),
  KEY idx_produccion_lactea_usuario (id_usuario),

  CONSTRAINT fk_produccion_lactea_tanque
    FOREIGN KEY (id_tanque) REFERENCES tanque_leche(id),

  CONSTRAINT fk_produccion_lactea_usuario
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE produccion_lactea_batch (
  id BIGINT NOT NULL AUTO_INCREMENT,

  id_produccion_lactea BIGINT NOT NULL,
  numero_batch INT NOT NULL,
  id_marmita BIGINT NOT NULL,

  litros_consumidos DECIMAL(14,3) NOT NULL,
  kilos_producidos DECIMAL(14,3) NOT NULL,
  rendimiento DECIMAL(10,4) NULL,

  id_movimiento_leche BIGINT NULL,

  observaciones VARCHAR(500) NULL,

  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (id),

  UNIQUE KEY uq_produccion_batch
    (id_produccion_lactea, numero_batch),

  KEY idx_batch_produccion (id_produccion_lactea),
  KEY idx_batch_marmita (id_marmita),
  KEY idx_batch_movimiento (id_movimiento_leche),

  CONSTRAINT fk_batch_produccion_lactea
    FOREIGN KEY (id_produccion_lactea)
    REFERENCES produccion_lactea(id)
    ON DELETE CASCADE,

  CONSTRAINT fk_batch_marmita
    FOREIGN KEY (id_marmita)
    REFERENCES marmita(id),

  CONSTRAINT fk_batch_movimiento_leche
    FOREIGN KEY (id_movimiento_leche)
    REFERENCES movimiento_leche(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;