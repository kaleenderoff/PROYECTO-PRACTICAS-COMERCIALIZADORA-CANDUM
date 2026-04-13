package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.application.dto.response.DashboardProduccionPorSkuResponse;
import com.yerman.produccion_api.application.dto.response.DashboardResumenResponse;

import java.util.List;

public interface GestionDashboardUseCase {

    DashboardResumenResponse obtenerResumenGeneral();

    List<DashboardProduccionPorSkuResponse> obtenerProduccionPorSku();
}