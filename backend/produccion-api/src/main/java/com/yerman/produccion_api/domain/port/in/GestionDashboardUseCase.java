package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.application.dto.response.DashboardResumenResponse;

public interface GestionDashboardUseCase {

    DashboardResumenResponse obtenerResumenGeneral();
}