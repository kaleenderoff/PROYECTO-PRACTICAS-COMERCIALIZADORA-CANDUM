package com.yerman.produccion_api.infrastructure.repository.projection;

import java.math.BigDecimal;

public interface DashboardProduccionVsEmpaqueProjection {

    Long getIdDetalleProduccion();

    Long getIdProduccion();

    String getNumeroLoteProduccion();

    Long getIdProducto();

    String getNombreProducto();

    Integer getNumBatch();

    BigDecimal getKgProgramados();

    BigDecimal getKgBatch();

    Integer getUnidadesReales();

    Long getUnidadesEmpacadas();

    Long getCajasEmpacadas();

    BigDecimal getPesoEmpacadoKg();
}