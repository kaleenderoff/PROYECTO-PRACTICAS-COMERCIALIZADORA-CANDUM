package com.yerman.produccion_api.infrastructure.repository.projection;

import java.math.BigDecimal;

public interface DashboardProduccionPorSkuProjection {

    Long getIdProductoTerminado();

    String getSku();

    String getNombreComercial();

    String getReferencia();

    Long getTotalUnidades();

    Long getTotalCajas();

    BigDecimal getTotalPesoKg();

    Long getTotalRegistrosEmpaque();
}