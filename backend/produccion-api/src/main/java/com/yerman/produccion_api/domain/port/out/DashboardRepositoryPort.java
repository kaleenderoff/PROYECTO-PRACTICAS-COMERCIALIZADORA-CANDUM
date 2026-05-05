package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.application.dto.response.DashboardProduccionVsEmpaqueResponse;

import java.util.List;

public interface DashboardRepositoryPort {

    List<DashboardProduccionVsEmpaqueResponse> obtenerProduccionVsEmpaque();
}