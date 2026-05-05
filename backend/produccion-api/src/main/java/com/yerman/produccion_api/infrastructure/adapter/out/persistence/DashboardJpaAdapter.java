package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.application.dto.response.DashboardProduccionVsEmpaqueResponse;
import com.yerman.produccion_api.domain.port.out.DashboardRepositoryPort;
import com.yerman.produccion_api.infrastructure.repository.DashboardJpaRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DashboardJpaAdapter implements DashboardRepositoryPort {

    private final DashboardJpaRepository repository;

    public DashboardJpaAdapter(DashboardJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<DashboardProduccionVsEmpaqueResponse> obtenerProduccionVsEmpaque() {
        return repository.obtenerProduccionVsEmpaque()
                .stream()
                .map(p -> new DashboardProduccionVsEmpaqueResponse(
                        p.getFecha(),
                        p.getTotalProducido(),
                        p.getTotalEmpaquetado(),
                        p.getTotalProducido().subtract(
                                p.getTotalEmpaquetado() != null ? p.getTotalEmpaquetado() : BigDecimal.ZERO)))
                .toList();
    }
}