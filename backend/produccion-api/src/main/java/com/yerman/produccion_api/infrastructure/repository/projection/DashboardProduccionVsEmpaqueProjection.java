package com.yerman.produccion_api.infrastructure.repository.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DashboardProduccionVsEmpaqueProjection {

    LocalDate getFecha();

    BigDecimal getTotalProducido();

    BigDecimal getTotalEmpaquetado();
}