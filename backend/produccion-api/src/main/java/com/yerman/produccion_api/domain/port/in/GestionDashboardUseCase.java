package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.application.dto.response.DashboardProduccionVsEmpaqueResponse;

import java.util.List;

public interface GestionDashboardUseCase {

    List<DashboardProduccionVsEmpaqueResponse> obtenerProduccionVsEmpaque();
}