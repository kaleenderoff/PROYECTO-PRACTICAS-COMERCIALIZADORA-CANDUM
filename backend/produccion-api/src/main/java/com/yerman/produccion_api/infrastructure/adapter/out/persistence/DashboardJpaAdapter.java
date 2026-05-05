package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.application.dto.response.DashboardProduccionVsEmpaqueResponse;
import com.yerman.produccion_api.application.dto.response.DashboardTrazabilidadLoteResponse;
import com.yerman.produccion_api.domain.port.out.DashboardRepositoryPort;
import com.yerman.produccion_api.infrastructure.repository.DashboardJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.projection.DashboardTrazabilidadLoteProjection;
import org.springframework.stereotype.Component;

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
                        p.getTotalProducido().subtract(p.getTotalEmpaquetado())))
                .toList();
    }

    @Override
    public DashboardTrazabilidadLoteResponse obtenerTrazabilidadPorLote(String lote) {
        DashboardTrazabilidadLoteProjection p = repository
                .obtenerTrazabilidadPorLote(lote)
                .orElseThrow(() -> new RuntimeException("Lote no encontrado"));

        return new DashboardTrazabilidadLoteResponse(
                p.getLote(),
                p.getProducto(),
                p.getFechaProduccion(),
                p.getNumeroBatch(),
                p.getKilosProducidos(),
                p.getKilosDisponibles(),
                p.getKilosEmpacados(),
                p.getEstadoProductoTerminado());
    }
}