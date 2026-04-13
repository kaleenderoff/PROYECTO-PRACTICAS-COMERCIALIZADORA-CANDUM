package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.application.dto.response.DashboardProduccionPorSkuResponse;
import com.yerman.produccion_api.application.dto.response.DashboardProduccionVsEmpaqueResponse;
import com.yerman.produccion_api.application.dto.response.DashboardResumenResponse;
import com.yerman.produccion_api.application.dto.response.DashboardTrazabilidadLoteResponse;
import com.yerman.produccion_api.application.dto.response.DashboardValidacionPendienteResponse;
import com.yerman.produccion_api.application.dto.response.DashboardValidacionResponse;

import java.util.List;

public interface GestionDashboardUseCase {

    DashboardResumenResponse obtenerResumenGeneral();

    List<DashboardProduccionPorSkuResponse> obtenerProduccionPorSku();

    List<DashboardProduccionVsEmpaqueResponse> obtenerProduccionVsEmpaque();

    DashboardTrazabilidadLoteResponse obtenerTrazabilidadPorLote(String lote);

    List<DashboardValidacionResponse> obtenerValidaciones();

    List<DashboardValidacionPendienteResponse> obtenerValidacionesPendientes();
}