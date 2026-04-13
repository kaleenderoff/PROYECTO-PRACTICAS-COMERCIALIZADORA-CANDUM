package com.yerman.produccion_api.infrastructure.repository.projection;

import java.math.BigDecimal;

public interface DashboardResumenEmpaqueProjection {

    Long getTotalUnidadesEmpacadas();

    Long getTotalCajasEmpacadas();

    BigDecimal getTotalPesoEmpacadoKg();
}