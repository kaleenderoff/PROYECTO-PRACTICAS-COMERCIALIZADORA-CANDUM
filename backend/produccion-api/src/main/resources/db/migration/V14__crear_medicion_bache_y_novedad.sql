CREATE TABLE medicion_bache (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_ejecucion BIGINT NOT NULL,
  numero_bache VARCHAR(10) NOT NULL,
  tipo_medicion ENUM('BACHE', 'MEZCLA', 'TANDA') NOT NULL DEFAULT 'BACHE',
  grados_brix DECIMAL(5,2) NULL,
  ph DECIMAL(4,2) NULL,
  hora_medicion TIME NULL,
  id_auxiliar_calidad BIGINT NOT NULL,
  observaciones TEXT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_medicion_bache (id_ejecucion, numero_bache, tipo_medicion),
  KEY idx_medicion_bache_ejecucion (id_ejecucion),
  KEY idx_medicion_bache_auxiliar (id_auxiliar_calidad),
  CONSTRAINT fk_medicion_bache_ejecucion
    FOREIGN KEY (id_ejecucion) REFERENCES ejecucion_produccion (id) ON DELETE CASCADE,
  CONSTRAINT fk_medicion_bache_auxiliar
    FOREIGN KEY (id_auxiliar_calidad) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE novedad_produccion (
  id BIGINT NOT NULL AUTO_INCREMENT,
  id_ejecucion BIGINT NOT NULL,
  tipo_novedad ENUM('BAJA_GRASA', 'FALLA_CALDERA', 'RETRASO_LECHE', 'FALLA_EQUIPO', 'BRIX_FUERA_RANGO', 'REPROCESO', 'CAMBIO_PROCESO', 'OTRO') NOT NULL,
  descripcion TEXT NOT NULL,
  impacto_rendimiento_pct DECIMAL(5,2) NULL,
  hora_ocurrencia TIME NULL,
  id_jefe_linea BIGINT NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_novedad_ejecucion (id_ejecucion),
  KEY idx_novedad_jefe_linea (id_jefe_linea),
  CONSTRAINT fk_novedad_ejecucion
    FOREIGN KEY (id_ejecucion) REFERENCES ejecucion_produccion (id) ON DELETE CASCADE,
  CONSTRAINT fk_novedad_jefe_linea
    FOREIGN KEY (id_jefe_linea) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
