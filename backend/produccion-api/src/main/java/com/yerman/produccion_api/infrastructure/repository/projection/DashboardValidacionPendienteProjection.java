package com.yerman.produccion_api.infrastructure.repository.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DashboardValidacionPendienteProjection {

    Long getIdDetalleProduccion();

    Long getIdProduccion();

    String getNumeroLote();

    LocalDate getFechaProduccion();

    String getEstadoProduccion();

    Long getIdProducto();

    String getNombreProducto();

    Integer getNumBatch();

    BigDecimal getKgProgramados();

    BigDecimal getKgBatch();

    Integer getUnidadesReales();

    BigDecimal getRendimientoPct();
}