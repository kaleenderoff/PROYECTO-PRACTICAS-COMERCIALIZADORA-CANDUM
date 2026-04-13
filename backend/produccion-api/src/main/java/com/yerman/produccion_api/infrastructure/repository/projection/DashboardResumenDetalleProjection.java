package com.yerman.produccion_api.infrastructure.repository.projection;

import java.math.BigDecimal;

public interface DashboardResumenDetalleProjection {

    BigDecimal getTotalKgProgramados();

    BigDecimal getTotalKgBatch();

    Long getTotalUnidadesReales();
}