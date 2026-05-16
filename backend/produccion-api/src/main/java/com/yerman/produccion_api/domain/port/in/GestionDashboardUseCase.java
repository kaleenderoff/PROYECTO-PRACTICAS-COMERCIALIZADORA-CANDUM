package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.application.dto.response.*;
import java.util.List;

public interface GestionDashboardUseCase {

    List<DashboardProduccionVsEmpaqueResponse> obtenerProduccionVsEmpaque();

    DashboardTrazabilidadLoteResponse obtenerTrazabilidadPorLote(String lote);

    List<DashboardProduccionSkuResponse> obtenerProduccionSkuMensual(int mes, int anio);

    List<RendimientoAnualResponse> obtenerRendimientoAnual(int anio);

    DashboardGerencialResponse obtenerDashboardGerencial(int mes, int anio);
}