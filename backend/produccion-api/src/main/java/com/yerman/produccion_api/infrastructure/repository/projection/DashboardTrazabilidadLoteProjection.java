package com.yerman.produccion_api.infrastructure.repository.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DashboardTrazabilidadLoteProjection {

    String getLote();

    String getProducto();

    LocalDate getFechaProduccion();

    Integer getNumeroBatch();

    BigDecimal getKilosProducidos();

    BigDecimal getKilosDisponibles();

    BigDecimal getKilosEmpacados();

    String getEstadoProductoTerminado();
}