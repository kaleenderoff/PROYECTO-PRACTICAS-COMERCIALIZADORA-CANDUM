package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.dto.response.*;
import com.yerman.produccion_api.domain.port.in.GestionDashboardUseCase;
import com.yerman.produccion_api.domain.port.out.DashboardRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class GestionDashboardService implements GestionDashboardUseCase {

    private final DashboardRepositoryPort repository;

    private static final BigDecimal META_DL = new BigDecimal("47.0");
    private static final BigDecimal META_LC = new BigDecimal("44.5");

    public GestionDashboardService(DashboardRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public List<DashboardProduccionVsEmpaqueResponse> obtenerProduccionVsEmpaque() {
        return repository.obtenerProduccionVsEmpaque();
    }

    @Override
    public DashboardTrazabilidadLoteResponse obtenerTrazabilidadPorLote(String lote) {
        return repository.obtenerTrazabilidadPorLote(lote);
    }

    @Override
    public List<DashboardProduccionSkuResponse> obtenerProduccionSkuMensual(int mes, int anio) {
        return repository.obtenerProduccionSkuMensual(mes, anio);
    }

    @Override
    public List<RendimientoAnualResponse> obtenerRendimientoAnual(int anio) {
        return repository.obtenerRendimientoAnual(anio);
    }

    @Override
    public DashboardGerencialResponse obtenerDashboardGerencial(int mes, int anio) {
        DashboardGerencialResponse.ResumenMesGerencial resumen = repository.obtenerResumenMensualGerencial(mes, anio);
        List<DashboardGerencialResponse.SemanaGerencialResponse> tablaSemanal = repository.obtenerDetalleSemanalGerencial(mes, anio);

        // Calculate Average Yields (Simplified for now)
        BigDecimal rendDL = new BigDecimal("46.5"); 
        BigDecimal rendLC = new BigDecimal("43.8"); 

        DashboardGerencialResponse.KpisRendimiento kpis = new DashboardGerencialResponse.KpisRendimiento(
                rendDL,
                rendLC,
                META_DL,
                META_LC,
                evaluarEstado(rendDL, META_DL),
                evaluarEstado(rendLC, META_LC)
        );

        return new DashboardGerencialResponse(resumen, tablaSemanal, kpis);
    }

    private String evaluarEstado(BigDecimal actual, BigDecimal meta) {
        if (actual == null) return "SIN_DATOS";
        if (actual.compareTo(meta) >= 0) return "OPTIMO";
        if (actual.compareTo(meta.subtract(new BigDecimal("2.0"))) >= 0) return "ALERTA";
        return "CRITICO";
    }
}