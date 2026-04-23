CREATE TABLE usuario (
  id_usuario BIGINT NOT NULL AUTO_INCREMENT,
  cc VARCHAR(20) NOT NULL,
  primer_nombre VARCHAR(100) NOT NULL,
  segundo_nombre VARCHAR(100) NULL,
  primer_apellido VARCHAR(100) NOT NULL,
  segundo_apellido VARCHAR(100) NULL,
  email VARCHAR(150) NULL,
  password_hash VARCHAR(255) NOT NULL,
  rol ENUM('JEFE_PRODUCCION', 'JEFE_LINEA', 'AUXILIAR_CALIDAD', 'ANALISTA_LACTEOS', 'JEFE_PLANTA', 'GERENCIA', 'ADMIN') NOT NULL,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_usuario),
  UNIQUE KEY uq_usuario_cc (cc),
  UNIQUE KEY uq_usuario_email (email),
  CONSTRAINT chk_usuario_cc_formato CHECK (REGEXP_LIKE(cc, '^[0-9]+$'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE log_auditoria (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_usuario BIGINT NOT NULL,
  accion VARCHAR(100) NOT NULL,
  entidad_afectada VARCHAR(100) NOT NULL,
  id_registro_afectado BIGINT NULL,
  detalle TEXT NULL,
  fecha_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_log_auditoria_usuario (id_usuario),
  CONSTRAINT fk_log_auditoria_usuario
    FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
