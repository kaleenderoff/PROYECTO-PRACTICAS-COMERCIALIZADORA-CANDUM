CREATE TABLE ejecucion_batch (
    id BIGINT NOT NULL AUTO_INCREMENT,
    id_orden_produccion BIGINT NOT NULL,
    numero_batch INT NOT NULL,
    id_marmita BIGINT NOT NULL,
    kg_entrada DECIMAL(14,3) NOT NULL,
    kg_producidos DECIMAL(14,3) DEFAULT NULL,
    rendimiento_pct DECIMAL(8,3) DEFAULT NULL,
    estado ENUM('EN_PROCESO','FINALIZADO','CON_NOVEDAD') NOT NULL DEFAULT 'EN_PROCESO',
    observaciones VARCHAR(500) DEFAULT NULL,
    fecha_inicio DATETIME DEFAULT NULL,
    fecha_fin DATETIME DEFAULT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    UNIQUE KEY uq_orden_numero_batch (id_orden_produccion, numero_batch),
    KEY idx_ejecucion_batch_orden (id_orden_produccion),
    KEY idx_ejecucion_batch_marmita (id_marmita),
    KEY idx_ejecucion_batch_estado (estado),

    CONSTRAINT fk_ejecucion_batch_orden
        FOREIGN KEY (id_orden_produccion)
        REFERENCES orden_produccion(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_ejecucion_batch_marmita
        FOREIGN KEY (id_marmita)
        REFERENCES marmita(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
