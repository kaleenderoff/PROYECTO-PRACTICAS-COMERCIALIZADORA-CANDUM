package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.dto.response.DashboardProduccionVsEmpaqueResponse;
import com.yerman.produccion_api.domain.port.in.GestionDashboardUseCase;
import com.yerman.produccion_api.domain.port.out.DashboardRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GestionDashboardService implements GestionDashboardUseCase {

    private final DashboardRepositoryPort dashboardRepositoryPort;

    public GestionDashboardService(DashboardRepositoryPort dashboardRepositoryPort) {
        this.dashboardRepositoryPort = dashboardRepositoryPort;
    }

    @Override
    public List<DashboardProduccionVsEmpaqueResponse> obtenerProduccionVsEmpaque() {
        return dashboardRepositoryPort.obtenerProduccionVsEmpaque();
    }
}